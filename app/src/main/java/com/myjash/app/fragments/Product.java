package com.myjash.app.fragments;

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
import android.view.ViewTreeObserver;
import android.widget.TextView;
import android.widget.Toast;

import com.myjash.app.AppUtil.HeaderAction;
import com.myjash.app.AppUtil.InternetService;
import com.myjash.app.AppUtil.Util;
import com.myjash.app.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.myjash.app.adapter.ArrayAdapterProduct;
import com.myjash.app.model.ProductModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Product extends Fragment {
    TextView txtTitle;
    public static List<ProductModel> arrProd;
    private RecyclerView recyclerView;
    private ArrayAdapterProduct adapterProduct;
    Activity activity;
    int id;
    String type = "";
//    public  int clickedPosition;


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
                Search.mallId = Mall.mallId;
                search();
            } else if (type.equals("location")) {
                clearSearchVariables();
                Search.locatioId = Location.locationId;
                search();
            } else {
                Log.d("Normal", "dgdfgd");
                new InternetService(getActivity()).downloadDataByGet("getAllProductDetails", "product", false);
            }
        } else {
            Toast.makeText(getActivity(), "No internet connection", Toast.LENGTH_SHORT).show();
        }


        return view;
    }

    public void clearSearchVariables() {
        Search.locatioId = null;
        Search.categoryId = null;
        Search.companyId = null;
        Search.brandId = null;
        Search.mallId = null;
    }

    public Map<String, String> search() {

        Map<String, String> params = new HashMap<String, String>();
        params.put("locationId", Search.locatioId);
        params.put("productName", Search.productName);
        Log.d("Bundleget", params + " null");
        Log.d("CompanyId", Search.companyId + " d");
        if (Search.categoryId == null && Search.companyId == null && Search.brandId == null && Search.mallId == null)
            new InternetService(activity).downloadDataByPOST("searchQuery", params, "search");
        else {
            params.put("categoryId", Search.categoryId);
            params.put("brandId", Search.brandId);
            params.put("vendorId", Search.companyId);
            params.put("mallId", Search.mallId);
            Log.d("Bundleget", params + " null");
            new InternetService(activity).downloadDataByPOST("advancedSearch", params, "search");
        }

        return params;
    }

    public void refreshAdapter(JSONObject json) {
        boolean isArrayEmpty = true;
        if (arrProd.size() > 0)
            isArrayEmpty = false;

        try {
            if (json.getString("response").contains("Success")) {
                arrProd.clear();
                JSONArray jsonArray = new JSONArray(json.getString("result"));
                Log.d("Normal", jsonArray.length() + " cgh");
                for (int i = 0; i < jsonArray.length(); i++) {
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
                        model.setCategoryId(jsonObject.getString("category_id"));
                        model.setLocationId(jsonObject.getString("mall_id"));
                        model.setCompanyId(jsonObject.getString("company_id"));
                        model.setMallId(jsonObject.getString("mall_id"));
                        model.setBrandId(jsonObject.getString("brand_id"));
                        model.setVendorId(jsonObject.getString("vendor_id"));
                        model.setStartDate(jsonObject.getString("ofr_valid_from"));
                        arrProd.add(model);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Log.d("TypeGet", arrProd.size() + " f");
                if (arrProd.size() > 0) {
                    if (isArrayEmpty) {
                        adapterProduct = new ArrayAdapterProduct(arrProd, activity);
                        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(activity);
                        recyclerView.setLayoutManager(mLayoutManager);
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        recyclerView.setAdapter(adapterProduct);

                          /*Scroll listenr*/
                        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                            @Override
                            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                                super.onScrolled(recyclerView, dx, dy);
                                Log.d("lastPos", mLayoutManager.findLastVisibleItemPosition() + "f");
                                if (mLayoutManager.findLastCompletelyVisibleItemPosition() == adapterProduct.num * 15 - 1) {
                                    if ((adapterProduct.num) * 15 < arrProd.size())
                                        adapterProduct.num = adapterProduct.num + 1;
                                }
                            }

                            @Override
                            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                                super.onScrollStateChanged(recyclerView, newState);


                            }
                        });
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
