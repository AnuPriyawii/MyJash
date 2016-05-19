package com.myjash.app.AppUtil;

import android.app.Activity;
import android.content.SharedPreferences;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.myjash.app.R;
import com.myjash.app.adapter.ArrayAdpterDrawer;

/**
 * Created by ubundu on 25/4/16.
 */
public class SlidingDrawer {
    Activity activity;
    RecyclerView recyclerView;
    //    ArrayAdpterDrawer adapterDrawer;
    DrawerLayout drawer;
    String[] arraySlider =new String[3];
    int[] arrayIcon = {R.drawable.about, R.drawable.contact, R.drawable.login};

    public SlidingDrawer(Activity activity) {
        this.activity = activity;



  /*Sliding drawer*/
        recyclerView = (RecyclerView) activity.findViewById(R.id.recycler_view_drawer);
        drawer = (DrawerLayout) activity.findViewById(R.id.drawerLayout);

        arraySlider[0]="About us";
        arraySlider[1]="Contact us";
        /*Check is already logined*/
        SharedPreferences mSharedPreferences = activity.getSharedPreferences(
                "LOGIN", 0);
        if (mSharedPreferences.getString("NAME", "") != "") {
            arraySlider[2] = "Logout";
        } else {
            arraySlider[2] = "Login";
        }

        ArrayAdpterDrawer adapterDrawer = new ArrayAdpterDrawer(arraySlider, arrayIcon, activity, drawer);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapterDrawer);
    }
}
