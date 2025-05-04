package com.example.test3.Model;

import java.util.List;

public class OrderResponse {
    private boolean success;
    private String message;
    private List<Order> orders;

    public OrderResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
    public List<Order> getOrders() {
        return orders;
    }
}
