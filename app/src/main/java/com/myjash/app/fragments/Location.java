package com.myjash.app.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.myjash.app.AppUtil.HeaderAction;
import com.myjash.app.AppUtil.InternetService;
import com.myjash.app.AppUtil.Util;
import com.myjash.app.R;

import java.util.ArrayList;

import com.myjash.app.adapter.ArrayAdapterBrand;
import com.myjash.app.adapter.ArrayAdapterLocation;
import com.myjash.app.adapter.ArrayAdpterCategory;
import com.myjash.app.model.ProductModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Location extends Fragment {
    private static ArrayList<ProductModel> arrProd = new ArrayList<>();
    private static RecyclerView recyclerView;
    private static ArrayAdapterLocation adapterProduct;
    static Activity activity;
    static Location fragment;
    static SwipeRefreshLayout swipeRefreshLayout;
    public static String locationId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_location, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        activity = getActivity();
        fragment = this;

        /*Set header*/
        new HeaderAction(view, getActivity());
        /////////////////Download data
        if (Util.haveNetworkConnection(getActivity())) {
            new InternetService(getActivity()).loadDataFromCache("getAllLocations", "location");
        } else {
            Toast.makeText(getActivity(), "No internet connection", Toast.LENGTH_SHORT).show();
        }
        //////////////////////////Refresh product
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new InternetService(getActivity()).downloadDataByGet("getAllLocations", "location", true);

            }
        });
        return view;
    }

    public static void refreshAdapter(JSONArray jsonArray) {
        swipeRefreshLayout.setRefreshing(false);
        arrProd = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                ProductModel model = new ProductModel();
                model.setName(jsonObject.getString("l_name"));
                model.setId(jsonObject.getInt("l_id"));
                arrProd.add(model);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        adapterProduct = new ArrayAdapterLocation(arrProd,activity);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(1));
        recyclerView.setAdapter(adapterProduct);
    }

    public void recyclerClickListener(int id) {

    }
}
