package com.myjash.app.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.CountDownTimer;
import android.os.Handler;
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
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.myjash.app.AppUtil.HeaderAction;
import com.myjash.app.AppUtil.InternetService;
import com.myjash.app.AppUtil.LruBitmapCache;
import com.myjash.app.AppUtil.Util;
import com.myjash.app.AppUtil.ZoomOutPageTransformer;
import com.myjash.app.activity.MainContainer;
import com.myjash.app.R;
import com.myjash.app.activity.WebViewAdvertisement;
import com.myjash.app.app.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import static com.myjash.app.activity.MainContainer.fragmentManager;

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
    public static String[] arrLink;

    static int currentPage;

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
            new InternetService(getActivity()).downloadDataByGet("getAds", "dashboard", true);
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

      /*  final Handler handler = new Handler();

        final Runnable update = new Runnable() {
            public void run() {

            }
        };


        new Timer().schedule(new TimerTask() {

            @Override
            public void run() {
                handler.post(update);
            }
        }, 10, 50);*/
        new CountDownTimer(120000, 8000) {

            public void onTick(long millisUntilFinished) {
                Log.d("Ticked", "dsfds");
                currentPage = viewPager.getCurrentItem();
                Log.d("Ticked", currentPage + "dsfds");
                if (currentPage == NUM_PAGES - 1) {
                    currentPage = -1;
                }
                Log.d("Ticked", currentPage+1 + "dsfds");
                viewPager.setCurrentItem(currentPage+1, true);
            }

            public void onFinish() {
            }
        }.start();


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
            arrLink = new String[NUM_PAGES];
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                final String url = InternetService.IMG_BASE_URL + "advertisement/" + jsonObject.getString("content");
                final String link = jsonObject.getString("ad_link");

                arrUrl[i] = url;
                arrLink[i] = link;

            }
            /*View pager for flip*/
            viewPager.setPageTransformer(true, new ZoomOutPageTransformer());
            mPagerAdapter = new ScreenSlidePagerAdapter(fragmentManager);
            viewPager.setAdapter(mPagerAdapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public static Bitmap scaleBitmap(Bitmap bitmap, int wantedWidth, int wantedHeight) {
        Bitmap output = Bitmap.createBitmap(wantedWidth, wantedHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        Matrix m = new Matrix();
        m.setScale((float) wantedWidth / bitmap.getWidth(), (float) wantedHeight / bitmap.getHeight());
        canvas.drawBitmap(bitmap, m, new Paint());

        return output;
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
                            .replace(R.id.lytMainContainer, new Mall(), "MALL").addToBackStack(null)
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
            bundle.putString("link", arrLink[position]);

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
