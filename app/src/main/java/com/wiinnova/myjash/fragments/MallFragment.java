package com.wiinnova.myjash.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.wiinnova.myjash.AppUtil.HeaderAction;
import com.wiinnova.myjash.AppUtil.InternetService;
import com.wiinnova.myjash.AppUtil.Util;
import com.wiinnova.myjash.R;
import com.wiinnova.myjash.adapter.ArrayAdapterMall;
import com.wiinnova.myjash.adapter.ArrayAdapterProduct;
import com.wiinnova.myjash.model.ProductModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MallFragment extends Fragment {
    static TextView txtTitle;
    private static List<ProductModel> arrProd = new ArrayList<>();
    private static RecyclerView recyclerView;
    private static ArrayAdapterMall adapterProduct;
    static Activity activity;
    public static String mallId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mall, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        txtTitle = (TextView) view.findViewById(R.id.txttitle);
        activity = getActivity();


        /*Set header*/
        new HeaderAction(view, getActivity());
        ////////////////////Download data
        if (Util.haveNetworkConnection(getActivity())) {
            new InternetService(getActivity()).loadDataFromCache("getAllActiveMallLocation", "mall");
        } else {
            Toast.makeText(getActivity(), "No internet connection", Toast.LENGTH_SHORT).show();
        }
        return view;
    }

    public static void refreshAdapter(JSONArray jsonArray) {
        Log.d("TypeGet", jsonArray.length() + " f");
        arrProd = new ArrayList<ProductModel>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                ProductModel model = new ProductModel();
                model.setName(jsonObject.getString("ml_name"));
                model.setPlace(jsonObject.getString("l_name"));
                model.setId(jsonObject.getInt("ml_id"));
//                model.setUrl(InternetService.IMG_BASE_URL + "product/" + jsonObject.getString("product_image"));

                arrProd.add(model);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (arrProd.size() > 0) {
            adapterProduct = new ArrayAdapterMall(arrProd, activity);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
//            recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(1));
            recyclerView.setAdapter(adapterProduct);
        } else {
            recyclerView.setVisibility(View.GONE);
            txtTitle.setVisibility(View.VISIBLE);
        }
    }

}
