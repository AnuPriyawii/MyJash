package com.myjash.app.activity;

import android.app.Activity;
import android.graphics.Typeface;
import android.location.GpsStatus;
import android.os.AsyncTask;
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
import com.myjash.app.AppUtil.CircularNetworkImageView;
import com.myjash.app.AppUtil.InternetService;
import com.myjash.app.AppUtil.LruBitmapCache;
import com.myjash.app.AppUtil.MyGridView;
import com.myjash.app.AppUtil.SlidingDrawer;
import com.myjash.app.R;
import com.myjash.app.adapter.ArrayAdapterBranches;
import com.myjash.app.adapter.ArrayAdpterProductMall;
import com.myjash.app.app.AppController;
import com.myjash.app.fragments.Product;
import com.myjash.app.model.ProductModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PopUpProduct extends AppCompatActivity {
    static Activity activity;
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
    static RecyclerView recyclerBranch;
    static RecyclerView recyclerMall;
    ScrollView scrollView;
    int position;
    ArrayList<String> arrayList;
    static ArrayList<ProductModel> arrBranches;
    static String[] arrMall;
    static ArrayAdapterBranches adapterBranches;
    static int postionClicked;
    static String locationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_up_product);
        activity = this;
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
//        arrMall = new ArrayList<>();

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

        /*load more details*/
        new InternetService(activity).downloadDataByGet("getAllActiveMallLocation", "popup", true);
        postionClicked = Integer.parseInt(arrayList.get(8));


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



        /*Display mall*/
//        ArrayAdpterProductMall adapterMall = new ArrayAdpterProductMall(this, arrBranches);
//        RecyclerView.LayoutManager mLayoutManager2 = new LinearLayoutManager(this);
//        recyclerMall.setLayoutManager(mLayoutManager2);
//        recyclerMall.setItemAnimator(new DefaultItemAnimator());
////        recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(1));
//        recyclerMall.setAdapter(adapterMall);
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


    public static void getLocationData(JSONArray jsonArray) {
        Log.d("MallId", jsonArray + " f");
        ProductModel model = Product.arrProd.get(postionClicked);
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                int mallId = jsonObject.getInt("ml_id");
                if (model.getLocationId() != null)
                    if (model.getLocationId().length() > 0)
                        if (mallId == Integer.parseInt(model.getLocationId())) {
                            locationId = jsonObject.getString("location_id");
                            break;
                        }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        Map<String, String> params = new HashMap<String, String>();
        params.put("category_id", model.getCategoryId());

        params.put("locationId", locationId);
        params.put("product_name", model.getName());
        params.put("brand_id", model.getBrandId());
        params.put("companyId", model.getCompanyId());
        params.put("mallId", model.getMallId());
        params.put("vendor_id", model.getVendorId());
        new InternetService(activity).downloadDataByPOST("fetchallproductdetails", params, "popup");
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
                    arrBranches.add(model);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        adapterBranches = new ArrayAdapterBranches(activity, arrBranches);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
        recyclerBranch.setLayoutManager(mLayoutManager);
        recyclerBranch.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(1));
        recyclerBranch.setAdapter(adapterBranches);

         /*Display mall*/
        if (mallList.length() > 0) {
            arrMall = mallList.split(",");
            ArrayAdpterProductMall adapterMall = new ArrayAdpterProductMall(activity, arrMall);
            RecyclerView.LayoutManager mLayoutManager2 = new LinearLayoutManager(activity);
            recyclerMall.setLayoutManager(mLayoutManager2);
            recyclerMall.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(1));
            recyclerMall.setAdapter(adapterMall);
        }
    }
}
