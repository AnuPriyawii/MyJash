package com.myjash.app.adapter;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.myjash.app.activity.MainContainer;
import com.myjash.app.R;

import java.util.List;

import com.myjash.app.fragments.Mall;
import com.myjash.app.fragments.Product;
import com.myjash.app.model.ProductModel;

/**
 * Created by ubundu on 6/4/16.
 */
public class ArrayAdapterMall extends RecyclerView.Adapter<ArrayAdapterMall.MyViewHolder> {
    private List<ProductModel> arrayProd;
    public View V;
    Activity activity;

    public ArrayAdapterMall(List<ProductModel> arrayProd, Activity activity) {
        this.arrayProd = arrayProd;
        this.activity = activity;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView txtproduct;
        public TextView txtPlace;
        public NetworkImageView imageView;

        public MyViewHolder(View view) {
            super(view);
            V = view;
            V.setOnClickListener(this);
            txtproduct = (TextView) view.findViewById(R.id.txtProdName);
            txtPlace = (TextView) view.findViewById(R.id.txtPlace);
            imageView = (NetworkImageView) view.findViewById(R.id.img);

        }

        @Override
        public void onClick(View v) {
            String id = v.getTag().toString();
            Mall.mallId = "";
            Mall.mallId = id;
            MainContainer.fragmentForBackPress = new Mall();
            Bundle bundle = new Bundle();
            bundle.putInt("id", Integer.parseInt(id));
            bundle.putString("type", "mall");
            Product fragobj = new Product();
            fragobj.setArguments(bundle);
            activity.getFragmentManager().beginTransaction()
                    .replace(R.id.lytMainContainer, fragobj).addToBackStack(null)
                    .commit();
        }
    }

    @Override
    public int getItemCount() {
        return arrayProd.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_list_mall, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ProductModel model = arrayProd.get(position);
        holder.txtPlace.setText(model.getPlace());
        holder.txtproduct.setText(model.getName());
        V.setTag(model.getId());
//        try {
//            if (model.getUrl() != null) {
//                ImageLoader imageLoader = AppController.getInstance().getImageLoader();
//                holder.imageView.setImageUrl(model.getUrl(), imageLoader);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
}
