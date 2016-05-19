package com.myjash.app.adapter;

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
import com.myjash.app.AppUtil.LruBitmapCache;
import com.myjash.app.activity.MainContainer;
import com.myjash.app.R;

import java.util.List;

import com.myjash.app.app.AppController;
import com.myjash.app.fragments.Brand;
import com.myjash.app.fragments.Location;
import com.myjash.app.fragments.Product;
import com.myjash.app.model.ProductModel;

/**
 * Created by ubundu on 6/4/16.
 */
public class ArrayAdapterLocation extends RecyclerView.Adapter<ArrayAdapterLocation.MyViewHolder> {

    private List<ProductModel> array;
    View MyView;
    Activity activity;


    public ArrayAdapterLocation(List<ProductModel> array, Activity activity) {
        this.array = array;
        this.activity = activity;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView txtproduct;

        public MyViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            MyView = view;
            txtproduct = (TextView) view.findViewById(R.id.txtProdName);
        }

        @Override
        public void onClick(View v) {
            Location.locationId = "";
            Location.locationId = v.getTag().toString();
            MainContainer.fragmentForBackPress = new Location();
            Bundle bundle = new Bundle();
            bundle.putInt("id", Integer.parseInt(v.getTag().toString()));
            bundle.putString("type", "location");
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
                .inflate(R.layout.layout_list_location, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ProductModel model = array.get(position);
        holder.txtproduct.setText(model.getName());
        MyView.setTag(model.getId());

    }
}
