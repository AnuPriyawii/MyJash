package com.wiinnova.myjash.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.wiinnova.myjash.AppUtil.InternetService;
import com.wiinnova.myjash.AppUtil.Util;
import com.wiinnova.myjash.R;

import org.json.JSONException;
import org.json.JSONObject;

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
    static Activity activity;
    TextWatcher textWatcher;

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
                } else if (edtCity.getText().toString().length() == 0) {
                    edtCity.requestFocus();
                    edtCity.setError("City");
                } else {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("firstName", edtName.getText().toString());
                    params.put("phone", edtPhone.getText().toString());
                    params.put("email", edtEmail.getText().toString());
                    params.put("password", edtPswd.getText().toString());
                    new InternetService(activity).downloadDataByPOST("register", params, "register");
                }
            }
        });

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


}
