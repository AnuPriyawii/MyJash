package com.wiinnova.myjash.adapter;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.wiinnova.myjash.AppUtil.LruBitmapCache;
import com.wiinnova.myjash.activity.MainContainer;
import com.wiinnova.myjash.R;

import java.util.List;

import com.wiinnova.myjash.app.AppController;
import com.wiinnova.myjash.fragments.Brand;
import com.wiinnova.myjash.fragments.Product;
import com.wiinnova.myjash.model.ProductModel;

/**
 * Created by ubundu on 6/4/16.
 */
public class ArrayAdapterBrand extends RecyclerView.Adapter<ArrayAdapterBrand.MyViewHolder> {

    private List<ProductModel> array;
    View MyView;
    Activity activity;


    public ArrayAdapterBrand(List<ProductModel> array, Activity activity) {
        this.array = array;
        this.activity = activity;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView txtproduct;
        NetworkImageView imageView;

        public MyViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            MyView = view;
            txtproduct = (TextView) view.findViewById(R.id.txtProdName);
            imageView = (NetworkImageView) view.findViewById(R.id.img);
        }

        @Override
        public void onClick(View v) {
            Brand.brandId = "";
            Brand.brandId = v.getTag().toString();
            MainContainer.fragmentForBackPress = new Brand();
            Bundle bundle = new Bundle();
            bundle.putInt("id", Integer.parseInt(v.getTag().toString()));
            bundle.putString("type", "brand");
            Product fragobj = new Product();
            fragobj.setArguments(bundle);
            activity.getFragmentManager().beginTransaction()
                    .replace(R.id.lytMainContainer, fragobj).addToBackStack(null)
                    .commit();
        }
    }

    @Override
    public int getItemCount() {
        return array.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_list_brand, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ProductModel model = array.get(position);
        holder.txtproduct.setText(model.getName());
        MyView.setTag(model.getId());
        try {
            if (model.getUrl() != null) {
                RequestQueue mRequestQueue = AppController.getInstance().getRequestQueue();

                ImageLoader imageLoader = new ImageLoader(mRequestQueue, new LruBitmapCache());
                // Loading image with placeholder and error image
                imageLoader.get(model.getUrl(), ImageLoader.getImageListener(
                        holder.imageView, R.drawable.logo_round, R.drawable.logo));
                holder.imageView.setImageUrl(model.getUrl(), imageLoader);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
