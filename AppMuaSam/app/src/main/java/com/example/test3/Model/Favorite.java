package com.example.test3.Model;

import com.google.gson.annotations.SerializedName;

public class Favorite {
    @SerializedName("fav_id")
    private int favId;

    @SerializedName("uid")
    private String uid;

    @SerializedName("product_id")
    private int productId;

    @SerializedName("name")
    private String name; // Tên sản phẩm

    @SerializedName("price")
    private double price; // Giá sản phẩm

    @SerializedName("image_url")
    private String imageUrl; // Ảnh sản phẩm
    private String description;
    public Favorite(int favId, String uid, int productId, String name, double price, String imageUrl, String description) {
        this.favId = favId;
        this.uid = uid;
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.description = description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public int getFavId() {
        return favId;
    }

    public void setFavId(int favId) {
        this.favId = favId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
