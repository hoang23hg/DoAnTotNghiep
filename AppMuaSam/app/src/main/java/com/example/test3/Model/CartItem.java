package com.example.test3.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CartItem implements Serializable {
    @SerializedName("cart_id")
    private int cart_id;
    @SerializedName("uid")
    private String uid;
    @SerializedName("product_id")
    private int product_id;
    @SerializedName("product_name")
    private String product_name;
    @SerializedName("product_image")
    private String product_image;
    @SerializedName("quantity")
    private int quantity;
    @SerializedName("size_id")
    private int size_id;
    @SerializedName("size_name")
    private String size_name;
    @SerializedName("price")
    private double price;

    public CartItem(int cart_id, String uid, int product_id, String product_name, String product_image, int quantity, int size_id, String size_name, double price) {
        this.cart_id = cart_id;
        this.uid = uid;
        this.product_id = product_id;
        this.product_name = product_name;
        this.product_image = product_image;
        this.quantity = quantity;
        this.size_id = size_id;
        this.size_name = size_name;
        this.price = price;
    }

    public int getCartId() { return cart_id; }
    public String getUid() { return uid; }
    public int getProductId() { return product_id; }
    public String getProductName() { return product_name; }
    public String getProductImage() { return product_image; }
    public int getQuantity() { return quantity; }
    public int getSizeId() { return size_id; }
    public String getSizeName() { return size_name; }
    public double getPrice() { return price; }

    public void setQuantity(int quantity) { this.quantity = quantity; }
}

