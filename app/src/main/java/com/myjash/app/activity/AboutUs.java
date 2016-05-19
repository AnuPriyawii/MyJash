package com.myjash.app.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.myjash.app.AppUtil.SlidingDrawer;
import com.myjash.app.R;

public class AboutUs extends AppCompatActivity {
    TextView txtTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        txtTitle = (TextView) findViewById(R.id.txtHeaderTitle);
        txtTitle.setText("ABOUT US");

        /*Sliding drawer*/
        new SlidingDrawer(this);
    }
}
