package com.wiinnova.myjash.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.wiinnova.myjash.AppUtil.HeaderAction;
import com.wiinnova.myjash.AppUtil.InternetService;
import com.wiinnova.myjash.AppUtil.LruBitmapCache;
import com.wiinnova.myjash.AppUtil.NetworkImageView;
import com.wiinnova.myjash.AppUtil.Util;
import com.wiinnova.myjash.AppUtil.ZoomOutPageTransformer;
import com.wiinnova.myjash.activity.MainContainer;
import com.wiinnova.myjash.R;
import com.wiinnova.myjash.app.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.wiinnova.myjash.activity.MainContainer.fragmentManager;

public class DashBoard extends Fragment {
    LinearLayout lytProduct;
    LinearLayout lytCategory;
    LinearLayout lytBrand;
    LinearLayout lyTComapny;
    LinearLayout lytMall;
    LinearLayout lytLocation;
    RelativeLayout lytMenu;
    static ViewPager viewPager;
    public static PagerAdapter mPagerAdapter;
    public static Activity activity;
    public static int NUM_PAGES = 0;
    public static String[] arrUrl;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_dash_board, container, false);
        activity = getActivity();
        lytProduct = (LinearLayout) view.findViewById(R.id.lytProduct);
        lytBrand = (LinearLayout) view.findViewById(R.id.lytBrand);
        lytCategory = (LinearLayout) view.findViewById(R.id.lytCategory);
        lyTComapny = (LinearLayout) view.findViewById(R.id.lytCompany);
        lytLocation = (LinearLayout) view.findViewById(R.id.lytLocation);
        lytMall = (LinearLayout) view.findViewById(R.id.lytMall);
        lytMenu = (RelativeLayout) view.findViewById(R.id.lytMenu);
        viewPager = (ViewPager) view.findViewById(R.id.viewFlipper);



        /*View flipper*/
        if (Util.haveNetworkConnection(getActivity())) {
            new InternetService(getActivity()).downloadDataByGet("getFlip", "dashboard", true);
        }

        /*Set header*/
        new HeaderAction(view, getActivity());

        lytProduct.setOnClickListener(onClick);
        lytMall.setOnClickListener(onClick);
        lytLocation.setOnClickListener(onClick);
        lyTComapny.setOnClickListener(onClick);
        lytCategory.setOnClickListener(onClick);
        lytBrand.setOnClickListener(onClick);
        lytMenu.setOnClickListener(onClick);


        return view;
    }

    public static void setViewFlipper(JSONObject json) {
        try {
            JSONArray jsonArray = new JSONArray(json.getString("result"));
            NUM_PAGES = jsonArray.length();
            if (NUM_PAGES > 0) {
                viewPager.setVisibility(View.VISIBLE);
            }
            arrUrl = new String[NUM_PAGES];
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String url = InternetService.IMG_BASE_URL + "vendor/" + jsonObject.getString("logo");

                NetworkImageView networkImageView = new NetworkImageView(activity);

                RequestQueue mRequestQueue = AppController.getInstance().getRequestQueue();
                ImageLoader imageLoader = new ImageLoader(mRequestQueue, new LruBitmapCache());
                imageLoader.get(url, ImageLoader.getImageListener(
                        networkImageView, R.drawable.logo_round, R.drawable.logo));
                networkImageView.setImageUrl(url, imageLoader);
                networkImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                networkImageView.setLayoutParams(params);
                arrUrl[i] = url;
                viewPager.addView(networkImageView);

            }
            /*View pager for flip*/
            viewPager.setPageTransformer(true, new ZoomOutPageTransformer());
            mPagerAdapter = new ScreenSlidePagerAdapter(fragmentManager);
            viewPager.setAdapter(mPagerAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.lytProduct:
                    getFragmentManager().beginTransaction()
                            .replace(R.id.lytMainContainer, new Product(), "PRODUCT").addToBackStack(null)
                            .commit();
                    break;
                case R.id.lytCategory:
                    getFragmentManager().beginTransaction()
                            .replace(R.id.lytMainContainer, new Category(), "CATEGORY").addToBackStack(null)
                            .commit();
                    break;
                case R.id.lytBrand:
                    getFragmentManager().beginTransaction()
                            .replace(R.id.lytMainContainer, new Brand(), "BRAND").addToBackStack(null)
                            .commit();
                    break;
                case R.id.lytCompany:
                    getFragmentManager().beginTransaction()
                            .replace(R.id.lytMainContainer, new Companies(), "COMPANY").addToBackStack(null)
                            .commit();
                    break;
                case R.id.lytMall:
                    getFragmentManager().beginTransaction()
                            .replace(R.id.lytMainContainer, new MallFragment(), "MALL").addToBackStack(null)
                            .commit();
                    break;
                case R.id.lytLocation:
                    getFragmentManager().beginTransaction()
                            .replace(R.id.lytMainContainer, new Location(), "LOCATION").addToBackStack(null)
                            .commit();
                    break;
                case R.id.lytMenu:

                    MainContainer.drawerLayout.openDrawer(Gravity.LEFT);

                    break;
            }
        }
    };

    /**
     * A simple pager adapter that for flip in between number of images
     */
    private static class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            Log.d("ImageURl", "positiom" + position);

            Bundle bundle = new Bundle();
            bundle.putString("url", arrUrl[position]);
            bundle.putInt("position", position);
            ScreenSlidePageFragmentForFlip fragobj = new ScreenSlidePageFragmentForFlip();
            fragobj.setArguments(bundle);
            return fragobj;
        }


        @Override
        public int getCount() {
            return NUM_PAGES;
        }


    }
}
