package com.example.backend.dto;

import lombok.Data;

@Data
public class ProductSizeRequest {
    private Integer productId;
    private String sizeName; // VD: "M", "L", "SIZE_41"
    private Integer stockQuantity;
}
