package com.myjash.app.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.myjash.app.R;

import java.util.ArrayList;

/**
 * Created by ubundu on 4/5/16.
 */
public class ArrayAdpterProductMall extends RecyclerView.Adapter<ArrayAdpterProductMall.MyViewHolder> {
    Activity activity;
    String[] array;
    private static LayoutInflater inflater = null;
    ArrayList<Integer> arrayPos;

    public ArrayAdpterProductMall(Activity activity, String[] array) {
        this.activity = activity;
        this.array = array;
        inflater = (LayoutInflater) activity.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        arrayPos = new ArrayList<Integer>(array.length);

    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtName;
        TextView txtDesc;
        ImageView imgIcon;

        public MyViewHolder(View view) {
            super(view);
            txtName = (TextView) view.findViewById(R.id.txtName);
            txtDesc = (TextView) view.findViewById(R.id.txtDesc);
            imgIcon = (ImageView) view.findViewById(R.id.imgIcon);
            txtName.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
          /*  if (txtDesc.getVisibility() == View.VISIBLE) {
                txtDesc.setVisibility(View.GONE);
//                    arrayPos.remove(position);
            } else {
                txtDesc.setVisibility(View.VISIBLE);
//                    arrayPos.add(position, position);
            }*/
        }
    }

    @Override
    public int getItemCount() {
        return array.length;
    }

    @Override
    public ArrayAdpterProductMall.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_list_branches, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.txtName.setText(array[position]);
        holder.txtDesc.setVisibility(View.GONE);
        holder.imgIcon.setVisibility(View.GONE);
    }


}
