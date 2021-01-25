package com.rhdigital.rhclient.common.dto;

public class UserFieldDto {

    private String field;
    private String value;

    public UserFieldDto(String field, String value) {
        this.field = field;
        this.value = value;
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
