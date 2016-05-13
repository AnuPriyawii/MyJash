package com.wiinnova.myjash.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wiinnova.myjash.R;

import java.util.ArrayList;

/**
 * Created by ubundu on 3/5/16.
 */
public class ArrayAdapterBranches extends RecyclerView.Adapter<ArrayAdapterBranches.MyViewHolder> {
    Activity activity;
    ArrayList<String> array;
    private static LayoutInflater inflater = null;
    ArrayList<Integer> arrayPos;

    public ArrayAdapterBranches(Activity activity, ArrayList<String> array) {
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
            txtName = (TextView) view.findViewById(R.id.txtName);
            txtDesc = (TextView) view.findViewById(R.id.txtDesc);
            txtName.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (txtDesc.getVisibility() == View.VISIBLE) {
                txtDesc.setVisibility(View.GONE);
//                    arrayPos.remove(position);
            } else {
                txtDesc.setVisibility(View.VISIBLE);
//                    arrayPos.add(position, position);
            }
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

//        holder.txtName.setText(array.get(position));
        holder.txtDesc.setText(array.get(position));
    }


}
