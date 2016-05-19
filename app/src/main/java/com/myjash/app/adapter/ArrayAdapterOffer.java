package com.myjash.app.adapter;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.myjash.app.AppUtil.InternetService;
import com.myjash.app.AppUtil.LruBitmapCache;
import com.myjash.app.activity.MainContainer;
import com.myjash.app.R;

import java.util.List;

import com.myjash.app.app.AppController;
import com.myjash.app.fragments.Flip;
import com.myjash.app.fragments.OfferFragment;
import com.myjash.app.model.ProductModel;

import static com.myjash.app.activity.MainContainer.fragmentManager;

/**
 * Created by ubundu on 6/4/16.
 */
public class ArrayAdapterOffer extends BaseAdapter {
    private List<ProductModel> array;
    Activity activity;
    View V;


    public ArrayAdapterOffer(List<ProductModel> array, Activity activity) {
        this.array = array;
        activity = activity;
    }

    public class MyViewHolder implements View.OnClickListener {
        public TextView txtproduct;
        public NetworkImageView img;

        public MyViewHolder(View view) {
            V = view;
            view.setOnClickListener(this);
            txtproduct = (TextView) view.findViewById(R.id.txtProdName);
            img = (NetworkImageView) view.findViewById(R.id.img);
        }

        @Override
        public void onClick(View v) {
            Log.d("Clicked", "Clicked");

            MainContainer.fragmentForBackPress = new OfferFragment();
            Flip fragment = new Flip();
            Intent intent = new Intent();
            intent.putExtra("url", v.getTag().toString());
            fragment.setArguments(intent.getExtras());
            fragmentManager.beginTransaction()
                    .replace(R.id.lytMainContainer, fragment).addToBackStack(null)
                    .commit();
        }
    }

    @Override
    public int getCount() {
        return array.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_list_offer, parent, false);
        MyViewHolder holder = new MyViewHolder(itemView);
        ProductModel model = array.get(position);
        holder.txtproduct.setText(model.getName());
        V.setTag(model.getUrl());

        /*Set logo*/
        String url = model.getLogo();
        if (url != null) {
            RequestQueue mRequestQueue = AppController.getInstance().getRequestQueue();

            ImageLoader imageLoader = new ImageLoader(mRequestQueue, new LruBitmapCache());

            /* Loading image with placeholder and error image*/
            imageLoader.get(InternetService.IMG_BASE_URL + "vendor/" + url, ImageLoader.getImageListener(holder.img, R.drawable.logo_round, R.drawable.logo));


            holder.img.setImageUrl(InternetService.IMG_BASE_URL + "vendor/" + url, imageLoader);

        }
        return itemView;
    }
}
    /* @Override
    public int getItemCount() {
        return array.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_list_offer, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        ProductModel model = array.get(position);
        holder.txtproduct.setText(model.getName());
        V.setTag(model.getUrl());

        *//*Set logo*//*
        String url = model.getLogo();
        if (url != null) {
            RequestQueue mRequestQueue = AppController.getInstance().getRequestQueue();

            ImageLoader imageLoader = new ImageLoader(mRequestQueue, new LruBitmapCache());

        *//* Loading image with placeholder and error image*//*
            imageLoader.get(InternetService.IMG_BASE_URL + "vendor/" + url, ImageLoader.getImageListener(holder.img, R.drawable.logo_round, R.drawable.logo));


            holder.img.setImageUrl(InternetService.IMG_BASE_URL + "vendor/" + url, imageLoader);
        }

    }*/
