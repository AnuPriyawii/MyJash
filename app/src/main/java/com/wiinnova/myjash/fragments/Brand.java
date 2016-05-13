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
import android.widget.Toast;

import com.wiinnova.myjash.AppUtil.HeaderAction;
import com.wiinnova.myjash.AppUtil.InternetService;
import com.wiinnova.myjash.AppUtil.Util;
import com.wiinnova.myjash.R;

import java.util.ArrayList;
import java.util.List;

import com.wiinnova.myjash.AppUtil.VerticalSpaceItemDecoration;
import com.wiinnova.myjash.adapter.ArrayAdapterBrand;
import com.wiinnova.myjash.adapter.ArrayAdapterProduct;
import com.wiinnova.myjash.adapter.ArrayAdpterCategory;
import com.wiinnova.myjash.model.ProductModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Brand extends Fragment {
    private static List<ProductModel> arrProd = new ArrayList<>();
    private static RecyclerView recyclerView;
    private static ArrayAdapterBrand adapterProduct;
    static Activity activity;
    public static String brandId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_brand, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        activity = getActivity();

        /*Set header*/
        new HeaderAction(view, getActivity());

        /////////////////Download data

        if (Util.haveNetworkConnection(getActivity())) {
            new InternetService(getActivity()).loadDataFromCache("getAllActiveBrands", "brand");
        } else {
            Toast.makeText(getActivity(), "No internet connection", Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    public static void refreshAdapter(JSONArray jsonArray) {
        arrProd = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                ProductModel model = new ProductModel();
                model.setName(jsonObject.getString("brand_name"));
                model.setUrl(InternetService.IMG_BASE_URL + "brand/" + jsonObject.getString("brand_image"));
                model.setId(jsonObject.getInt("id"));
                arrProd.add(model);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        adapterProduct = new ArrayAdapterBrand(arrProd, activity);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(1));
        recyclerView.setAdapter(adapterProduct);
    }


}
