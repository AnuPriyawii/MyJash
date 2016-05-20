package com.myjash.app.fragments;

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

import com.myjash.app.AppUtil.HeaderAction;
import com.myjash.app.AppUtil.PopUpSearchCategory;
import com.myjash.app.AppUtil.VerticalSpaceItemDecoration;
import com.myjash.app.activity.MainContainer;
import com.myjash.app.R;
import com.myjash.app.adapter.ArrayAdapterSearchCategory;
import com.myjash.app.model.SearchCategoryModel;

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
    public static String locatioId = null;
    public static String productName = null;
    public static String categoryId = null;
    public static String companyId = null;
    public static String brandId = null;
    public static String mallId = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_search, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        btnAdvanced = (Button) view.findViewById(R.id.btnAdvanced);
        btnSearch = (Button) view.findViewById(R.id.btnSearch);
        arrSearchCat = new ArrayList<SearchCategoryModel>();

        /*Set header*/
        new HeaderAction(view, getActivity());

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
                arrProd.add("Shop");
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

                MainContainer.fragmentForBackPress = new Search();
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
        locatioId = null;
        categoryId = null;
        companyId = null;
        brandId = null;
        mallId = null;
        for (int i = 0; i < arrSearchCat.size(); i++) {
            if (arrSearchCat.get(i).getCategory().equals("Location")) {
                if (locatioId != null)
                    locatioId = arrSearchCat.get(i).getId() + "";
                else
                    locatioId = locatioId + "," + arrSearchCat.get(i).getId() + "";
            } else if (arrSearchCat.get(i).getCategory().equals("Category")) {
                if (categoryId!= null)
                    categoryId = arrSearchCat.get(i).getId() + "";
                else
                    categoryId = categoryId + "," + arrSearchCat.get(i).getId() + "";
            } else if (arrSearchCat.get(i).getCategory().equals("Company")) {
                if (companyId!= null)
                    companyId = arrSearchCat.get(i).getId() + "";
                else
                    companyId = companyId + "," + arrSearchCat.get(i).getId() + "";
            } else if (arrSearchCat.get(i).getCategory().equals("Brand")) {
                if (brandId!= null)
                    brandId = arrSearchCat.get(i).getId() + "";
                else
                    brandId = brandId + "," + arrSearchCat.get(i).getId() + "";
            } else if (arrSearchCat.get(i).getCategory().equals("Mall")) {
                if (mallId!= null)
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
