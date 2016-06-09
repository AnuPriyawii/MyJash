package com.myjash.app.AppUtil;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.os.AsyncTask;
import android.support.v4.util.Pair;
import android.util.Base64;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.myjash.app.R;
import com.myjash.app.activity.Login;
import com.myjash.app.activity.Register;
import com.myjash.app.adapter.ArrayAdapterProduct;
import com.myjash.app.app.AppController;
import com.myjash.app.fragments.Brand;
import com.myjash.app.fragments.Category;
import com.myjash.app.fragments.Companies;
import com.myjash.app.fragments.DashBoard;
import com.myjash.app.fragments.Location;
import com.myjash.app.fragments.Mall;
import com.myjash.app.fragments.Offer;
import com.myjash.app.fragments.Pdf;
import com.myjash.app.fragments.Product;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;


/**
 * Created by ubundu on 6/4/16.
 */
public class InternetService {
    Activity activity;
    private String SERVICE_URL = "http://myjashmob.freestech.in/";
    public static String IMG_BASE_URL = "http://www.myjash.freestech.in/images/";
    public static String FLIP_IMG_URL = "http://www.myjash.freestech.in/images/flyers/";
    JSONArray jsonArray = null;
    ProgressDialog pDialog;

    String tag_json_obj = "json_obj_req";
    boolean cacheExist = false;

    public InternetService(Activity activity) {
        this.activity = activity;
        pDialog = new ProgressDialog(activity);
    }

    /*download data from cache memmory by volley library*/
    public JSONArray loadDataFromCache(String url, final String from) {

        try {
            jsonArray = new JSONArray();

            pDialog.setMessage("Loading...");
            pDialog.show();

            Cache cache = AppController.getInstance().getRequestQueue().getCache();
            Cache.Entry entry = cache.get(SERVICE_URL + url);
            if (entry != null) {
                cacheExist = true;
                try {
                    String data = new String(entry.data, "UTF-8");
                    try {
                        JSONObject jsonObject = new JSONObject(data);
                        if (jsonObject.getString("response").contains("Success")) {
                            jsonArray = new JSONArray(jsonObject.getString("result"));
                            Log.d("jsonArray", jsonArray.toString());
                            if (from.equals("company"))
                                Companies.loadImage(jsonArray);
                            else if (from.equals("product")) {
                                Fragment fragment = activity.getFragmentManager().findFragmentById(R.id.lytMainContainer);
                                ((Product) fragment).refreshAdapter(jsonObject);
                            } else if (from.equals("brand"))
                                Brand.refreshAdapter(jsonArray);
                            else if (from.equals("category"))
                                Category.refreshAdapter(jsonArray);
                            else if (from.equals("mall"))
                                Mall.refreshAdapter(jsonArray);
                            else if (from.equals("search"))
                                PopUpSearchCategory.refreshAdapter(jsonArray);
                            else if (from.equals("offer"))
                                Offer.refreshAdapter(jsonArray);
                            else if (from.equals("pdf"))
                                Pdf.refreshAdapter(jsonArray);
                            else if (from.equals("location"))
                                Location.refreshAdapter(jsonArray);
                            else if (from.equals("dashboard"))
                                DashBoard.setViewFlipper(jsonObject);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }


                pDialog.hide();

                downloadDataByGet(url, from, true);
            } else {
                pDialog.hide();
                Log.d("NoEntry", "sdfdd");
                downloadDataByGet(url, from, false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonArray;
    }

    public JSONArray downloadDataByGet(String url, final String from, final boolean refresh) {
        try {
            jsonArray = new JSONArray();
            if (!refresh) {
                pDialog = new ProgressDialog(activity);
                pDialog.setMessage("Loading...");
                pDialog.show();
            }


            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                    SERVICE_URL + url, null,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {

                            try {
                                JSONObject jsonObject = new JSONObject(response.toString());
                                if (jsonObject.getString("response").contains("Success")) {
                                    jsonArray = new JSONArray(jsonObject.getString("result"));
                                    Log.d("jsonArray", jsonArray.toString());
                                    if (from.equals("company"))
                                        Companies.loadImage(jsonArray);
                                    else if (from.equals("product")) {
                                        Fragment fragment = activity.getFragmentManager().findFragmentById(R.id.lytMainContainer);
                                        if (fragment instanceof Product)
                                            ((Product) fragment).refreshAdapter(jsonObject);
                                    } else if (from.equals("brand"))
                                        Brand.refreshAdapter(jsonArray);
                                    else if (from.equals("category"))
                                        Category.refreshAdapter(jsonArray);
                                    else if (from.equals("mall"))
                                        Mall.refreshAdapter(jsonArray);
                                    else if (from.equals("search"))
                                        PopUpSearchCategory.refreshAdapter(jsonArray);
                                    else if (from.equals("offer"))
                                        Offer.refreshAdapter(jsonArray);
                                    else if (from.equals("pdf"))
                                        Pdf.refreshAdapter(jsonArray);
                                    else if (from.equals("location"))
                                        Location.refreshAdapter(jsonArray);
                                    else if (from.equals("dashboard"))
                                        DashBoard.setViewFlipper(jsonObject);
                                    else if (from.equals("popup")) {
                                        ArrayAdapterProduct.getLocationData(jsonArray);
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            if (!refresh)
                                pDialog.hide();
                        }
                    }

                    , new Response.ErrorListener()

            {

                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d("Rspocevolley", "Error: " + error.getMessage());

                    if (!refresh)
                        pDialog.hide();
                }
            }

            )

            {

                /**
                 * Passing some request headers
                 */
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json");
                    headers.put("Key", "12345");
                    return headers;
                }

            };
            Log.d("CacheKey", jsonObjReq.getCacheKey() + " h");
            AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

            Log.d("RspocevolleyLast", jsonArray.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonArray;
    }


    public JSONArray downloadDataByPOST(String url, final Map<String, String> params, final String from) {
        try {
            jsonArray = new JSONArray();
//            pDialog = new ProgressDialog(activity);
            pDialog.setMessage("Loading...");
            pDialog.show();
            Log.d("ParamsGet", params + " dfgfd");

            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    SERVICE_URL + url, new JSONObject(params),
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("jsonArray", response.toString());
//                            Log.d("jsonArray", jsonArray.toString());
                            if (from.equals("search")) {
                                Fragment fragment = activity.getFragmentManager().findFragmentById(R.id.lytMainContainer);
                                ((Product) fragment).refreshAdapter(response);
                            } else if (from.equals("login"))
                                Login.normalLoginResult(response);
                            else if (from.equals("register"))
                                Register.registerResult(response);
                            else if (from.equals("popup")) {
                                ArrayAdapterProduct.displayProduct(response);
                            }
                            pDialog.hide();
                        }
                    }

                    , new Response.ErrorListener()

            {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("Rspocevolley", "Error: " + error.getMessage());
                    pDialog.hide();
                }

            })

            {

                /**
                 * Passing some request headers
                 */
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/form-data");
                    headers.put("Key", "12345");
                    return headers;
                }


            };
            AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonArray;
    }


    public void sendAsFormData() {

        final String lineEnd = "\r\n";
        final String twoHyphens = "--";
        final String boundary = "----WebKitFormBoundary7MA4YWxkTrZu0gW";
        new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... params) {
                try {
                    String urlString = "http://192.168.0.111/doctorsdiary1/public/getPatientData";
                    URL url = new URL(urlString);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(10000);
                    conn.setConnectTimeout(15000);
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Connection", "Keep-Alive");
//                    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//                    final String boundary = "===" + System.currentTimeMillis() + "===";
//                    conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
                    conn.setRequestProperty("Content-Type", "application/json");
                    conn.setRequestProperty("charset", "utf-8");
                    conn.setRequestProperty("key", "12345");
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    conn.setUseCaches(false);
                    OutputStream os = conn.getOutputStream();

                    HashMap<String, String> paramss = new HashMap<String, String>();
                    paramss.put("patientId", "111");

                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(os, "UTF-8"));
                    try {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("patientId", "111");
                        writer.write(jsonObject.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
//                    writer.write(boundary+"\nContent-Disposition: form-data; name=\"uploadedfile\";filename=\"" + getPostDataString(paramss) + "\"" + lineEnd);
                    writer.flush();
                    writer.close();
                    os.close();

                    conn.connect();
                    Log.d("Output", readInputStreamToString(conn));

                } catch (IOException e) {
                    e.printStackTrace();
                }


                /*// Create a new HttpClient and Post Header
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(url);

                try {
                    // Add your data
                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
                    nameValuePairs.add(new BasicNameValuePair("patientId", "111"));
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    httppost.setHeader("key", "12345");

                    // Execute HTTP Post Request
                    HttpResponse response = httpclient.execute(httppost);
                    HttpEntity entity = response.getEntity();
                    InputStream is = entity.getContent();
                    Log.d("Rsponce", convertStreamToString(is));

                } catch (ClientProtocolException e) {
                    // TODO Auto-generated catch block
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                }*/
                return null;
            }
        }.execute();

    }

    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }

    private String readInputStreamToString(HttpURLConnection connection) {
        String result = null;
        StringBuffer sb = new StringBuffer();
        InputStream is = null;

        try {
            is = new BufferedInputStream(connection.getInputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String inputLine = "";
            while ((inputLine = br.readLine()) != null) {
                sb.append(inputLine);
            }
            result = sb.toString();
        } catch (Exception e) {
            Log.i("tag", "Error reading InputStream");
            result = null;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    Log.i("tag", "Error closing InputStream");
                }
            }
        }

        return result;
    }

    public void volley() {

        String url = "http://192.168.0.111/doctorsdiary1/public/getPatientData";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("patientId", "111");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, jsonObject,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("jsonArray", response.toString());

                    }
                }

                , new Response.ErrorListener()

        {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Rspocevolley", "Error: " + error.getMessage());
                pDialog.hide();
            }

        })

        {

            /**
             * Passing some request headers
             */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("Key", "12345");
                return headers;
            }


        };
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
  /*      StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Responchgfguyhge", response.toString());

            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Responcehhbjhbjkkj", "Error: " + error.getMessage());
            }

        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                final String boundary = "===" + System.currentTimeMillis() + "===";
                headers.put("Content-Type", "application/json");
                headers.put("Key", "12345");
                return headers;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("patientId", "111");
                return params;
            }
        };

// Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);*/
    }

}
