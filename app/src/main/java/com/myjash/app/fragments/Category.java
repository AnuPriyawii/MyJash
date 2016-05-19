package com.myjash.app.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.os.Bundle;
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
import com.myjash.app.activity.MainContainer;
import com.myjash.app.R;

import java.util.ArrayList;
import java.util.List;

import com.myjash.app.adapter.ArrayAdpterCategory;
import com.myjash.app.model.ProductModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Category extends Fragment {
    private static ArrayList<ProductModel> arrProd = new ArrayList<>();
    private static List<ArrayList> arrSub;
    private static RecyclerView recyclerView;
    private static ArrayAdpterCategory adapterProduct;
    static Activity activity;
    static Category fragment;
    public static String categoryId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_category, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        activity = getActivity();
        fragment = this;

        /*Set header*/
        new HeaderAction(view, getActivity());
        /////////////////Download data
        if (Util.haveNetworkConnection(getActivity())) {
            new InternetService(getActivity()).loadDataFromCache("getAllActiveCategories", "category");
        } else {
            Toast.makeText(getActivity(), "No internet connection", Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    public static void refreshAdapter(JSONArray jsonArray) {
        arrProd = new ArrayList<>();
        arrSub = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                ArrayList<ProductModel> arrSubprod=new ArrayList<>();
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                ProductModel model = new ProductModel();
                model.setName(jsonObject.getString("category_name"));
                model.setId(jsonObject.getInt("category_id"));
                arrProd.add(model);

                /*Sub category*/
                if (jsonObject.getString("subCategory") != null) {
                    arrSubprod = new ArrayList<>();
                    JSONArray jsonSub = new JSONArray(jsonObject.getString("subCategory"));
                    for (int j = 0; j < jsonSub.length(); j++) {
                        JSONObject jsub = jsonSub.getJSONObject(j);
                        model = new ProductModel();
                        model.setName(jsub.getString("category_name"));
                        model.setId(jsub.getInt("category_id"));
                        arrSubprod.add(model);
                    }
                }
                arrSub.add(arrSubprod);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        adapterProduct = new ArrayAdpterCategory(arrProd, activity, "category", arrSub);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(1));
        recyclerView.setAdapter(adapterProduct);
    }

    public void recyclerClickListener(int id) {
        categoryId = "";
        categoryId = id + "";
        MainContainer.fragmentForBackPress = new Category();
        Bundle bundle = new Bundle();
        bundle.putInt("id", id);
        bundle.putString("type", "category");
        Product fragobj = new Product();
        fragobj.setArguments(bundle);
        getFragmentManager().beginTransaction()
                .replace(R.id.lytMainContainer, fragobj).addToBackStack(null)
                .commit();
    }
}
