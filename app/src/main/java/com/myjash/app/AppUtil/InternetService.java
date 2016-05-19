package com.myjash.app.AppUtil;

import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;


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
    }

    /*download data from cache memmory by volley library*/
    public JSONArray loadDataFromCache(String url, final String from) {

        try {
            jsonArray = new JSONArray();
            pDialog = new ProgressDialog(activity);
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
                            else if (from.equals("product"))
                                Product.refreshAdapter(jsonObject);
                            else if (from.equals("brand"))
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
                                    else if (from.equals("product"))
                                        Product.refreshAdapter(jsonObject);
                                    else if (from.equals("brand"))
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
            pDialog = new ProgressDialog(activity);
            pDialog.setMessage("Loading...");
            pDialog.show();
            Log.d("ParamsGet", params + " dfgfd");

            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    SERVICE_URL + url, new JSONObject(params),
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("jsonArray", response.toString());
                            Log.d("jsonArray", jsonArray.toString());
                            if (from.equals("search"))
                                Product.refreshAdapter(response);
                            else if (from.equals("login"))
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

}
