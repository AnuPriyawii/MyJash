package com.wiinnova.myjash.adapter;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.wiinnova.myjash.activity.MainContainer;
import com.wiinnova.myjash.R;

import java.util.ArrayList;
import java.util.List;

import com.wiinnova.myjash.fragments.Category;
import com.wiinnova.myjash.fragments.Location;
import com.wiinnova.myjash.fragments.Product;
import com.wiinnova.myjash.model.ProductModel;

/**
 * Created by ubundu on 6/4/16.
 */
public class ArrayAdpterCategory extends RecyclerView.Adapter<ArrayAdpterCategory.MyViewHolder> {
    private List<ProductModel> array;
    Activity activity;
    View Myview;
    String type;
    ArrayList<Integer> arrRecycler;

    public ArrayAdpterCategory(List<ProductModel> array, Activity activity, String type) {
        this.array = array;
        this.activity = activity;
        this.type = type;
        arrRecycler = new ArrayList<>();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView txtproduct;
        NetworkImageView imageView;
        ImageView img;

        RecyclerView recyclerView;
        ArrayAdpterCategory adapterProduct;

        public MyViewHolder(View view) {
            super(view);
            Myview = view;
            view.setOnClickListener(this);
            txtproduct = (TextView) view.findViewById(R.id.txtProdName);
            imageView = (NetworkImageView) view.findViewById(R.id.img);
            recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
            img = (ImageView) view.findViewById(R.id.imgViewMore);
        }

        @Override
        public void onClick(View v) {
            int id = Integer.parseInt(v.getTag().toString());
//            fragment.recyclerClickListener(id);
            if (type.equals("category")) {
                Category.categoryId = "";
                Category.categoryId = id + "";
                MainContainer.fragmentForBackPress = new Category();
            } else if (type.equals("location")) {

                Location.locationId = "";
                Location.locationId = id + "";
                MainContainer.fragmentForBackPress = new Location();
            }
            Bundle bundle = new Bundle();
            bundle.putInt("id", id);
            bundle.putString("type", type);
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
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        ProductModel model = array.get(position);
        Myview.setTag(model.getId());
        holder.txtproduct.setText(model.getName().trim());

        /*Sample data*/
        ArrayList<ProductModel> arrProd = new ArrayList<>();
        ProductModel model1 = new ProductModel();
        model1.setName(model.getName().trim() + 1);
        model1.setId(model.getId());
        arrProd.add(model1);
        model1 = new ProductModel();
        model1.setName(model.getName().trim() + 2);
        model1.setId(model.getId());
        arrProd.add(model1);
        model1 = new ProductModel();
        model1.setName(model.getName().trim() + 3);
        model1.setId(model.getId());
        arrProd.add(model1);

        ArrayAdapterSubCategory adapterProduct = new ArrayAdapterSubCategory(arrProd, activity);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
        holder.recyclerView.setLayoutManager(mLayoutManager);
        holder.recyclerView.setItemAnimator(new DefaultItemAnimator());
        holder.recyclerView.setAdapter(adapterProduct);

        if (arrRecycler.contains(position)) {
            holder.recyclerView.setVisibility(View.VISIBLE);
        } else {
            holder.recyclerView.setVisibility(View.GONE);
        }
        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.recyclerView.getVisibility() == View.VISIBLE) {
                    holder.recyclerView.setVisibility(View.GONE);
                    for (int i = 0; i < arrRecycler.size(); i++) {
                        if (arrRecycler.get(i) == position)
                            arrRecycler.remove(i);
                        break;
                    }
                } else {
                    holder.recyclerView.setVisibility(View.VISIBLE);
                    arrRecycler.add(position);
                }
            }
        });
    }
}
