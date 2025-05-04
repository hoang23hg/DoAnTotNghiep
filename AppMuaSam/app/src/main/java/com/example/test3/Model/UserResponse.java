package com.example.test3.Model;

public class UserResponse {
    private String status;
    private User data;

    public String getStatus() {
        return status;
    }

    public User getData() {
        return data;
    }

    public static class User {
        private String uid;
        private String email;
        private String display_name;
        private String phone;
        private String avatar;

        public String getUid() {
            return uid;
        }

        public String getEmail() {
            return email;
        }

        public String getDisplayName() {
            return display_name;
        }

        public String getPhone() {
            return phone;
        }

        public String getAvatar() {
            return avatar;
        }
    }
}



