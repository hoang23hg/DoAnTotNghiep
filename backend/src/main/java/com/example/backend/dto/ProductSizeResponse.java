package com.example.backend.dto;

// ProductSizeResponse.java
public class ProductSizeResponse {
    private Integer productId;
    private String productName;
    private Integer sizeId;
    private String sizeName;
    private int stockQuantity;

    public ProductSizeResponse(Integer productId, String productName, Integer sizeId, String sizeName, int stockQuantity) {
        this.productId = productId;
        this.productName = productName;
        this.sizeId = sizeId;
        this.sizeName = sizeName;
        this.stockQuantity = stockQuantity;
    }

    // Getters
    public Integer getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public Integer getSizeId() {
        return sizeId;
    }

    public String getSizeName() {
        return sizeName;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }
}
