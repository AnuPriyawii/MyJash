package com.myjash.app.fragments;

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
import com.myjash.app.app.AppController;

import java.io.File;

/**
 * Created by ubundu on 7/4/16.
 */
public class ScreenSlidePageFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_screen_slide_page, container, false);
        final com.myjash.app.AppUtil.NetworkImageView networkImageView = (com.myjash.app.AppUtil.NetworkImageView) rootView.findViewById(R.id.img);
        final ProgressBar progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);

//        String url = Flip.IMAGE_URL;
        String url = getArguments().getString("url");
        Log.d("ImageURl", url + " fragment");
        RequestQueue mRequestQueue = AppController.getInstance().getRequestQueue();

        ImageLoader imageLoader = new ImageLoader(mRequestQueue, new LruBitmapCache(
                LruBitmapCache.getCacheSize(getActivity())));

        /* Loading image with placeholder and error image*/
        imageLoader.get(InternetService.FLIP_IMG_URL + url, new ImageLoader.ImageListener() {
            ImageView view = (ImageView) networkImageView;

            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                if (response.getBitmap() != null) {
                    view.setImageBitmap(response.getBitmap());
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {

                view.setImageResource(R.drawable.logo);
                progressBar.setVisibility(View.GONE);
            }

        });


        networkImageView.setImageUrl(InternetService.FLIP_IMG_URL + url, imageLoader);


        return rootView;
    }
}
