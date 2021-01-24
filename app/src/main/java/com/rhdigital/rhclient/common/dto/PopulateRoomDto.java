package com.rhdigital.rhclient.common.dto;

public class PopulateRoomDto {
    public final static int USER = 0;
    public final static int REPORTS = 1;
    public final static int PROGRAM_CONTENT = 3;
    public final static int APP_START = 4;

    private int type;
    private String[] DAOS;
    private String[] collections;

    public PopulateRoomDto(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public String[] getCollections() {
        return collections;
    }

    public String[] getDAOS() {
        return DAOS;
    }

    public void setCollections(String[] collections) {
        this.collections = collections;
    }

    public void setDAOS(String[] DAOS) {
        this.DAOS = DAOS;
    }
}
