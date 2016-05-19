package com.myjash.app.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.myjash.app.R;

import java.util.List;

import com.myjash.app.app.AppController;
import com.myjash.app.model.CompanyModel;

/**
 * Created by ubundu on 6/4/16.
 */
public class ArrayAdpterCompany extends BaseAdapter {
    List<CompanyModel> array;
    private static LayoutInflater inflater = null;
    Activity context;

    public ArrayAdpterCompany(List<CompanyModel> array, Activity context) {
        this.array = array;
        this.context = context;
        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return array.size();
    }

    public class ViewHolder {
        NetworkImageView img;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View rowView;
        ViewHolder holder = new ViewHolder();
        rowView = inflater.inflate(R.layout.layout_list_companies, null);
        holder.img = (NetworkImageView) rowView.findViewById(R.id.img);

        if (array.get(position).getUrl() != null) {
            Log.d("ImAgeURl", array.get(position).getUrl());
            try {
                ImageLoader imageLoader = AppController.getInstance().getImageLoader();
                holder.img.setImageUrl(array.get(position).getUrl(), imageLoader);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /*rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Companies.companyId = "";
                Companies.companyId = array.get(position).getId() + "";
                Log.d("Clicked", "2222");
                MainContainer.fragmentForBackPress = new Companies();
                Bundle bundle = new Bundle();
                Log.d("Clicked", "3333");
                bundle.putInt("id", Integer.parseInt(Companies.companyId));
                Log.d("Clicked", "444");
                bundle.putString("type", "company");
                Product fragobj = new Product();
                Log.d("Clicked", "555");
                fragobj.setArguments(bundle);
                Log.d("Clicked", "6666");
                context.getFragmentManager().beginTransaction()
                        .replace(R.id.lytMainContainer, fragobj).addToBackStack(null)
                        .commit();
            }
        });*/
        return rowView;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public Object getItem(int position) {
        return position;
    }


}
