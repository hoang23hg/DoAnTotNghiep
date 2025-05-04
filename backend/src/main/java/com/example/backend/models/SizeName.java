package com.example.backend.models;

public enum SizeName {
    S("S"), M("M"), L("L"), XL("XL"), XXL("XXL"),
    XANH("Xanh"), DO("Đỏ"), TRANG("Trắng"), DEN("Đen"), VANG("Vàng"),
    INCH_32("32 inch"), INCH_43("43 inch"), INCH_55("55 inch"), INCH_75("75 inch"),
    SO_36("36"), SO_37("37"), SO_38("38"), SO_39("39"), SO_40("40"),
    SO_41("41"), SO_42("42"), SO_43("43");

    private final String value;

    SizeName(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static SizeName fromValue(String value) {
        for (SizeName s : SizeName.values()) {
            if (s.value.equalsIgnoreCase(value)) {
                return s;
            }
        }
        throw new IllegalArgumentException("Unknown size: " + value);
    }
}
