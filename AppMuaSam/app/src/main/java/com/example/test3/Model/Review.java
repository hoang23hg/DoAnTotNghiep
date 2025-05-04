package com.example.test3.Model;

public class Review {
    private String display_name;
    private String comment;
    private float rating;

    public Review(String display_name, String comment, float rating) {
        this.display_name = display_name;
        this.comment = comment;
        this.rating = rating;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public String getComment() {
        return comment;
    }

    public float getRating() {
        return rating;
    }
}
