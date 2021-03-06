package com.myjash.app.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import com.myjash.app.AppUtil.HeaderAction;
import com.myjash.app.AppUtil.InternetService;
import com.myjash.app.AppUtil.Util;
import com.myjash.app.AppUtil.VerticalSpaceItemDecoration;
import com.myjash.app.R;
import com.myjash.app.adapter.ArrayAdapterOffer;
import com.myjash.app.adapter.ArrayAdapterPdf;
import com.myjash.app.adapter.ArrayAdapterPdf;
import com.myjash.app.model.ProductModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Pdf extends Fragment {
    private static List<ProductModel> arrProd = new ArrayList<>();
    private static GridView gridView;
    private static ArrayAdapterPdf adapterProduct;
    static Activity activity;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_pdf, container, false);
        gridView = (GridView) view.findViewById(R.id.recycler_view);
        activity = getActivity();

        /*Set header*/
        new HeaderAction(view, getActivity());
        if (Util.haveNetworkConnection(getActivity())) {
            /////////////////Download data
            new InternetService(getActivity()).loadDataFromCache("getPdf", "pdf");
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
                model.setUrl(InternetService.FLIP_IMG_URL + jsonObject.getString("flyer_file_name"));
                model.setLogo(InternetService.IMG_BASE_URL+"vendor/"+jsonObject.getString("logo"));
                model.setId(jsonObject.getInt("fl_id"));
                arrProd.add(model);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        adapterProduct = new ArrayAdapterPdf(arrProd, activity);
//        gridView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
//        gridView.setLayoutManager(mLayoutManager);
//        gridView.setItemAnimator(new DefaultItemAnimator());
        gridView.setAdapter(adapterProduct);
    }
}
