package com.myjash.app.adapter;

import android.content.res.TypedArray;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.myjash.app.R;

import java.util.ArrayList;

import com.myjash.app.fragments.Search;

/**
 * Created by ubundu on 6/4/16.
 */
public class ArrayAdapterSearchCategory extends RecyclerView.Adapter<ArrayAdapterSearchCategory.MyViewHolder> {
    private ArrayList<String> array;
    Search activity;
    private TypedArray arrIcon;


    public ArrayAdapterSearchCategory(ArrayList<String> array, TypedArray arrIcon, Search activity) {
        this.array = array;
        this.activity = activity;
        this.arrIcon = arrIcon;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView txtproduct;
        ImageView imgIcon;
        EditText edtProduct;
        View v;

        public MyViewHolder(View view) {
            super(view);
            v = view;
            view.setOnClickListener(this);
            txtproduct = (TextView) view.findViewById(R.id.txtProdName);
            imgIcon = (ImageView) view.findViewById(R.id.img);
            edtProduct = (EditText) view.findViewById(R.id.edtProduct);
        }

        @Override
        public void onClick(View v) {
            Log.d("Clicked", "Clicked");
            if (v.getTag() != null)
                activity.showPopUp(v.getTag().toString(), v);
//            fragmentManager.beginTransaction()
//                    .replace(R.id.lytMainContainer, new PdfViewerFragment()).addToBackStack(null)
//                    .commit();
        }
    }

    @Override
    public int getItemCount() {
        return array.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_list_search_category, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if (position == 0) {
            holder.edtProduct.setVisibility(View.VISIBLE);
            holder.edtProduct.requestFocus();
            holder.txtproduct.setVisibility(View.GONE);
            holder.v.setTag(null);
        }
        holder.txtproduct.setHint(array.get(position));
        holder.v.setTag(array.get(position));
        if (position < arrIcon.length())
            holder.imgIcon.setBackgroundResource(arrIcon.getResourceId(position, -1));
    }
}
