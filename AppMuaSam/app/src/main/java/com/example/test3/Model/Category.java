package com.example.test3.Model;

import com.google.gson.annotations.SerializedName;

public class Category {
    @SerializedName("category_id")
    private int id;
    @SerializedName("category_name")
    private String category_name;
    @SerializedName("image_url")
    private String imageUrl;

    public Category(int id, String category_name, String imageUrl) {
        this.id = id;
        this.category_name= category_name;
        this.imageUrl = imageUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void getCategory_name(String name) {
        this.category_name= name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
