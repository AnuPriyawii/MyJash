package com.myjash.app.activity;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.myjash.app.AppUtil.InternetService;
import com.myjash.app.AppUtil.SlidingDrawer;
import com.myjash.app.R;
import com.myjash.app.fragments.DashBoard;
import com.myjash.app.fragments.Offer;
import com.myjash.app.fragments.Pdf;
import com.myjash.app.fragments.Product;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import com.myjash.app.fragments.Search;

public class MainContainer extends AppCompatActivity {
    Activity activity;
    public static Fragment fragmentForBackPress;

    LinearLayout lytHome;
    LinearLayout lytProduct;
    LinearLayout lytPdf;
    LinearLayout lytFlip;
    RelativeLayout lytSearch;
    public static FragmentManager fragmentManager;
    public static DrawerLayout drawerLayout;

    ArrayList<LinearLayout> arrLyt = new ArrayList<LinearLayout>();

    //    Login with FB
    CallbackManager callbackManager;
    AccessTokenTracker accessTokenTracker;
    AccessToken accessToken;
    ProfileTracker profileTracker;
    int backPress = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_container);
        activity = this;
        fragmentManager = getSupportFragmentManager();
        lytFlip = (LinearLayout) findViewById(R.id.lytFlip);
        lytHome = (LinearLayout) findViewById(R.id.lytHome);
        lytPdf = (LinearLayout) findViewById(R.id.lytPdf);
        lytProduct = (LinearLayout) findViewById(R.id.lytProduct);
        lytSearch = (RelativeLayout) findViewById(R.id.lytSearch);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

        /*Set sliding drawer*/
        new SlidingDrawer(this);

        /*@'fragmentForBackPress' define which fragment will inflate while backpress*/
        fragmentForBackPress = new DashBoard();

        /*Set home page*/
        getFragmentManager().beginTransaction()
                .replace(R.id.lytMainContainer, new DashBoard()).addToBackStack(null)
                .commit();

        /*add all layout to an array for changing tab color*/
        arrLyt.add(lytHome);
        arrLyt.add(lytProduct);
        arrLyt.add(lytPdf);
        arrLyt.add(lytFlip);

        /*Set onClick listener*/
        lytFlip.setOnClickListener(onClick);
        lytHome.setOnClickListener(onClick);
        lytSearch.setOnClickListener(onClick);
        lytProduct.setOnClickListener(onClick);
        lytPdf.setOnClickListener(onClick);

        /*Login with FB*/
        FacebookSdk.sdkInitialize(this);
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                    }
                });
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {
                // Set the access token using
                // currentAccessToken when it's loaded or set.
            }
        };
       /* If the access token is available already assign it.*/
        accessToken = AccessToken.getCurrentAccessToken();
        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(
                    Profile oldProfile,
                    Profile currentProfile) {
                // App code
            }
        };
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.facebook.samples.loginhowto",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }

    }

    View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.lytHome:
                    changeTabColor(0);
                    getFragmentManager().beginTransaction()
                            .replace(R.id.lytMainContainer, new DashBoard(), "DASHBOARD").addToBackStack(null)
                            .commit();
                    break;
                case R.id.lytProduct:
                    changeTabColor(1);
                    getFragmentManager().beginTransaction()
                            .replace(R.id.lytMainContainer, new Product(), "PRODUCT").addToBackStack(null)
                            .commit();
                    break;
                case R.id.lytPdf:
                    changeTabColor(2);
                    getFragmentManager().beginTransaction()
                            .replace(R.id.lytMainContainer, new Pdf(), "PDF").addToBackStack(null)
                            .commit();
                    break;
                case R.id.lytFlip:
                    changeTabColor(3);
                    getFragmentManager().beginTransaction()
                            .replace(R.id.lytMainContainer, new Offer(), "OFFER").addToBackStack(null)
                            .commit();
                    break;
                case R.id.lytSearch:
                    getFragmentManager().beginTransaction()
                            .replace(R.id.lytMainContainer, new Search(), "SEARCH").addToBackStack(null)
                            .commit();
                    break;
            }
        }
    };

    /*Change tab color while tab pressed*/
    public void changeTabColor(int position) {
        for (int i = 0; i < arrLyt.size(); i++) {
            if (i == position) {
                arrLyt.get(i).setBackgroundColor(Color.parseColor("#29ffffff"));
            } else {
                arrLyt.get(i).setBackgroundColor(Color.parseColor("#1e1e1e"));
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        /*Set sliding drawer*/
        new SlidingDrawer(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Logs 'app deactivate' App Event.
//        AppEventsLogger.deactivateApp(this);
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*if (requestCode == 10 && resultCode == 10) {
             *//*Its must be Search result*//*
            Intent intent = new Intent();
            intent.putExtra("type", "search");
            Bundle b = intent.getExtras();

            Product fragobj = new Product();
            fragobj.setArguments(b);
            getFragmentManager().beginTransaction()
                    .replace(R.id.lytMainContainer, fragobj).addToBackStack(null)
                    .commit();
            changeTabColor(1);
        } else*/
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        accessTokenTracker.stopTracking();
//        profileTracker.stopTracking();
    }

    @Override
    public void onBackPressed() {
        Fragment f = getFragmentManager().findFragmentById(R.id.lytMainContainer);
        if (f instanceof DashBoard) {
            Log.d("BAckPress","Instanceof db");
            if (backPress > 0)
                finish();
            else
                Toast.makeText(MainContainer.this, "Press again to exit", Toast.LENGTH_SHORT).show();

            backPress++;
        } else {

            Log.d("BAckPress","Not an Instanceof db");
            backPress = 0;
            getFragmentManager().beginTransaction()
                    .replace(R.id.lytMainContainer, fragmentForBackPress, "DASHBOARD").addToBackStack(null)
                    .commit();
            if (fragmentForBackPress instanceof DashBoard) {

                changeTabColor(0);
            }
        }

        fragmentForBackPress = new DashBoard();
    }
}
