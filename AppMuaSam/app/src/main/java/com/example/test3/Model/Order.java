package com.example.test3.Model;

public class Order {
    private int order_id;
    private String total_price;
    private String order_date;
    private String status;
    private String payment_method;
    private String receiver_name;
    private String phone_number;
    private String full_address;
    private int product_id;
    private String uid;

    public int getOrder_id() { return order_id; }
    public String getTotal_price() { return total_price; }
    public String getOrder_date() { return order_date; }
    public String getStatus() { return status; }
    public String getPayment_method() { return payment_method; }
    public String getReceiver_name() { return receiver_name; }
    public String getPhone_number() { return phone_number; }
    public String getFull_address() { return full_address; }

    public int getProduct_id() { return product_id; }
    public String getUid() { return uid; }


    public Order(int order_id, String total_price, String order_date, String status,
                 String payment_method, String receiver_name, String phone_number,
                 String full_address, int product_id, String uid) {
        this.order_id = order_id;
        this.total_price = total_price;
        this.order_date = order_date;
        this.status = status;
        this.payment_method = payment_method;
        this.receiver_name = receiver_name;
        this.phone_number = phone_number;
        this.full_address = full_address;
        this.product_id = product_id;
        this.uid = uid;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setPayment_method(String payment_method) {
        this.payment_method = payment_method;
    }

    public void setReceiver_name(String receiver_name) {
        this.receiver_name = receiver_name;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public void setFull_address(String full_address) {
        this.full_address = full_address;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
