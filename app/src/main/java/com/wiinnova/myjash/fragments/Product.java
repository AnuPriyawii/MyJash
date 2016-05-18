package com.wiinnova.myjash.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.wiinnova.myjash.AppUtil.HeaderAction;
import com.wiinnova.myjash.AppUtil.InternetService;
import com.wiinnova.myjash.AppUtil.Util;
import com.wiinnova.myjash.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wiinnova.myjash.adapter.ArrayAdapterProduct;
import com.wiinnova.myjash.model.ProductModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Product extends Fragment {
    static TextView txtTitle;
    public static List<ProductModel> arrProd;
    private static RecyclerView recyclerView;
    private static ArrayAdapterProduct adapterProduct;
    static Activity activity;
    static int id;
    static String type = "";
//    public static int clickedPosition;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_product, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        txtTitle = (TextView) view.findViewById(R.id.txttitle);
        arrProd = new ArrayList<>();
//        swipeRefreshLayout.setColorSchemeColors(
//                R.color.refresh_progress_1,
//                R.color.refresh_progress_2,
//                R.color.refresh_progress_3);
        activity = getActivity();

        /*Set header*/
        new HeaderAction(view, getActivity());
        ///////////////////display product based on category,brand or company.
        if (getArguments() != null) {
            id = getArguments().getInt("id");
            type = getArguments().getString("type");
        } else {
            type = "";
        }
        if (Util.haveNetworkConnection(getActivity())) {
            ////////////////////Download data
            if (type.equals("search")) {
                search();
            } else if (type.equals("category")) {
                clearSearchVariables();
                Log.d("CategoryId", Category.categoryId + "dfg");
                Search.categoryId = Category.categoryId;
                search();
            } else if (type.equals("brand")) {
                clearSearchVariables();
                Search.brandId = Brand.brandId;
                search();
            } else if (type.equals("company")) {
                clearSearchVariables();
                Search.companyId = Companies.companyId;
                search();
            } else if (type.equals("mall")) {
                clearSearchVariables();
                Search.mallId = MallFragment.mallId;
                search();
            } else if (type.equals("location")) {
                clearSearchVariables();
                Search.locatioId = Location.locationId;
                search();
            } else
                new InternetService(getActivity()).loadDataFromCache("getAllProductDetails", "product");
        } else {
            Toast.makeText(getActivity(), "No internet connection", Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    public void clearSearchVariables() {
        Search.locatioId = "";
        Search.categoryId = "";
        Search.companyId = "";
        Search.brandId = "";
        Search.mallId = "";
    }

    public Map<String, String> search() {

        Map<String, String> params = new HashMap<String, String>();
        params.put("locationId", Search.locatioId);
        params.put("productName", Search.productName);
        Log.d("Bundleget", params + " null");
        if (Search.categoryId.length() == 0 && Search.companyId.length() == 0 && Search.brandId.length() == 0 && Search.mallId.length() == 0)
            new InternetService(activity).downloadDataByPOST("searchQuery", params, "search");
        else {
            params.put("categoryId", Search.categoryId);
            params.put("brandId", Search.brandId);
            params.put("companyId", Search.companyId);
            params.put("mallId", Search.mallId);
            Log.d("Bundleget", params + " null");
            new InternetService(activity).downloadDataByPOST("advancedSearch", params, "search");
        }

        return params;
    }

    public static void refreshAdapter(JSONObject json) {
        Log.d("TypeGet", arrProd.size() + " f");
        boolean isArrayEmpty = true;
        if (arrProd.size() > 0)
            isArrayEmpty = false;

        try {
            if (json.getString("response").contains("Success")) {
                arrProd.clear();
                JSONArray jsonArray = new JSONArray(json.getString("result"));
                for (int i = jsonArray.length() - 1; i >= 0; i--) {
                    try {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        ProductModel model = new ProductModel();
                        model.setBrand(jsonObject.getString("brand_name"));
                        model.setName(jsonObject.getString("product_name"));
                        model.setNewRate(jsonObject.getString("offer_price"));
                        model.setOldRate(jsonObject.getString("actual_price"));
                        model.setPlace(jsonObject.getString("v_name"));
                        model.setUrl(InternetService.IMG_BASE_URL + "vendor/" + jsonObject.getString("v_logo"));
                        model.setProdUrl("http://www.myjash.freestech.in/products/original/" + jsonObject.getString("product_image"));
                        model.setExprDate(jsonObject.getString("ofr_valid_to"));
                        model.setDescription(jsonObject.getString("description"));
                        arrProd.add(model);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Log.d("TypeGet", arrProd.size() + " f");
                if (arrProd.size() > 0) {
                    if (isArrayEmpty) {
                        adapterProduct = new ArrayAdapterProduct(arrProd, activity);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
                        recyclerView.setLayoutManager(mLayoutManager);
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
//            recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(1));
                        recyclerView.setAdapter(adapterProduct);
                    } else {
                        adapterProduct.notifyDataSetChanged();
                    }
                }
            } else {
                txtTitle.setVisibility(View.VISIBLE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
