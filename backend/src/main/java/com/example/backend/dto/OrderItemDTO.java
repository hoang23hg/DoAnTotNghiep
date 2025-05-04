package com.example.backend.dto;

import java.math.BigDecimal;

public class OrderItemDTO {
    private String productName;
    private int quantity;
    private BigDecimal price; // ✅ Sửa từ double -> BigDecimal

    public OrderItemDTO() {
    }

    public OrderItemDTO(String productName, int quantity, BigDecimal price) { // ✅ Thay đổi kiểu dữ liệu price
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() { // ✅ Sửa kiểu dữ liệu trả về thành BigDecimal
        return price;
    }

    public void setPrice(BigDecimal price) { // ✅ Sửa kiểu dữ liệu tham số thành BigDecimal
        this.price = price;
    }
}
