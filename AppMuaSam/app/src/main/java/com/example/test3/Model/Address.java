package com.example.test3.Model;

public class Address {
    private int address_id;
    private String uid;
    private String houseNumber;
    private String street;
    private String ward;
    private String district;
    private String city;
    private boolean is_default;
    private String receiver_name;
    private String phone_number; // Thêm trường phone

    // Constructor
    public Address(int address_id, String uid, String houseNumber, String street, String ward,
                   String district, String city, boolean is_default, String receiver_name, String phone_number) {
        this.address_id = address_id;
        this.uid = uid;
        this.houseNumber = houseNumber;
        this.street = street;
        this.ward = ward;
        this.district = district;
        this.city = city;
        this.is_default = is_default;
        this.receiver_name = receiver_name;
        this.phone_number = phone_number;
    }

    // Getter & Setter
    public int getAddressId() { return address_id; }
    public void setAddressId(int address_id) { this.address_id = address_id; }

    public String getUid() { return uid; }
    public void setUid(String uid) { this.uid = uid; }

    public String getHouseNumber() { return houseNumber; }
    public void setHouseNumber(String houseNumber) { this.houseNumber = houseNumber; }

    public String getStreet() { return street; }
    public void setStreet(String street) { this.street = street; }

    public String getWard() { return ward; }
    public void setWard(String ward) { this.ward = ward; }

    public String getDistrict() { return district; }
    public void setDistrict(String district) { this.district = district; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public boolean isDefault() { return is_default; }
    public void setDefault(boolean is_default) { this.is_default = is_default; }

    public String getReceiverName() { return receiver_name; }
    public void setReceiverName(String receiver_name) { this.receiver_name = receiver_name; }

    public String getPhoneNumber() { return phone_number; }
    public void setPhoneNumber(String phone_number) { this.phone_number = phone_number; }
}
