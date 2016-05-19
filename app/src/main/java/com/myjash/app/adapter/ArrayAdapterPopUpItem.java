package com.myjash.app.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.myjash.app.R;

import java.util.ArrayList;
import java.util.HashMap;

import com.myjash.app.fragments.Search;
import com.myjash.app.model.ProductModel;
import com.myjash.app.model.SearchCategoryModel;

import static com.myjash.app.AppUtil.PopUpSearchCategory.arrSearchCategory;

/**
 * Created by ubundu on 6/4/16.
 */
public class ArrayAdapterPopUpItem extends RecyclerView.Adapter<ArrayAdapterPopUpItem.MyViewHolder> {
    private ArrayList<ProductModel> array;
    Activity activity;
    String category;
    HashMap<Integer, View> hashMap;
    MyViewHolder holder;
    View myView;


    public ArrayAdapterPopUpItem(ArrayList<ProductModel> array, Activity activity, String category) {
        this.array = array;
        this.activity = activity;
        this.category = category;
        arrSearchCategory.clear();
        hashMap = new HashMap<>();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
        public TextView txtproduct;
        CheckBox checkBox;
        LinearLayout lytMain;


        public MyViewHolder(View view) {
            super(view);
            myView = view;
//            myView.setOnClickListener(this);
            txtproduct = (TextView) view.findViewById(R.id.txtName);
            checkBox = (CheckBox) view.findViewById(R.id.checkbox);
            lytMain = (LinearLayout) view.findViewById(R.id.lytMain);
//            checkBox.setOnCheckedChangeListener(this);
        }

        @Override
        public void onClick(View v) {
//            Log.d("Clicked", "Clicked");
//            Object item = v.getTag(R.integer.tag1);
//            CheckBox checkBox = (CheckBox) v.getTag(R.integer.tag2);
//            Log.d("Clicked", checkBox + " and " + item.toString());
//            if (item != null) {
//                SearchCategoryModel model = new SearchCategoryModel();
//                model.setCategory(category);
//                model.setItem(item.toString());
          /*  if (holder.checkBox.isChecked()) {
                holder.checkBox.setChecked(false);
            } else {
                holder.checkBox.setChecked(true);
            }*/
//            }
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        }/*{
            if (buttonView.getTag(R.integer.tag3) != null) {
                SearchCategoryModel model = new SearchCategoryModel();
                model.setCategory(category);
                model.setItem(buttonView.getTag(R.integer.tag3).toString());
                model.setId(Integer.parseInt(buttonView.getTag(R.integer.tag4).toString()));
                Log.d("CheckedItem", model.getItem() + " f " + model.getId());
                if (isChecked) {
                    arrSearchCategory.add(model);
                    ProductModel model1 = new ProductModel();
                    model1.setChecked(true);
//                    array.add(Integer.parseInt(buttonView.getTag(R.integer.tag5) + ""), model1);
                } else {
                    *//*remove from main array*//*
//                    array.remove(Integer.parseInt(buttonView.getTag(R.integer.tag5) + ""));
                    for (int i = 0; i < Search.arrSearchCat.size(); i++) {

                        if (Search.arrSearchCat.get(i).getCategory().equals(category) && Search.arrSearchCat.get(i).getItem().equals(model.getItem())) {
                            Search.arrSearchCat.remove(i);
                        }
                    }
                    *//*remove from local array*//*
                    for (int i = 0; i < arrSearchCategory.size(); i++) {

                        if (arrSearchCategory.get(i).getCategory().equals(category) && arrSearchCategory.get(i).getItem().equals(model.getItem())) {
                            arrSearchCategory.remove(i);
                        }
                    }
                }
            }
        }*/
    }

    @Override
    public int getItemCount() {
        return array.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_list_popup_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.txtproduct.setText(array.get(position).getName());

        Log.d("Checkeddc", array.get(position).isChecked() + " d");
           /*boolean isExist = false;
     for (int i = 0; i < arrSearchCategory.size(); i++) {
            if (arrSearchCategory.get(i).getId() == array.get(position).getId()) {
                isExist = true;
                break;
            }
        }*/
        if (array.get(position).isChecked()) {
            holder.checkBox.setChecked(true);
        } else {

            holder.checkBox.setChecked(false);
        }

        holder.checkBox.setTag(R.integer.tag3, array.get(position).getName());
        holder.checkBox.setTag(R.integer.tag4, array.get(position).getId());
        holder.checkBox.setTag(R.integer.tag5, position);
        myView.setTag(R.integer.tag1, array.get(position).getName());
        myView.setTag(R.integer.tag2, holder.checkBox);


        holder.lytMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.checkBox.isChecked()) {
                    holder.checkBox.setChecked(false);
                } else {

                    holder.checkBox.setChecked(true);
                }
            }
        });
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (holder.txtproduct.getText() != null) {
                    SearchCategoryModel model = new SearchCategoryModel();
                    model.setCategory(category);
                    model.setItem(holder.txtproduct.getText().toString());
                    model.setId(Integer.parseInt(holder.checkBox.getTag(R.integer.tag4).toString()));
                    Log.d("CheckedItem", model.getItem() + " f " + model.getId());
                    if (isChecked) {
                        arrSearchCategory.add(model);
//                        array.get(position).setChecked(true);
//                        array.remove(position);
//                        ProductModel model1 = array.get(position);
//                        model1.setChecked(true);
//                        array.add(position, model1);
                    } else {
                    /*remove from main array*/
//
//                        array.get(position).setChecked(false);
                        for (int i = 0; i < Search.arrSearchCat.size(); i++) {

                            if (Search.arrSearchCat.get(i).getCategory().equals(category) && Search.arrSearchCat.get(i).getItem().equals(model.getItem())) {
                                Search.arrSearchCat.remove(i);
                            }
                        }
                    /*remove from local array*/
                        for (int i = 0; i < arrSearchCategory.size(); i++) {

                            if (arrSearchCategory.get(i).getCategory().equals(category) && arrSearchCategory.get(i).getItem().equals(model.getItem())) {
                                arrSearchCategory.remove(i);
                            }
                        }
                    }
                }
            }
        });
    }
}
