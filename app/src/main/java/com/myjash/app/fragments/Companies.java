package com.myjash.app.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.myjash.app.AppUtil.HeaderAction;
import com.myjash.app.AppUtil.InternetService;
import com.myjash.app.activity.MainContainer;
import com.myjash.app.R;

import java.util.ArrayList;
import java.util.List;

import com.myjash.app.adapter.ArrayAdpterCompany;
import com.myjash.app.model.CompanyModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Companies extends Fragment {
    static GridView gridCompany;
    static ArrayAdpterCompany adapter;
    static List<CompanyModel> array;
    static Activity activity;
    public static String companyId;
    public static boolean haveData;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_companies, container, false);
        gridCompany = (GridView) view.findViewById(R.id.gridCompany);
        activity = getActivity();
        array = new ArrayList<>();
        haveData = false;
        /*Set header*/
        new HeaderAction(view, getActivity());

        /////////////////////Download data
        Log.d("ArraySize", array.size() + " d");

        new InternetService(getActivity()).loadDataFromCache("getAllActiveCompanies", "company");

        /*ItemClickListener*/
        gridCompany.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                companyId = "";
                companyId = array.get(position).getId() + "";
                MainContainer.fragmentForBackPress = new Companies();
                Bundle bundle = new Bundle();
                bundle.putInt("id", Integer.parseInt(companyId));
                bundle.putString("type", "company");
                Product fragobj = new Product();
                fragobj.setArguments(bundle);
                getFragmentManager().beginTransaction()
                        .replace(R.id.lytMainContainer, fragobj).addToBackStack(null)
                        .commit();
            }
        });
        return view;
    }

    public static void loadImage(JSONArray jsonArray) {
        if (array.size() > 0) {
            haveData = true;
        }
        //////////////////////////////Download image
        array.clear();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String imgUrl = jsonObject.getString("logo");
                imgUrl = InternetService.IMG_BASE_URL + "company/" + imgUrl;
                /////////////set image url to array
                CompanyModel model = new CompanyModel();
                model.setUrl(imgUrl);
                model.setId(jsonObject.getInt("cmp_id"));
                array.add(i, model);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (!haveData) {
            adapter = new ArrayAdpterCompany(array, activity);
            gridCompany.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }


}
