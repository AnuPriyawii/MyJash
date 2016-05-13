
package com.wiinnova.myjash.AppUtil;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wiinnova.myjash.R;
import com.wiinnova.myjash.fragments.Search;
import com.wiinnova.myjash.adapter.ArrayAdapterPopUpItem;
import com.wiinnova.myjash.model.ProductModel;
import com.wiinnova.myjash.model.SearchCategoryModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PopUpSearchCategory extends Activity {
    private static RecyclerView recyclerView;
    TextView txtOkay;
    TextView txtTitle;
    private static ArrayAdapterPopUpItem adapter;
    static ArrayList<ProductModel> arrProd = new ArrayList<ProductModel>();
    static String category;
    static Activity activity;
    static LinearLayout lytMain;
    public static ArrayList<SearchCategoryModel> arrSearchCategory;
    static boolean isExist = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_up_search_category);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        txtOkay = (TextView) findViewById(R.id.txtOkay);
        txtTitle = (TextView) findViewById(R.id.txttitle);
        lytMain = (LinearLayout) findViewById(R.id.lytMain);
        category = getIntent().getExtras().getString("Category");
        Log.d("category", category + " d");
        activity = this;
        arrSearchCategory = new ArrayList<SearchCategoryModel>();
        ////////////////////////////Download data
        if (category.equals("Category")) {
            new InternetService(this).downloadDataByGet("getAllActiveCategories", "search", false);
        } else if (category.equals("Company")) {
            new InternetService(this).downloadDataByGet("getAllActiveCompanies", "search", false);
        } else if (category.equals("Brand")) {
            new InternetService(this).downloadDataByGet("getAllActiveBrands", "search", false);
        } else if (category.equals("Mall")) {
            new InternetService(this).downloadDataByGet("getAllActiveMallLocation", "search", false);
        } else if (category.equals("Location")) {
            new InternetService(this).downloadDataByGet("getAllLocations", "search", false);
        }

        txtTitle.setText(category);


        txtOkay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /////////////////Save checked items to array
                Log.d("SearchSize", arrSearchCategory.size() + " g");
                if (arrSearchCategory.size() > 0)
                    for (int i = 0; i < arrSearchCategory.size(); i++)
                        Search.arrSearchCat.add(arrSearchCategory.get(i));
                setResult(1);
                finish();
            }
        });
    }

    public static void refreshAdapter(JSONArray jsonArray) {
        Log.d("category", category + " d");
        arrProd.clear();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                ProductModel model = new ProductModel();
                if (category.equals("Category")) {
                    model.setName(jsonObject.getString("category_name"));
                    model.setId(jsonObject.getInt("category_id"));
                } else if (category.equals("Company")) {

                    model.setName(jsonObject.getString("cmp_name"));
                    model.setId(jsonObject.getInt("cmp_id"));
                    Log.d("ItemGet", jsonObject.getString("cmp_name") + " d");
                } else if (category.equals("Brand")) {

                    model.setName(jsonObject.getString("brand_name"));
                    model.setId(jsonObject.getInt("id"));
                } else if (category.equals("Mall")) {

                    model.setName(jsonObject.getString("ml_name"));
                    model.setId(jsonObject.getInt("ml_id"));
                } else if (category.equals("Location")) {

                    model.setName(jsonObject.getString("l_name"));
                    model.setId(jsonObject.getInt("l_id"));
                }

                for (int j = 0; j < Search.arrSearchCat.size(); j++) {
                    if (Search.arrSearchCat.get(j).getCategory().equals(category)
                            && Search.arrSearchCat.get(j).getItem().equals(model.getName())
                            && Search.arrSearchCat.get(j).getId() == (model.getId())) {
                        model.setChecked(true);
                        break;
                    } else
                        model.setChecked(false);
                }
               /* isExist = false;
                for (int k = 0; k < arrProd.size(); k++) {
                    if (arrProd.get(k).getId() == model.getId()) {
                        isExist = true;
                        break;
                    }
                }
                if (!isExist) {*/
                    arrProd.add(model);
//                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (arrProd.size() > 0) {
            lytMain.setVisibility(View.VISIBLE);
            adapter = new ArrayAdapterPopUpItem(arrProd, activity, category);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity)
            {
                @Override
                public boolean canScrollVertically() {
                    return false;
                }

                @Override
                public boolean canScrollHorizontally() {
                    return false;
                }
            };
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(1));
            recyclerView.setAdapter(adapter);
        } else {
            activity.finish();
        }
    }

}
