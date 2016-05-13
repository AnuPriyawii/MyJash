package com.wiinnova.myjash.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import com.wiinnova.myjash.AppUtil.HeaderAction;
import com.wiinnova.myjash.AppUtil.InternetService;
import com.wiinnova.myjash.AppUtil.Util;
import com.wiinnova.myjash.AppUtil.VerticalSpaceItemDecoration;
import com.wiinnova.myjash.R;
import com.wiinnova.myjash.adapter.ArrayAdapterBrand;
import com.wiinnova.myjash.adapter.ArrayAdapterOffer;
import com.wiinnova.myjash.adapter.ArrayAdpterCategory;
import com.wiinnova.myjash.model.ProductModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class OfferFragment extends Fragment {
    private static List<ProductModel> arrProd = new ArrayList<>();
    private static GridView recyclerView;
    private static ArrayAdapterOffer adapterProduct;
    static Activity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_offer, container, false);
        recyclerView = (GridView) view.findViewById(R.id.recycler_view);
        activity = getActivity();

        /*Set header*/
        new HeaderAction(view, getActivity());
        /////////////////Download data
        if (Util.haveNetworkConnection(getActivity())) {
            new InternetService(getActivity()).loadDataFromCache("getFlip", "offer");
        } else {
            Toast.makeText(getActivity(), "No internet connection", Toast.LENGTH_SHORT).show();
        }
        return view;
    }

    public static void refreshAdapter(JSONArray jsonArray) {
        arrProd = new ArrayList<ProductModel>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                ProductModel model = new ProductModel();
                model.setName(jsonObject.getString("title"));
                model.setUrl(jsonObject.getString("flyer_file_name"));
                model.setLogo(jsonObject.getString("logo"));
                model.setId(jsonObject.getInt("fl_id"));
                arrProd.add(model);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        adapterProduct = new ArrayAdapterOffer(arrProd, activity);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
//        recyclerView.setLayoutManager(mLayoutManager);
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapterProduct);
    }

}
