package com.wiinnova.myjash.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.wiinnova.myjash.AppUtil.LruBitmapCache;
import com.wiinnova.myjash.activity.PopUpProduct;
import com.wiinnova.myjash.R;

import java.util.ArrayList;
import java.util.List;

import com.wiinnova.myjash.app.AppController;
import com.wiinnova.myjash.model.ProductModel;

/**
 * Created by ubundu on 6/4/16.
 */
public class ArrayAdapterProduct extends RecyclerView.Adapter<ArrayAdapterProduct.MyViewHolder> {
    private List<ProductModel> arrayProd;
    Activity activity;

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

                ArrayList<String> arrayList = new ArrayList<>();
                arrayList.add(arrayProd.get(position).getName());
                arrayList.add(arrayProd.get(position).getBrand());
                arrayList.add(arrayProd.get(position).getPlace());
                arrayList.add(arrayProd.get(position).getOldRate());
                arrayList.add(arrayProd.get(position).getNewRate());
                arrayList.add(arrayProd.get(position).getUrl());
                arrayList.add(arrayProd.get(position).getProdUrl());
                arrayList.add(arrayProd.get(position).getDescription());
                Intent intent = new Intent(activity, PopUpProduct.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("position", Integer.parseInt(v.getTag().toString()));
                intent.putStringArrayListExtra("list", arrayList);
                activity.startActivity(intent);
            }
        });
    }
}
