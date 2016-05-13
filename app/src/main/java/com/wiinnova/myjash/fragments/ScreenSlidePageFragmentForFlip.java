package com.wiinnova.myjash.fragments;

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
import com.wiinnova.myjash.AppUtil.InternetService;
import com.wiinnova.myjash.AppUtil.LruBitmapCache;
import com.wiinnova.myjash.R;
import com.wiinnova.myjash.app.AppController;

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
        final NetworkImageView networkImageView= (NetworkImageView) rootView.findViewById(R.id.img);


//        String url = Flip.IMAGE_URL;
        String url = getArguments().getString("url");
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


        return rootView;
    }
}
