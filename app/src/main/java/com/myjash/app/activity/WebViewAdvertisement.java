package com.myjash.app.activity;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.myjash.app.R;

public class WebViewAdvertisement extends AppCompatActivity {
    Activity activity;
    RelativeLayout lytMenu;
    Button btnClose;
    WebView webView;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view_advertisement);
        activity = this;
        btnClose = (Button) findViewById(R.id.btnClose);
        webView = (WebView) findViewById(R.id.webView);
        /*remove menu icon from header*/
        lytMenu = (RelativeLayout) findViewById(R.id.lytMenu);
        lytMenu.setVisibility(View.GONE);

        url = getIntent().getStringExtra("url");

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        /*WebView*/
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                // Activities and WebViews measure progress with different scales.
                // The progress meter will automatically disappear when we reach 100%
                activity.setProgress(progress * 1000);
                if (progress >50) {
                    webView.setVisibility(View.VISIBLE);
                }
                Log.d("Progress", progress + " fdg");
            }
        });
        webView.setWebViewClient(new WebViewClient() {
//            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
//                Toast.makeText(activity, "Oh no! " + description, Toast.LENGTH_SHORT).show();
//            }

        });

        webView.loadUrl(url);


    }
}
