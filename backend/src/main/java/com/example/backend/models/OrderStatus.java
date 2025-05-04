package com.example.backend.models;

public enum OrderStatus {
    PENDING("pending"),
    SHIPPED("shipped"),
    DELIVERED("delivered"),
    CANCELLED("cancelled");

    private final String status;

    OrderStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    // Chuyển đổi từ chuỗi MySQL sang enum
    public static OrderStatus fromString(String status) {
        for (OrderStatus os : OrderStatus.values()) {
            if (os.status.equalsIgnoreCase(status)) {
                return os;
            }
        }
        throw new IllegalArgumentException("Không tìm thấy trạng thái đơn hàng: " + status);
    }
}
