package com.wiinnova.myjash.fragments;

import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wiinnova.myjash.AppUtil.HeaderAction;
import com.wiinnova.myjash.R;

import com.wiinnova.myjash.AppUtil.DepthPageTransformer;

import static com.wiinnova.myjash.activity.MainContainer.fragmentManager;

public class Flip extends Fragment {

    /**
     * The number of pages (wizard steps) to show in this demo.
     */
    private static int NUM_PAGES = 0;
    String[] arrUrl;
    public static Bitmap[] arrBitmap;
    public static String IMAGE_URL;

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager mPager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter mPagerAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_flip, container, false);
        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) view.findViewById(R.id.pager);
        String url = getArguments().getString("url");
        Log.d("ImageURl", url + " frst");
        arrUrl = url.split(",");
        if (arrUrl.length > 0) {
            IMAGE_URL = arrUrl[0];
            arrBitmap = new Bitmap[arrUrl.length];
        }
        NUM_PAGES = arrUrl.length;

        mPager.setPageTransformer(true, new DepthPageTransformer());
        mPagerAdapter = new ScreenSlidePagerAdapter(fragmentManager);
        mPager.setAdapter(mPagerAdapter);

        /*Set header*/
        new HeaderAction(view,getActivity());

        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                IMAGE_URL = arrUrl[position];
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        return view;
    }


    /**
     * A simple pager adapter that for flip in between number of images
     */
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Log.d("ImageURl", "positiom" + position);

            Bundle bundle = new Bundle();
            bundle.putString("url", arrUrl[position]);
            bundle.putInt("position",position);
            ScreenSlidePageFragment fragobj = new ScreenSlidePageFragment();
            fragobj.setArguments(bundle);
            return fragobj;
        }


        @Override
        public int getCount() {
            return NUM_PAGES;
        }


    }
}
