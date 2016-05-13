package com.wiinnova.myjash.adapter;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wiinnova.myjash.activity.MainContainer;
import com.wiinnova.myjash.R;

import java.util.List;

import com.wiinnova.myjash.fragments.Category;
import com.wiinnova.myjash.fragments.Product;
import com.wiinnova.myjash.model.ProductModel;

/**
 * Created by ubundu on 6/4/16.
 */
public class ArrayAdapterSubCategory extends RecyclerView.Adapter<ArrayAdapterSubCategory.MyViewHolder> {
    private List<ProductModel> array;
    Activity activity;
    View Myview;
    String type;

    public ArrayAdapterSubCategory(List<ProductModel> array, Activity activity) {
        this.array = array;
        this.activity = activity;
        this.type = type;
        Log.d("jsonArray", array.size() + " d");
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView txtproduct;
        ImageView img;


        public MyViewHolder(View view) {
            super(view);
            Myview = view;
            view.setOnClickListener(this);
            txtproduct = (TextView) view.findViewById(R.id.txtProdName);
            img = (ImageView) view.findViewById(R.id.imgViewMore);
        }

        @Override
        public void onClick(View v) {
            int id = Integer.parseInt(v.getTag().toString());
//            fragment.recyclerClickListener(id);
//            if (type.equals("category")) {
            Category.categoryId = "";
            Category.categoryId = id + "";
            MainContainer.fragmentForBackPress = new Category();
//            } else if (type.equals("location")) {
//
//                Location.locationId = "";
//                Location.locationId = id + "";
//                MainContainer.fragmentForBackPress = new Location();
//            }
            Bundle bundle = new Bundle();
            bundle.putInt("id", id);
            bundle.putString("type", "category");
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
                .inflate(R.layout.layout_list_category, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ProductModel model = array.get(position);
        Myview.setTag(model.getId());
        holder.txtproduct.setText(model.getName().trim());
        holder.img.setVisibility(View.GONE);

    }
}
