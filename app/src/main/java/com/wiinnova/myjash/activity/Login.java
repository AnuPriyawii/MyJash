package com.wiinnova.myjash.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.wiinnova.myjash.AppUtil.InternetService;
import com.wiinnova.myjash.AppUtil.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

import com.wiinnova.myjash.R;
import com.wiinnova.myjash.adapter.ArrayAdpterDrawer;

public class Login extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    Button btnSignUp;
    Button btnSignIn;
    RelativeLayout lytFb;
    RelativeLayout lytTwitter;
    RelativeLayout lytGoogle;
    public static EditText edtUserName;
    public static EditText edtPassword;
    static Activity activity;

    /////////Login with Fb
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    public static boolean flag = false;

    //////////////////Login with twitter

    static String TWITTER_CONSUMER_KEY = "OnCNtz8yB9g1FUV7MEcTtgWpa";
    static String TWITTER_CONSUMER_SECRET = "xuQayPnlDjkXJdb1Sr6CaRWReVLzuynW1ENPS0Pdvyv0uSh4xJ";

    // Preference Constants
    static String PREFERENCE_NAME = "twitter_oauth";
    static final String PREF_KEY_OAUTH_TOKEN = "oauth_token";
    static final String PREF_KEY_OAUTH_SECRET = "oauth_token_secret";
    static final String PREF_KEY_TWITTER_LOGIN = "isTwitterLogedIn";

    static final String TWITTER_CALLBACK_URL = "oauth://t4jsample";

    // Twitter oauth urls
    static final String URL_TWITTER_AUTH = "auth_url";
    static final String URL_TWITTER_OAUTH_VERIFIER = "oauth_verifier";
    static final String URL_TWITTER_OAUTH_TOKEN = "oauth_token";


    // Progress dialog
    ProgressDialog pDialog;

    // Twitter
    private static Twitter twitter;
    private static RequestToken requestToken;

    // Shared Preferences
    private static SharedPreferences mSharedPreferences;

    ////////////////////////////////Login with google plus
    public static GoogleApiClient google_api_client;
    GoogleApiAvailability google_api_availability;
    SignInButton signIn_btn;
    private static final int SIGN_IN_CODE = 0;
    private static final int PROFILE_PIC_SIZE = 120;
    private ConnectionResult connection_result;
    private boolean is_intent_inprogress;
    private boolean is_signInBtn_clicked;
    private int request_code;
    ProgressDialog progress_dialog;
    boolean logout = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        activity = this;
        btnSignUp = (Button) findViewById(R.id.btnSignUp);
        btnSignIn = (Button) findViewById(R.id.btnSignIn);
        lytFb = (RelativeLayout) findViewById(R.id.lytFb);
        lytTwitter = (RelativeLayout) findViewById(R.id.lytTwitter);
        lytGoogle = (RelativeLayout) findViewById(R.id.lytGoogle);
        signIn_btn = (SignInButton) findViewById(R.id.sign_in_button);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        edtUserName = (EditText) findViewById(R.id.edtUserName);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
                finish();
            }
        });
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtPassword.setError(null);
                edtUserName.setError(null);
                if (edtUserName.getText().toString().length() == 0) {
                    edtUserName.requestFocus();
                    edtUserName.setError("Name");
                } else if (edtPassword.getText().toString().length() == 0) {
                    edtPassword.requestFocus();
                    edtPassword.setError("password");
                } else {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("email", edtUserName.getText().toString());
                    params.put("password", edtPassword.getText().toString());
                    new InternetService(activity).downloadDataByPOST("login", params, "login");
                }
            }
        });
        /////////////////////login with google plus

        google_api_availability = GoogleApiAvailability.getInstance();

        progress_dialog = new ProgressDialog(Login.this);
        lytGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout = false;
                buidNewGoogleApiClient();
                google_api_client.connect();

                progress_dialog.setMessage("Signing in....");
                progress_dialog.show();
                gPlusSignIn();


            }
        });


        //////////////////////Login with Fb
        FacebookSdk.sdkInitialize(this);
        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList(
                "public_profile", "email", "user_birthday", "user_friends"));

        lytFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Util.haveNetworkConnection(Login.this)) {
                    flag = true;
                    LoginManager.getInstance().logInWithReadPermissions(Login.this, (Arrays.asList("public_profile", "user_friends", "user_birthday", "user_about_me", "email")));
                    loginButton.performClick();
                }
                LoginManager.getInstance().registerCallback(callbackManager,
                        new FacebookCallback<LoginResult>() {
                            @Override
                            public void onSuccess(LoginResult loginResult) {

                                Log.d("AccessTocken", loginResult.getAccessToken() + " g");
                                GraphRequest request = GraphRequest.newMeRequest(
                                        loginResult.getAccessToken(),
                                        new GraphRequest.GraphJSONObjectCallback() {
                                            @Override
                                            public void onCompleted(JSONObject object, GraphResponse response) {
                                                Log.v("LoginActivity", object.toString());
//                                                object = new JSONObject(response.toString());
                                                // Application code
                                                try {
                                                    String email = object.getString("email");
//                                                    String birthday = object.getString("birthday"); // 01/31/1980 format
                                                    String name = object.getString("name");
                                                    String gender = object.getString("gender");
                                                    Log.d("DAtatGet", object.toString());
                                                    edtUserName.setText("");
                                                    edtPassword.setText("");
                                                    edtUserName.setText(name);
                                                    edtPassword.setText(email);

                                                    mSharedPreferences = getApplicationContext().getSharedPreferences(
                                                            "LOGIN", 0);
                                                    SharedPreferences.Editor editor = mSharedPreferences.edit();
                                                    editor.putString("NAME", name);
                                                    editor.commit();
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });
                                Bundle parameters = new Bundle();
                                parameters.putString("fields", "id,name,email,gender,birthday");
                                request.setParameters(parameters);
                                request.executeAsync();

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

            }
        });

        //////////////////////////////////Login with Twitter
        // Check if Internet present
        if (!Util.haveNetworkConnection(this)) {
            // Internet Connection is not present
            Toast.makeText(this, "Internet Connection Error.Please connect to working Internet connection", Toast.LENGTH_SHORT).show();

            return;
        }

        // Check if twitter keys are set
        if (TWITTER_CONSUMER_KEY.trim().length() == 0 || TWITTER_CONSUMER_SECRET.trim().length() == 0) {

            return;
        }

        // All UI elements

        // Shared Preferences
        mSharedPreferences = getApplicationContext().getSharedPreferences(
                "LOGIN", 0);

        /**
         * Twitter login button click event will call loginToTwitter() function
         * */
        lytTwitter.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // Call login twitter function
                loginToTwitter();
            }
        });


        /** This if conditions is tested once is
         * redirected from twitter page. Parse the uri to get oAuth
         * Verifier
         * */
        if (!isTwitterLoggedInAlready()) {
            Log.d("isTwitterLoggedInAlr", isTwitterLoggedInAlready() + " f");
            Uri uri = getIntent().getData();
            Log.d("isTwitterLoggedInAlr", uri + " f");
            if (uri != null && uri.toString().startsWith(TWITTER_CALLBACK_URL)) {
                // oAuth verifier
                final String verifier = uri
                        .getQueryParameter(URL_TWITTER_OAUTH_VERIFIER);
                Log.d("isTwitterLoggedInAlr", verifier + " f");

                new AsyncTask<String, String, String>() {
                    String name, email;

                    @Override
                    protected String doInBackground(String... params) {
                        // Get the access token
                        try {
                            AccessToken accessToken = twitter.getOAuthAccessToken(
                                    requestToken, verifier);

                            Log.d("isTwitterLoggedInAlr", accessToken.getToken() + " token");
                            // Shared Preferences
                            SharedPreferences.Editor e = mSharedPreferences.edit();

                            Log.d("isTwitterLoggedInAlr", accessToken.getTokenSecret() + " f");
                            // After getting access token, access token secret
                            // store them in application preferences
                            e.putString(PREF_KEY_OAUTH_TOKEN, accessToken.getToken());
                            Log.d("isTwitterLoggedInAlr", accessToken.getTokenSecret() + " f");
                            e.putString(PREF_KEY_OAUTH_SECRET,
                                    accessToken.getTokenSecret());
                            Log.d("isTwitterLoggedInAlr", accessToken.getTokenSecret() + " f");
                            // Store login status - true
                            e.putBoolean(PREF_KEY_TWITTER_LOGIN, true);
                            Log.d("isTwitterLoggedInAlr", accessToken.getTokenSecret() + " f");
                            e.commit(); // save changes
                            Log.d("isTwitterLoggedInAlr", accessToken.getTokenSecret() + " f");

                            Log.e("Twitter OAuth Token", "> " + accessToken.getToken());


                            // Getting user details from twitter
                            // For now i am getting his name only

                            long userID = accessToken.getUserId();
                            User user = twitter.showUser(userID);
                            name = user.getName();


                            Log.d("isTwitterLoggedInAlr", name + " f");
                        } catch (TwitterException ex) {
                            // Check log for login errors
                            Log.e("Twitter Login Error", "> " + ex.getMessage());
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(String s) {
                        super.onPostExecute(s);
                        edtUserName.setText("");
                        edtPassword.setText("");
                        edtUserName.setText(name);
                        SharedPreferences.Editor editor = mSharedPreferences.edit();
                        editor.putString("NAME", name);
                        editor.commit();
                    }
                }.execute();

                // Displaying in xml ui

            }
        }


    }

    /**
     * Function to login twitter
     */

    private void loginToTwitter() {
        // Check if already logged in
        if (!isTwitterLoggedInAlready()) {
            ConfigurationBuilder builder = new ConfigurationBuilder();
            builder.setOAuthConsumerKey(TWITTER_CONSUMER_KEY);
            builder.setOAuthConsumerSecret(TWITTER_CONSUMER_SECRET);
            Configuration configuration = builder.build();

            TwitterFactory factory = new TwitterFactory(configuration);
            twitter = factory.getInstance();

            new AsyncTask<String, String, String>() {
                @Override
                protected String doInBackground(String... params) {
                    try {
                        requestToken = twitter
                                .getOAuthRequestToken(TWITTER_CALLBACK_URL);
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri
                                .parse(requestToken.getAuthenticationURL())));
                    } catch (TwitterException e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            }.execute();

        } else {
            // user already logged into twitter
            Toast.makeText(getApplicationContext(),
                    "Already Logged into twitter", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Check user already logged in your application using twitter Login flag is
     * fetched from Shared Preferences
     */
    private boolean isTwitterLoggedInAlready() {
        // return twitter login status from Shared Preferences

        return mSharedPreferences.getBoolean(PREF_KEY_TWITTER_LOGIN, false);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (flag) {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
        if (requestCode == SIGN_IN_CODE) {
            request_code = requestCode;
            if (resultCode != RESULT_OK) {
                is_signInBtn_clicked = false;
                progress_dialog.dismiss();

            }

            is_intent_inprogress = false;

            if (!google_api_client.isConnecting()) {
                google_api_client.connect();
            }
        }
    }

    //////////////////////////////login with google plus
    /*
    create and  initialize GoogleApiClient object to use Google Plus Api.
    While initializing the GoogleApiClient object, request the Plus.SCOPE_PLUS_LOGIN scope.
    */

    public void buidNewGoogleApiClient() {
        Log.d("googleplus", "connect");
        /*disconnect running google api client*/
        if (google_api_client != null) {
            google_api_client.stopAutoManage(this);
            google_api_client.disconnect();
        }
        google_api_client = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API, Plus.PlusOptions.builder().build())
//                .addApi(Drive.API)
//                .addScope(Drive.SCOPE_FILE)
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .addScope(Plus.SCOPE_PLUS_PROFILE)
                .enableAutoManage(this, 0, this)

                .build();

    }


    protected void onStart() {
        super.onStart();
//        google_api_client.connect();
    }

    protected void onStop() {
        super.onStop();
        if (google_api_client != null)
            if (google_api_client.isConnected()) {
                google_api_client.disconnect();
            }
    }

    protected void onResume() {
        super.onResume();
        if (google_api_client != null) {
            if (google_api_client.isConnected()) {
                google_api_client.connect();
            }
        }
    }

    /*Logout google plus*/
    public void logoutGooglePlus() {
        try {
            if (google_api_client.isConnected()) {
                Plus.AccountApi.clearDefaultAccount(google_api_client);
                google_api_client.disconnect();
                google_api_client.connect();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.d("userconnected", "Failed");
        if (!result.hasResolution()) {
            google_api_availability.getErrorDialog(this, result.getErrorCode(), request_code).show();
            progress_dialog.hide();
            return;
        } else {

            if (!is_intent_inprogress) {

                connection_result = result;

                if (is_signInBtn_clicked) {

                    resolveSignInError();
                }
            }
        }

    }

    @Override
    public void onConnected(Bundle arg0) {
        progress_dialog.hide();
        is_signInBtn_clicked = false;
        getProfileInfo();
    }

    @Override
    public void onConnectionSuspended(int arg0) {
        Log.d("userconnected", "Suspended");
        google_api_client.connect();
    }
    /*
      Sign-in into the Google + account
     */

    private void gPlusSignIn() {
        if (!google_api_client.isConnected()) {
            is_signInBtn_clicked = true;
//            progress_dialog.show();
            resolveSignInError();
//            progress_dialog.hide();
        } else {
            progress_dialog.hide();
            getProfileInfo();
        }
    }
    /*
      Method to resolve any signin errors
     */

    private void resolveSignInError() {
        if (connection_result != null) {
            if (connection_result.hasResolution()) {
                try {
                    is_intent_inprogress = true;
                    connection_result.startResolutionForResult(this, SIGN_IN_CODE);
                    Log.d("resolve error", "sign in error resolved");
                } catch (IntentSender.SendIntentException e) {
                    is_intent_inprogress = false;
                    google_api_client.connect();
                }
            }
        } else {
            progress_dialog.hide();

        }
    }


    private void getProfileInfo() {
        progress_dialog.hide();
        /*Plus.PeopleApi.load(google_api_client, "411089945761").setResultCallback(new ResultCallback<People.LoadPeopleResult>() {
            @Override
            public void onResult(@NonNull People.LoadPeopleResult loadPeopleResult) {
                if (loadPeopleResult.getPersonBuffer() != null) {
                    Person person = loadPeopleResult.getPersonBuffer().get(0);
                    Log.d("ResultGet", "Person loaded");
                    Log.d("ResultGet", person.getName().getGivenName());
                    Log.d("ResultGet", person.getName().getFamilyName());
                    Log.d("ResultGet", person.getDisplayName());
                    Log.d("ResultGet", person.getGender() + "");
                }
            }
        });*/
        if (Plus.PeopleApi.getCurrentPerson(google_api_client) != null) {
            Person currentPerson = Plus.PeopleApi.getCurrentPerson(google_api_client);
            String personName = currentPerson.getDisplayName();
            String personPhotoUrl = currentPerson.getImage().getUrl();
            String personGooglePlusProfile = currentPerson.getUrl();
            String email = Plus.AccountApi.getAccountName(google_api_client);

            Log.e("ResultGet", "Name: " + personName + ", plusProfile: "
                    + personGooglePlusProfile + ", email: " + email
                    + ", Image: " + personPhotoUrl);
            edtUserName.setText("");
            edtPassword.setText("");
            edtUserName.setText(personName);
            edtPassword.setText(email);
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putString("NAME", personName);
            editor.commit();
        } else {
            Log.d("ResultGet", "null");
        }
//        try {
//            Plus.PeopleApi.load(google_api_client, "411089945761-7nd175t1e0ne77e35knnf6b3i0lkik5v.apps.googleusercontent.com").setResultCallback(new ResultCallback<People.LoadPeopleResult>() {
//                @Override
//                public void onResult(@NonNull People.LoadPeopleResult loadPeopleResult) {
//                    Person person = loadPeopleResult.getPersonBuffer().get(0);
//                    Log.d("GoogleResult", "Person loaded");
//                    Log.d("GoogleResult", person.getName().getGivenName());
//                    Log.d("GoogleResult", person.getName().getFamilyName());
//                    Log.d("GoogleResult", person.getDisplayName());
//                    Log.d("GoogleResult", person.getGender() + "");
//                }
//            });
////            if (Plus.PeopleApi.getCurrentPerson(google_api_client) != null) {
////                Person currentPerson = Plus.PeopleApi.getCurrentPerson(google_api_client);
////                setPersonalInfo(currentPerson);
////
////            } else {
////                Toast.makeText(getApplicationContext(),
////                        "No Personal info mention", Toast.LENGTH_LONG).show();
////
////            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

    }

 /*
     set the User information into the views defined in the layout
     */

    private void setPersonalInfo(Person currentPerson) {

        String personName = currentPerson.getDisplayName();
        String personPhotoUrl = currentPerson.getImage().getUrl();
//        String email = Plus.AccountApi.getAccountName(google_api_client);
        Log.d("GoogleOutput", personName + " fv " + personPhotoUrl);
//        TextView   user_name = (TextView) findViewById(R.id.userName);
//        user_name.setText("Name: "+personName);
//        TextView gemail_id = (TextView)findViewById(R.id.emailId);
//        gemail_id.setText("Email Id: " +email);
//        TextView dob = (TextView)findViewById(R.id.dob);
//        dob.setText("DOB: "+currentPerson.getBirthday());
//        TextView tag_line = (TextView)findViewById(R.id.tag_line);
//        tag_line.setText("Tag Line: " +currentPerson.getTagline());
//        TextView about_me = (TextView)findViewById(R.id.about_me);
//        about_me.setText("About Me: "+currentPerson.getAboutMe());
//        setProfilePic(personPhotoUrl);
        progress_dialog.dismiss();
        Toast.makeText(this, "Person information is shown!", Toast.LENGTH_LONG).show();
    }
/*
     By default the profile pic url gives 50x50 px image.
     If you need a bigger image we have to change the query parameter value from 50 to the size you want
    */

    private void setProfilePic(String profile_pic) {
        profile_pic = profile_pic.substring(0,
                profile_pic.length() - 2)
                + PROFILE_PIC_SIZE;
//        ImageView    user_picture = (ImageView)findViewById(R.id.profile_pic);
//        new LoadProfilePic(user_picture).execute(profile_pic);
    }

    /*Normal login*/
    public static void normalLoginResult(JSONObject jsonObject) {
        try {
            String response = jsonObject.getString("response");
            String msg = new JSONObject(response).getString("msg");
            Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
            if (msg.equalsIgnoreCase("success")) {
                SharedPreferences.Editor editor = mSharedPreferences.edit();
                editor.putString("NAME", edtUserName.getText().toString());
                editor.commit();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
