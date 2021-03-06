package com.myjash.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.myjash.app.AppUtil.InternetService;
import com.myjash.app.AppUtil.Util;
import com.myjash.app.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {
    Button btnSignIn;
    Button btnSignUp;
    EditText edtName;
    EditText edtPhone;
    EditText edtEmail;
    EditText edtCity;
    EditText edtPswd;
    Spinner spnrRegion;
    Spinner spnrLocation;
    static Activity activity;
    TextWatcher textWatcher;
    ArrayAdapter<String> arrayAdapterRegion;
    ArrayList<String> arrayRegion;
    ArrayList<Integer> arrayRegionId;
    ArrayAdapter<String> arrayAdapterLocation;
    ArrayList<String> arrayLocation;
    ArrayList<Integer> arrayLocationId;

    int regionId;
    int locationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        activity = this;
        btnSignIn = (Button) findViewById(R.id.btnSignIn);
        btnSignUp = (Button) findViewById(R.id.btnSignUp);
        edtCity = (EditText) findViewById(R.id.edtCity);
        edtEmail = (EditText) findViewById(R.id.edtEmai);
        edtName = (EditText) findViewById(R.id.edtName);
        edtPhone = (EditText) findViewById(R.id.edtPhone);
        edtPswd = (EditText) findViewById(R.id.edtPassword);
        spnrLocation = (Spinner) findViewById(R.id.spnrLocation);
        spnrRegion = (Spinner) findViewById(R.id.spnrRegion);

        arrayRegion = new ArrayList<>();
        arrayRegionId = new ArrayList<>();


        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this, Login.class);
                startActivity(intent);
                finish();
            }
        });
        textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                edtCity.setError(null);
                edtName.setError(null);
                edtEmail.setError(null);
                edtPhone.setError(null);
                edtPswd.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        edtEmail.addTextChangedListener(textWatcher);
        edtPhone.addTextChangedListener(textWatcher);
        edtCity.addTextChangedListener(textWatcher);
        edtName.addTextChangedListener(textWatcher);
        edtPswd.addTextChangedListener(textWatcher);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtName.getText().toString().length() == 0) {
                    edtName.requestFocus();
                    edtName.setError("Name");
                } else if (edtEmail.getText().toString().length() == 0) {
                    edtEmail.requestFocus();
                    edtEmail.setError("Email");
                } else if (!Util.isValidEmail(edtEmail.getText())) {
                    edtEmail.requestFocus();
                    edtEmail.setError("Invalid");
                } else if (edtPswd.getText().toString().length() == 0) {
                    edtPswd.requestFocus();
                    edtPswd.setError("Password");
                } else if (edtPhone.getText().toString().length() == 0) {
                    edtPhone.requestFocus();
                    edtPhone.setError("Phone");
                }/* else if (edtCity.getText().toString().length() == 0) {
                    edtCity.requestFocus();
                    edtCity.setError("City");
                } */else {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("firstName", edtName.getText().toString());
                    params.put("phone", edtPhone.getText().toString());
                    params.put("email", edtEmail.getText().toString());
                    params.put("password", edtPswd.getText().toString());
                    params.put("regionId", String.valueOf(regionId));
                    params.put("locationId", String.valueOf(locationId));
                    new InternetService(activity).downloadDataByPOST("register", params, "register");
                }
            }
        });

        /*Download region */
        new InternetService(activity).downloadDataByGet("getRegion", "region", false);

    }

    public static void registerResult(JSONObject jsonObject) {
        try {
            String response = jsonObject.getString("response");
            String msg = new JSONObject(response).getString("msg");
            Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void getRegion(JSONObject jsonObject) {
        try {
            String response = jsonObject.getString("response");
            String msg = new JSONObject(response).getString("msg");
//            Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
            if (msg.equals("Success")) {
                String result = jsonObject.getString("result");
                JSONArray jsonArray = new JSONArray(result);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject json = jsonArray.getJSONObject(i);
                    arrayRegion.add(i, json.getString("r_name"));
                    arrayRegionId.add(i, Integer.parseInt(json.getString("r_id")));
                }
                arrayAdapterRegion = new ArrayAdapter<String>(activity, R.layout.layout_list_spinner_item, arrayRegion);
                spnrRegion.setAdapter(arrayAdapterRegion);
                spnrRegion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        regionId = arrayRegionId.get(position);
                        locationId = 0;
                        Log.d("regionIDDDD", regionId + "");

                        if (arrayLocation != null) {
                            arrayLocation.clear();
                            arrayAdapterLocation.notifyDataSetChanged();
                        }
                        int id_region = arrayRegionId.get(position);
                        new InternetService(activity).downloadDataByGet("getLocation?regionId=" + id_region, "location_register", false);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getLocation(JSONObject jsonObject) {
        try {
            arrayLocation = new ArrayList<>();
            arrayLocationId = new ArrayList<>();

            String response = jsonObject.getString("response");
            String msg = new JSONObject(response).getString("msg");
//            Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
            if (msg.equals("Success")) {
                String result = jsonObject.getString("result");
                JSONArray jsonArray = new JSONArray(result);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject json = jsonArray.getJSONObject(i);
                    arrayLocation.add(i, json.getString("l_name"));
                    arrayLocationId.add(i, Integer.parseInt(json.getString("l_id")));
                }
            }
            arrayAdapterLocation = new ArrayAdapter<String>(activity, R.layout.layout_list_spinner_item, arrayLocation);
            spnrLocation.setAdapter(arrayAdapterLocation);
            spnrLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    locationId = arrayLocationId.get(position);
                    Log.d("LocationIDDDD", locationId + "");
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
