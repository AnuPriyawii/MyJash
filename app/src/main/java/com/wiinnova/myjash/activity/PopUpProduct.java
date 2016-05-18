package com.wiinnova.myjash.activity;

import android.graphics.Typeface;
import android.location.GpsStatus;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.wiinnova.myjash.AppUtil.CircularNetworkImageView;
import com.wiinnova.myjash.AppUtil.LruBitmapCache;
import com.wiinnova.myjash.AppUtil.MyGridView;
import com.wiinnova.myjash.AppUtil.SlidingDrawer;
import com.wiinnova.myjash.R;
import com.wiinnova.myjash.adapter.ArrayAdapterBranches;
import com.wiinnova.myjash.adapter.ArrayAdpterProductMall;
import com.wiinnova.myjash.app.AppController;
import com.wiinnova.myjash.fragments.Product;
import com.wiinnova.myjash.model.ProductModel;

import java.util.ArrayList;

public class PopUpProduct extends AppCompatActivity {
    NetworkImageView imgProduct;
    CircularNetworkImageView imgLogo;
    CircularNetworkImageView imgLogoFirst;
    RelativeLayout lytLogoMain;
    RelativeLayout lytLogoFirst;
    TextView txtProdName;
    TextView txtCompany;
    TextView txtPlace;
    TextView txtOldPrice;
    TextView txtNewPrice;
    TextView txtDesc;
    RecyclerView recyclerBranch;
    RecyclerView recyclerMall;
    ScrollView scrollView;
    int position;
    ArrayList<String> arrayList;
    ArrayList<String> arrBranches;
    ArrayAdapterBranches adapterBranches;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_up_product);
        imgLogo = (CircularNetworkImageView) findViewById(R.id.imgLogo);
        imgLogoFirst = (CircularNetworkImageView) findViewById(R.id.imgLogoFirst);
        imgProduct = (NetworkImageView) findViewById(R.id.imgProduct);
        txtCompany = (TextView) findViewById(R.id.txtCompany);
        txtNewPrice = (TextView) findViewById(R.id.txtNewPrice);
        txtOldPrice = (TextView) findViewById(R.id.txtOldPrice);
        txtPlace = (TextView) findViewById(R.id.txtPlace);
        txtProdName = (TextView) findViewById(R.id.txtProductName);
        txtDesc = (TextView) findViewById(R.id.txtDesc);
        lytLogoMain = (RelativeLayout) findViewById(R.id.lytLogoMain);
        lytLogoFirst = (RelativeLayout) findViewById(R.id.lytLogoFirst);
        recyclerBranch = (RecyclerView) findViewById(R.id.recyclerBranches);
        recyclerMall = (RecyclerView) findViewById(R.id.recyclerMall);
        scrollView = (ScrollView) findViewById(R.id.scrollView);

        scrollView.fullScroll(ScrollView.FOCUS_UP);
        scrollView.smoothScrollTo(0, 0);

        arrBranches = new ArrayList<>();

        position = getIntent().getExtras().getInt("position");
        arrayList = getIntent().getStringArrayListExtra("list");


        /*Set fontType*/
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/fabfeltscripts.TTF");
        txtOldPrice.setTypeface(typeface);
        txtNewPrice.setTypeface(typeface);

        /*Set sliding drawer*/
        new SlidingDrawer(this);

        txtProdName.setText(arrayList.get(0).toUpperCase());
        txtCompany.setText(arrayList.get(1));
        txtPlace.setText(arrayList.get(2));
        txtOldPrice.setText(arrayList.get(3));
        txtNewPrice.setText(arrayList.get(4));
        txtDesc.setText(arrayList.get(7));


        /*load product image*/
        // Loading image with placeholder and error image
        String prodUrl = arrayList.get(6);
        if (prodUrl != null) {
            RequestQueue mRequestQueue = AppController.getInstance().getRequestQueue();
            ImageLoader imageLoader = new ImageLoader(mRequestQueue, new LruBitmapCache());
            ImageLoader.ImageListener listener = new ImageLoader.ImageListener() {
                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                    Log.d("Responceget", response.toString());
                    if (response.getBitmap() != null) {
                        imgProduct.setImageBitmap(response.getBitmap());
                        lytLogoMain.setVisibility(View.VISIBLE);
                        lytLogoFirst.setVisibility(View.GONE);
                        displayLogo(imgLogo);

                    } else {
                        lytLogoFirst.setVisibility(View.VISIBLE);
                        lytLogoMain.setVisibility(View.GONE);
                        displayLogo(imgLogoFirst);
                    }
                }

                @Override
                public void onErrorResponse(VolleyError error) {
                    lytLogoMain.setVisibility(View.GONE);
                    lytLogoFirst.setVisibility(View.VISIBLE);
                    displayLogo(imgLogoFirst);
                }
            };
            imageLoader.get(prodUrl, listener);
            imgProduct.setImageUrl(prodUrl, imageLoader);
        }

        /*Display branches*/
//        for (int i = 0; i < 10; i++)
//            arrBranches.add("Lorem ipsum dolor sit amet,\nconsectetur adipiscing elit.\nInteger quis vestibulum odio,\neget ultrices enim." + i);

        adapterBranches = new ArrayAdapterBranches(this, arrBranches);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerBranch.setLayoutManager(mLayoutManager);
        recyclerBranch.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(1));
        recyclerBranch.setAdapter(adapterBranches);

        /*Display mall*/
        ArrayAdpterProductMall adapterMall = new ArrayAdpterProductMall(this, arrBranches);
        RecyclerView.LayoutManager mLayoutManager2 = new LinearLayoutManager(this);
        recyclerMall.setLayoutManager(mLayoutManager2);
        recyclerMall.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(1));
        recyclerMall.setAdapter(adapterMall);
    }

    public void displayLogo(final CircularNetworkImageView imgLogo) {

        /*Load logo*/
        RequestQueue mRequestQueue = AppController.getInstance().getRequestQueue();

        ImageLoader imageLoader = new ImageLoader(mRequestQueue, new LruBitmapCache());
        // Loading image with placeholder and error image
        ImageLoader.ImageListener listener = new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                Log.d("ResponcegetLogo", response.toString());
                if (response.getBitmap() != null) {
                    imgLogo.setVisibility(View.VISIBLE);
                    imgLogo.setImageBitmap(response.getBitmap());
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {
            }
        };

        imageLoader.get(arrayList.get(5), listener);
        imgLogo.setImageUrl(arrayList.get(5), imageLoader);
    }
}
