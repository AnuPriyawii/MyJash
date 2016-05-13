package com.wiinnova.myjash.model;

/**
 * Created by ubundu on 12/4/16.
 */
public class SearchCategoryModel {
    private String category;
    private String item;
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }
}
