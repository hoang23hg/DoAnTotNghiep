package com.example.test3.Model;

import com.google.gson.annotations.SerializedName;

public class ApiResponseCart {
    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private String message;

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
