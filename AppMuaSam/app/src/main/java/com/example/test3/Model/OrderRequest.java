package com.example.test3.Model;

import java.util.List;

public class OrderRequest {
    private String uid;
    private double total_price;
    private String paymentMethod;
    private int address_id; // Thêm address_id
    private List<ProductItem> products;

    public OrderRequest(String uid, double total_price, String paymentMethod, int address_id, List<ProductItem> products) {
        this.uid = uid;
        this.total_price = total_price;
        this.paymentMethod = paymentMethod;
        this.address_id = address_id;
        this.products = products;
    }
    public OrderRequest(String uid) {
        this.uid = uid;
    }


    public static class ProductItem {
        private int product_id;
        private int size_id;
        private int quantity;
        private double price;

        public ProductItem(int product_id, int size_id, int quantity, double price) {
            this.product_id = product_id;
            this.size_id = size_id;
            this.quantity = quantity;
            this.price = price;
        }
    }

    // Getter
    public String getUid() { return uid; }
    public double getTotalPrice() { return total_price; }
    public String getPaymentMethod() { return paymentMethod; }
    public int getAddressId() { return address_id; }  // Getter mới
    public List<ProductItem> getProducts() { return products; }
}
