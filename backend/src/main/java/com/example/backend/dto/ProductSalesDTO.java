package com.example.backend.dto;

import java.math.BigDecimal;

public class ProductSalesDTO {
    private String productName;
    private Long quantitySold;
    private BigDecimal totalRevenue;

    public ProductSalesDTO(String productName, Long quantitySold, BigDecimal totalRevenue) {
        this.productName = productName;
        this.quantitySold = quantitySold;
        this.totalRevenue = totalRevenue;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Long getQuantitySold() {
        return quantitySold;
    }

    public void setQuantitySold(Long quantitySold) {
        this.quantitySold = quantitySold;
    }

    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(BigDecimal totalRevenue) {
        this.totalRevenue = totalRevenue;
    }
}
