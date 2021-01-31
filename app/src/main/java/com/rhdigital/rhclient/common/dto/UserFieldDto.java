package com.rhdigital.rhclient.common.dto;

public class UserFieldDto {

    public static int BASIC = 0;
    public static int COUNTRY = 1;

    private String field;
    private String value;
    private int type;

    public UserFieldDto(String field, String value, int type) {
        this.field = field;
        this.value = value;
        this.type = type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public String getField() {
        return field;
    }

    public String getValue() {
        return value;
    }

    public void setField(String field) {
        this.field = field;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
