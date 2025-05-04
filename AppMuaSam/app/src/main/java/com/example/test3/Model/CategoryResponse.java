package com.example.test3.Model;

import java.util.List;

public class CategoryResponse {
    private String category_name;
    private List<Product> products;

    public String getCategoryName() {
        return category_name;
    }

    public List<Product> getProducts() {
        return products;
    }
}
