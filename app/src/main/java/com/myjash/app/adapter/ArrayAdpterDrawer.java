package com.myjash.app.adapter;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.myjash.app.activity.AboutUs;
import com.myjash.app.AppUtil.SlidingDrawer;
import com.myjash.app.activity.ContactUs;
import com.myjash.app.activity.Login;
import com.myjash.app.R;

/**
 * Created by ubundu on 6/4/16.
 */
public class ArrayAdpterDrawer extends RecyclerView.Adapter<ArrayAdpterDrawer.MyViewHolder> {
    private String[] array;
    int[] arrIcon;
    Activity activity;
    View viewlicked;
    DrawerLayout drawerLayout;

    public ArrayAdpterDrawer(String[] array, int[] arrIcon, Activity activity, DrawerLayout drawerLayout) {
        this.array = array;
        this.activity = activity;
        this.arrIcon = arrIcon;
        this.drawerLayout = drawerLayout;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView txtTitle;
        public ImageView img;

        public MyViewHolder(View view) {
            super(view);
            viewlicked = view;
            view.setOnClickListener(this);
            txtTitle = (TextView) view.findViewById(R.id.txttitle);
            img = (ImageView) view.findViewById(R.id.img);
        }

        @Override
        public void onClick(View v) {
            if (v.getTag().toString().equals("Login")) {
                Intent intent = new Intent(activity, Login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(intent);
            } else if (v.getTag().toString().equals("About us")) {
                if (activity instanceof AboutUs) {
                    drawerLayout.closeDrawer(Gravity.LEFT);
                } else {
                    Intent intent = new Intent(activity, AboutUs.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    activity.startActivity(intent);
                }
            } else if (v.getTag().toString().equals("Contact us")) {
                if (activity instanceof ContactUs) {
                    drawerLayout.closeDrawer(Gravity.LEFT);
                } else {
                    Intent intent = new Intent(activity, ContactUs.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    activity.startActivity(intent);
                }
            } else if (v.getTag().toString().equals("Logout")) {
                SharedPreferences mSharedPreferences = activity.getSharedPreferences(
                        "LOGIN", 0);
                SharedPreferences.Editor editor = mSharedPreferences.edit();
                editor.putString("NAME", "");
                editor.putString("oauth_token", "");
                editor.putString("oauth_token_secret", "");
                editor.putBoolean("isTwitterLogedIn", false);
                editor.commit();
                Toast.makeText(activity, "Success", Toast.LENGTH_SHORT).show();

        /*Set sliding drawer*/
                new SlidingDrawer(activity);
            }
            drawerLayout.closeDrawer(Gravity.LEFT);
//            fragmentManager.beginTransaction()
//                    .replace(R.id.lytMainContainer, new LoginWithFB()).addToBackStack(null)
//                    .commit();

//             LoginWithFB.loginButton.performClick();
//            FacebookSdk.sdkInitialize(activity);
//            LoginManager.getInstance().logInWithReadPermissions(activity, Arrays.asList("public_profile", "user_friends"));
        }
    }

    @Override
    public int getItemCount() {
        return array.length;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.drawer_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.txtTitle.setText(array[position]);
        holder.img.setBackgroundResource(arrIcon[position]);
        viewlicked.setTag(array[position]);
    }


}
