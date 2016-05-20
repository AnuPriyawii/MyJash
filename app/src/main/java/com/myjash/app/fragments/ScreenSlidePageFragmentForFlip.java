package com.myjash.app.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.myjash.app.AppUtil.InternetService;
import com.myjash.app.AppUtil.LruBitmapCache;
import com.myjash.app.R;
import com.myjash.app.activity.WebViewAdvertisement;
import com.myjash.app.app.AppController;

import java.io.File;

/**
 * Created by ubundu on 7/4/16.
 */
public class ScreenSlidePageFragmentForFlip extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_screen_slide_page_fragment_for_flip, container, false);
        final NetworkImageView networkImageView = (NetworkImageView) rootView.findViewById(R.id.img);


//        String url = Flip.IMAGE_URL;
        String url = getArguments().getString("url");
        final String link = getArguments().getString("link");
        Log.d("ImageURl", url + " fragment");
        RequestQueue mRequestQueue = AppController.getInstance().getRequestQueue();

        ImageLoader imageLoader = new ImageLoader(mRequestQueue, new LruBitmapCache(
                LruBitmapCache.getCacheSize(getActivity())));
        /* Loading image with placeholder and error image*/
        imageLoader.get(url, new ImageLoader.ImageListener() {
            ImageView view = (ImageView) networkImageView;

            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                if (response.getBitmap() != null) {
                    view.setImageBitmap(response.getBitmap());

                } else {
                    view.setImageResource(R.drawable.logo);
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {

                view.setImageResource(R.drawable.logo);

            }

        });


        networkImageView.setImageUrl(url, imageLoader);

        networkImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashBoard.activity, WebViewAdvertisement.class);
                intent.putExtra("url", link);
                DashBoard.activity.startActivity(intent);
            }
        });
        return rootView;
    }
}
