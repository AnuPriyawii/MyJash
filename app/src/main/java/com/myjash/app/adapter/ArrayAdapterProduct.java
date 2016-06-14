package com.myjash.app.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.myjash.app.AppUtil.InternetService;
import com.myjash.app.AppUtil.LruBitmapCache;
import com.myjash.app.activity.PopUpProduct;
import com.myjash.app.R;

import java.text.DecimalFormat;
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
    public int num = 1;
    HashMap<Integer, View> hashMap;

    public ArrayAdapterProduct(List<ProductModel> arrayProd, Activity activity) {
        this.arrayProd = arrayProd;
        this.activity = activity;
        hashMap = new HashMap<>();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtproduct;
        public TextView txtExprd;
        public TextView txtPlace;
        public TextView txtNewRate;
        public TextView txtOldRate;
        public ImageView imageView;
        public int imgPosition;
        public ImageLoader imageLoader;
        View V;

        public MyViewHolder(View view) {
            super(view);
            if (hashMap.get(getAdapterPosition()) == null) {
                hashMap.put(getAdapterPosition(), view);
            }
            V = view;
            txtproduct = (TextView) view.findViewById(R.id.txtProdName);
            txtExprd = (TextView) view.findViewById(R.id.txtExpDate);
            txtNewRate = (TextView) view.findViewById(R.id.txtNewPrice);
            txtOldRate = (TextView) view.findViewById(R.id.txtOldPrice);
            txtPlace = (TextView) view.findViewById(R.id.txtPlace);
            imageView = (ImageView) view.findViewById(R.id.img);
        }


    }

    @Override
    public int getItemCount() {
       /* if (num * 15 > arrayProd.size()) {
            return arrayProd.size();
        } else {
            return num * 15;
        }*/
        return arrayProd.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d("parent", parent + "f");
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_list_product, parent, false);
        parent.setTag(itemView);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.V.setTag(position);
        final ProductModel model = arrayProd.get(position);
//        if (!model.isLoadedOnce()) {
        holder.txtPlace.setText(model.getPlace());
        DecimalFormat decimalFormat = new DecimalFormat(".00");
        holder.txtOldRate.setText("AED " + decimalFormat.format(Double.parseDouble(model.getOldRate())));
        holder.txtNewRate.setText("AED " + decimalFormat.format(Double.parseDouble(model.getNewRate())));
        holder.txtExprd.setText("Expiry date: " + model.getExprDate());
        holder.txtproduct.setText(model.getName());
        holder.imageView.setTag(R.integer.tag1, model.getUrl());
        holder.imageView.setTag(R.integer.tag2, position);
        if (model.getBitmap() != null) {
            holder.imageView.setImageBitmap(model.getBitmap());
        } /*else {*/
        Glide.with(activity).load(model.getUrl())
                .thumbnail(1.f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.imageView);
//        }
        /*Strike through*/
        holder.txtOldRate.setPaintFlags(holder.txtOldRate.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
/*
        try {
            if (model.getUrl() != null) {
                RequestQueue mRequestQueue = AppController.getInstance().getRequestQueue();

                holder.imageLoader = new ImageLoader(mRequestQueue, new LruBitmapCache());
                holder.imageLoader.get(model.getUrl(), new ImageLoader.ImageListener() {
                    @Override
                    public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
//                        if (holder.imageView.getTag(R.integer.tag1).toString().equals(model.getUrl())) {
                        if (response.getBitmap() != null) {
//                            int pos = Integer.parseInt(holder.imageView.getTag(R.integer.tag2).toString());
//                            arrayProd.get(pos).setBitmap(response.getBitmap());
                            holder.imageView.setImageBitmap(response.getBitmap());
                        } else {
                            holder.imageView.setImageResource(R.drawable.logo_round);
                        }
                       *//* } else {
                            holder.imageView.setImageResource(R.drawable.logo_round);
                        }*//*
                    }

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        holder.imageView.setImageResource(R.drawable.logo_round);
                    }
                });
                holder.imageView.setImageUrl(model.getUrl(), holder.imageLoader);
            } else {
                holder.imageView.setImageBitmap(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        holder.V.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
        Log.d("Bundleget", params.toString());
        new InternetService(activity).downloadDataByPOST("fetchallproductdetails", params, "popup");
    }

    public static void displayProduct(JSONObject response) {
        jsonObject = response;
        Log.d("DataForPopUp", response + " f");
        Intent intent = new Intent(activity, PopUpProduct.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("position", positionClicked);
//                intent.putStringArrayListExtra("list", arrayList);
        activity.startActivity(intent);

    }
}
