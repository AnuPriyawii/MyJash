package com.myjash.app.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.myjash.app.R;
import com.myjash.app.activity.GoogleMap;
import com.myjash.app.activity.PopUpProduct;
import com.myjash.app.model.ProductModel;

import java.util.ArrayList;

/**
 * Created by ubundu on 3/5/16.
 */
public class ArrayAdapterBranches extends RecyclerView.Adapter<ArrayAdapterBranches.MyViewHolder> {
    Activity activity;
    ArrayList<ProductModel> array;
    private static LayoutInflater inflater = null;
    ArrayList<Integer> arrayPos;
    View V;

    public ArrayAdapterBranches(Activity activity, ArrayList<ProductModel> array) {
        this.activity = activity;
        this.array = array;
        inflater = (LayoutInflater) activity.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        arrayPos = new ArrayList<Integer>(array.size());

    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtName;
        TextView txtDesc;

        public MyViewHolder(View view) {
            super(view);
            V = view;
            txtName = (TextView) view.findViewById(R.id.txtName);
            txtDesc = (TextView) view.findViewById(R.id.txtDesc);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
           /* if (txtDesc.getVisibility() == View.VISIBLE) {
                txtDesc.setVisibility(View.GONE);
//                    arrayPos.remove(position);
            } else {
                txtDesc.setVisibility(View.VISIBLE);
//                    arrayPos.add(position, position);
            }*/
            int pos = Integer.parseInt(v.getTag().toString());
            Intent intent = new Intent(activity, GoogleMap.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("latitude", array.get(pos).getLatitude());
            intent.putExtra("longitude", array.get(pos).getLongitude());
            intent.putExtra("name", array.get(pos).getName());
            activity.startActivity(intent);
        }
    }

    @Override
    public int getItemCount() {
        return array.size();
    }

    @Override
    public ArrayAdapterBranches.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_list_branches, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ArrayAdapterBranches.MyViewHolder holder, int position) {

        holder.txtName.setText(array.get(position).getName());
        holder.txtDesc.setText(array.get(position).getAddress());
        V.setTag(position);
    }


}
