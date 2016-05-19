package com.myjash.app.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.myjash.app.AppUtil.InternetService;
import com.myjash.app.AppUtil.LruBitmapCache;
import com.myjash.app.activity.PopUpProduct;
import com.myjash.app.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.myjash.app.app.AppController;
import com.myjash.app.fragments.Product;
import com.myjash.app.model.ProductModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ubundu on 6/4/16.
 */
public class ArrayAdapterProduct extends RecyclerView.Adapter<ArrayAdapterProduct.MyViewHolder> {
    private List<ProductModel> arrayProd;
    static Activity activity;
    public static ProductModel modelClicked;
    public static int positionClicked;
    public static JSONObject jsonObject;

    public ArrayAdapterProduct(List<ProductModel> arrayProd, Activity activity) {
        this.arrayProd = arrayProd;
        this.activity = activity;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtproduct;
        public TextView txtExprd;
        public TextView txtPlace;
        public TextView txtNewRate;
        public TextView txtOldRate;
        public NetworkImageView imageView;
        View V;

        public MyViewHolder(View view) {
            super(view);
            V = view;
            txtproduct = (TextView) view.findViewById(R.id.txtProdName);
            txtExprd = (TextView) view.findViewById(R.id.txtExpDate);
            txtNewRate = (TextView) view.findViewById(R.id.txtNewPrice);
            txtOldRate = (TextView) view.findViewById(R.id.txtOldPrice);
            txtPlace = (TextView) view.findViewById(R.id.txtPlace);
            imageView = (NetworkImageView) view.findViewById(R.id.img);
        }


    }

    @Override
    public int getItemCount() {
        return arrayProd.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_list_product, parent, false);
        parent.setTag(itemView);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.V.setTag(position);
        ProductModel model = arrayProd.get(position);
        holder.txtPlace.setText(model.getPlace());
        holder.txtOldRate.setText("AED " + model.getOldRate());
        holder.txtNewRate.setText("AED " + model.getNewRate());
        holder.txtExprd.setText("Expiry date: " + model.getExprDate());
        holder.txtproduct.setText(model.getName());

        /*Strike through*/
        holder.txtOldRate.setPaintFlags(holder.txtOldRate.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        try {
            if (model.getUrl() != null) {
                RequestQueue mRequestQueue = AppController.getInstance().getRequestQueue();

                ImageLoader imageLoader = new ImageLoader(mRequestQueue, new LruBitmapCache());
                imageLoader.get(model.getUrl(), ImageLoader.getImageListener(
                        holder.imageView, R.drawable.logo_round, R.drawable.logo));

                holder.imageView.setImageUrl(model.getUrl(), imageLoader);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.V.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                ArrayList<String> arrayList = new ArrayList<>();
//                arrayList.add(arrayProd.get(position).getName());
//                arrayList.add(arrayProd.get(position).getBrand());
//                arrayList.add(arrayProd.get(position).getPlace());
//                arrayList.add(arrayProd.get(position).getOldRate());
//                arrayList.add(arrayProd.get(position).getNewRate());
//                arrayList.add(arrayProd.get(position).getUrl());
//                arrayList.add(arrayProd.get(position).getProdUrl());
//                arrayList.add(arrayProd.get(position).getDescription());
//                arrayList.add(position + "");

                 /*load more details includes branches and mall*/
                positionClicked = Integer.parseInt(v.getTag().toString());
                modelClicked = Product.arrProd.get(positionClicked);
                new InternetService(activity).downloadDataByGet("getAllActiveMallLocation", "popup", false);


            }
        });
    }

    public static void getLocationData(JSONArray jsonArray) {
        String locationId = "";
        Log.d("MallId", jsonArray + " f");
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                int mallId = jsonObject.getInt("ml_id");
                if (modelClicked.getLocationId() != null)
                    if (modelClicked.getLocationId().length() > 0)
                        if (mallId == Integer.parseInt(modelClicked.getLocationId())) {
                            locationId = jsonObject.getString("location_id");
                            break;
                        }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        Map<String, String> params = new HashMap<String, String>();
        params.put("category_id", modelClicked.getCategoryId());

        params.put("locationId", locationId);
        params.put("product_name", modelClicked.getName());
        params.put("brand_id", modelClicked.getBrandId());
        params.put("companyId", modelClicked.getCompanyId());
        params.put("mallId", modelClicked.getMallId());
        params.put("vendor_id", modelClicked.getVendorId());
        new InternetService(activity).downloadDataByPOST("fetchallproductdetails", params, "popup");
    }

    public static void displayProduct(JSONObject response) {
        jsonObject = response;
        Intent intent = new Intent(activity, PopUpProduct.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("position", positionClicked);
//                intent.putStringArrayListExtra("list", arrayList);
        activity.startActivity(intent);

    }
}
