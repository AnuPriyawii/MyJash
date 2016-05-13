package com.wiinnova.myjash.fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.wiinnova.myjash.AppUtil.PopUpSearchCategory;
import com.wiinnova.myjash.AppUtil.VerticalSpaceItemDecoration;
import com.wiinnova.myjash.activity.MainContainer;
import com.wiinnova.myjash.R;
import com.wiinnova.myjash.adapter.ArrayAdapterSearchCategory;
import com.wiinnova.myjash.model.SearchCategoryModel;

import java.util.ArrayList;

public class Search extends Fragment {
    ArrayList<String> arrProd;
    TypedArray arrIcon;
    private RecyclerView recyclerView;
    private ArrayAdapterSearchCategory adapter;
    public static ArrayList<SearchCategoryModel> arrSearchCat;
    View clickedView;
    String clickedCategory;
    Button btnAdvanced;
    Button btnSearch;
    public static String locatioId = "";
    public static String productName = "";
    public static String categoryId = "";
    public static String companyId = "";
    public static String brandId = "";
    public static String mallId = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_search, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        btnAdvanced = (Button) view.findViewById(R.id.btnAdvanced);
        btnSearch = (Button) view.findViewById(R.id.btnSearch);
        arrSearchCat = new ArrayList<SearchCategoryModel>();

        ///set value to array for arrayadapter
        arrProd = new ArrayList<String>();
        arrProd.add("Product");
        arrProd.add("Location");

        arrIcon = getResources().obtainTypedArray(R.array.arrIcon);


        /////////////////set arrayadapter
        adapter = new ArrayAdapterSearchCategory(arrProd, arrIcon, Search.this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(1));
        recyclerView.setAdapter(adapter);

        //////////////Advanced search
        btnAdvanced.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arrProd.add("Category");
                arrProd.add("Company");
                arrProd.add("Brand");
                arrProd.add("Mall");
                adapter.notifyDataSetChanged();
                btnAdvanced.setVisibility(View.GONE);
            }
        });
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                getDataForSearch();
                productName = ((EditText) recyclerView.getChildAt(0).findViewById(R.id.edtProduct)).getText().toString();

                MainContainer.fragmentForBackPress =new Search();
                Bundle bundle = new Bundle();
                bundle.putInt("id", 0);
                bundle.putString("type", "search");
                Product fragobj = new Product();
                fragobj.setArguments(bundle);
                getActivity().getFragmentManager().beginTransaction()
                        .replace(R.id.lytMainContainer, fragobj).addToBackStack(null)
                        .commit();
            }
        });
        return view;
    }

    public void getDataForSearch() {
        locatioId = "";
        categoryId = "";
        companyId = "";
        brandId = "";
        mallId = "";
        for (int i = 0; i < arrSearchCat.size(); i++) {
            if (arrSearchCat.get(i).getCategory().equals("Location")) {
                if (locatioId.length() == 0)
                    locatioId = arrSearchCat.get(i).getId() + "";
                else
                    locatioId = locatioId + "," + arrSearchCat.get(i).getId() + "";
            } else if (arrSearchCat.get(i).getCategory().equals("Category")) {
                if (categoryId.length() == 0)
                    categoryId = arrSearchCat.get(i).getId() + "";
                else
                    categoryId = categoryId + "," + arrSearchCat.get(i).getId() + "";
            } else if (arrSearchCat.get(i).getCategory().equals("Company")) {
                if (companyId.length() == 0)
                    companyId = arrSearchCat.get(i).getId() + "";
                else
                    companyId = companyId + "," + arrSearchCat.get(i).getId() + "";
            } else if (arrSearchCat.get(i).getCategory().equals("Brand")) {
                if (brandId.length() == 0)
                    brandId = arrSearchCat.get(i).getId() + "";
                else
                    brandId = brandId + "," + arrSearchCat.get(i).getId() + "";
            } else if (arrSearchCat.get(i).getCategory().equals("Mall")) {
                if (mallId.length() == 0)
                    mallId = arrSearchCat.get(i).getId() + "";
                else
                    mallId = mallId + "," + arrSearchCat.get(i).getId() + "";
            }
        }

    }

    public void showPopUp(String category, View v) {
        clickedView = v;
        clickedCategory = category;
        Intent intent = new Intent(getActivity(), PopUpSearchCategory.class);
        intent.putExtra("Category", category);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1) {

            /*Display checked items*/
            TextView txtName = (TextView) clickedView.findViewById(R.id.txtProdName);
            String item = null;
            for (int i = 0; i < arrSearchCat.size(); i++) {
                if (arrSearchCat.get(i).getCategory().equals(clickedCategory)) {
                    if (item == null)
                        item = arrSearchCat.get(i).getItem();
                    else
                        item = item + " , " + arrSearchCat.get(i).getItem();
                }
            }
            if (item != null)
                txtName.setText(item);
            else {
                txtName.setText("");
            }
        }

    }

}
