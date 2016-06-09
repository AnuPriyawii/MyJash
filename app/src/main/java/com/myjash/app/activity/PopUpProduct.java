package com.myjash.app.activity;

import android.animation.LayoutTransition;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.GpsStatus;
import android.os.AsyncTask;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.myjash.app.AppUtil.CircularNetworkImageView;
import com.myjash.app.AppUtil.HeaderAction;
import com.myjash.app.AppUtil.InternetService;
import com.myjash.app.AppUtil.LruBitmapCache;
import com.myjash.app.AppUtil.MyGridView;
import com.myjash.app.AppUtil.SlidingDrawer;
import com.myjash.app.AppUtil.Util;
import com.myjash.app.AppUtil.VerticalSpaceItemDecoration;
import com.myjash.app.R;
import com.myjash.app.adapter.ArrayAdapterBranches;
import com.myjash.app.adapter.ArrayAdapterProduct;
import com.myjash.app.adapter.ArrayAdpterProductMall;
import com.myjash.app.app.AppController;
import com.myjash.app.fragments.Product;
import com.myjash.app.model.ProductModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.myjash.app.AppUtil.CircleImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PopUpProduct extends AppCompatActivity {
    static Activity activity;
    ImageView imgProduct;
    ImageView imgLogo;
    View testView;
    TextView txtProdName;
    TextView txtCompany;
    TextView txtPlace;
    TextView txtOldPrice;
    TextView txtNewPrice;
    TextView txtDesc;
    TextView txtEpiry;
    TextView txtStartDate;
    LinearLayout lytLocation;

    static RecyclerView recyclerBranch;
    static RecyclerView recyclerMall;
    ScrollView scrollView;
    static ArrayList<ProductModel> arrBranches;
    static String[] arrMall;
    static ArrayAdapterBranches adapterBranches;
    static int postionClicked;
    static String locationId;
    static ProductModel model;
    LinearLayout lytImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_up_product);
        activity = this;
        imgLogo = (ImageView) findViewById(R.id.imgLogo);
        imgProduct = (ImageView) findViewById(R.id.imgProduct);
        txtCompany = (TextView) findViewById(R.id.txtCompany);
        txtNewPrice = (TextView) findViewById(R.id.txtNewPrice);
        txtOldPrice = (TextView) findViewById(R.id.txtOldPrice);
        txtPlace = (TextView) findViewById(R.id.txtPlace);
        txtProdName = (TextView) findViewById(R.id.txtProductName);
        txtDesc = (TextView) findViewById(R.id.txtDesc);
        txtEpiry = (TextView) findViewById(R.id.txtExpiryDate);
        txtStartDate = (TextView) findViewById(R.id.txtStartDate);
        recyclerBranch = (RecyclerView) findViewById(R.id.recyclerBranches);
        recyclerMall = (RecyclerView) findViewById(R.id.recyclerMall);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        lytLocation = (LinearLayout) findViewById(R.id.lytLocation);
        testView = (View) findViewById(R.id.testView);
        lytImg = (LinearLayout) findViewById(R.id.lytImg);


        /*Set header*/
        new HeaderAction(null, this);

        scrollView.fullScroll(ScrollView.FOCUS_UP);
        scrollView.smoothScrollTo(0, 0);

        arrBranches = new ArrayList<>();

        postionClicked = getIntent().getExtras().getInt("position");
        if (Product.arrProd.size() > postionClicked) {
            model = Product.arrProd.get(postionClicked);
        } else {
            model = new ProductModel();
        }


        /*Set fontType*/
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/fabfeltscripts.TTF");
        txtOldPrice.setTypeface(typeface);
        txtNewPrice.setTypeface(typeface);

        /*Set sliding drawer*/
        new SlidingDrawer(this);

        txtProdName.setText(model.getName().toUpperCase());
        txtCompany.setText(model.getBrand());
        txtPlace.setText(model.getPlace());
        txtOldPrice.setText(model.getOldRate());
        txtNewPrice.setText(model.getNewRate());
        txtDesc.setText(model.getDescription());
        txtEpiry.setText("Expiry date: " + model.getExprDate());
        txtStartDate.setText("Start date: " + model.getStartDate());


        displayLogo();


        getBranchDetails(ArrayAdapterProduct.jsonObject);
    }

    public void viewLocation(View v) {

        if (arrBranches.size() > 0) {
            Intent intent = new Intent(activity, GoogleMap.class);
            intent.putExtra("array", arrBranches);
            startActivity(intent);
        } else {
            final Animation aAnim = new AlphaAnimation(1.0f, 0.0f);
            aAnim.setDuration(50);
            aAnim.setFillAfter(false);
            v.startAnimation(aAnim);
            Toast.makeText(activity, "Location data not found!!!", Toast.LENGTH_SHORT).show();
        }
    }

    public void displayLogo() {

        Glide.with(activity).load(model.getUrl())
                .asBitmap()
                .thumbnail(1.f)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(new BitmapImageViewTarget(imgLogo) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(activity.getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        imgLogo.setImageDrawable(circularBitmapDrawable);
                    }
                });
        Glide.with(activity).load(model.getProdUrl())
                .asBitmap()
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(new BitmapImageViewTarget(imgProduct) {
                    @Override
                    protected void setResource(final Bitmap resource) {

                        if (resource != null) {
                            testView.setVisibility(View.VISIBLE);
                            imgProduct.setVisibility(View.GONE);
                            imgProduct.setVisibility(View.VISIBLE);
                            imgProduct.setImageBitmap(resource);
                           /* LayoutTransition layoutTransition = lytImg.getLayoutTransition();
                            layoutTransition.addTransitionListener(new LayoutTransition.TransitionListener() {
                                @Override
                                public void startTransition(LayoutTransition transition, ViewGroup container, View view, int transitionType) {

                                }

                                @Override
                                public void endTransition(LayoutTransition transition, ViewGroup container, View view, int transitionType) {
                                    imgProduct.setImageBitmap(resource);
                                }
                            });*/

                        }
                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        super.onLoadFailed(e, errorDrawable);
                        imgProduct.setVisibility(View.GONE);
                        testView.setVisibility(View.GONE);
                      /*  Animation animation = AnimationUtils.loadAnimation(activity, R.anim.slide_out_to_top);
                        animation.setFillAfter(true);
                        animation.setRepeatCount(0);
                        animation.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                imgProduct.setVisibility(View.GONE);
                                testView.setVisibility(View.GONE);
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });
                        imgProduct.startAnimation(animation);*/

                    }
                });
    }


    public static void getBranchDetails(JSONObject response) {
        String mallList = "";
        try {
            JSONArray jsonArray = new JSONArray(response.getString("result"));
            for (int k = 0; k < jsonArray.length(); k++) {
                JSONObject jsonObject = jsonArray.getJSONObject(k);
                mallList = jsonObject.getString("mall_names");
                String branch_details = jsonObject.getString("branch_details");
                JSONArray jArrBranch = new JSONArray(branch_details);
                for (int j = 0; j < jArrBranch.length(); j++) {
                    ProductModel model = new ProductModel();
                    model.setName(jArrBranch.getJSONObject(j).getString("b_name"));
                    model.setAddress(jArrBranch.getJSONObject(j).getString("address"));
                    model.setPhone(jArrBranch.getJSONObject(j).getString("phone"));
                    model.setEmail(jArrBranch.getJSONObject(j).getString("email"));
                    model.setLatitude(jArrBranch.getJSONObject(j).getString("lat"));
                    model.setLongitude(jArrBranch.getJSONObject(j).getString("lng"));
                    model.setPhone(jArrBranch.getJSONObject(j).getString("phone"));
                    arrBranches.add(model);
                }
                 /*Add location of shop too*/
                model.setVendorName(jsonObject.getString("v_name"));
                model.setLatitude(jsonObject.getString("v_lat"));
                model.setLongitude(jsonObject.getString("v_lng"));
                model.setAddress(jsonObject.getString("v_address"));
                model.setPhone(jsonObject.getString("v_phone"));
                arrBranches.add(model);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("ArrBranchDetails", arrBranches.size() + " d");

    }
}
