package com.example.test3.Model;

public class Size {
    private int size_id;
    private String size_name;

    // Constructor
    public Size(int size_id, String size_name) {
        this.size_id = size_id;
        this.size_name = size_name;
    }

    // Getter
    public int getSizeId() {
        return size_id;
    }

    public String getSizeName() {
        return size_name;
    }
}

