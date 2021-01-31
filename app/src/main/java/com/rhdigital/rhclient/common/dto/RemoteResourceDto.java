package com.rhdigital.rhclient.common.dto;

public class RemoteResourceDto {

    public static final int PROGRAM_POSTER = 0;
    public static final int PROFILE_PHOTO = 1;
    public static final int VIDEO_URI = 2;
    public static final int WORKBOOK_URI = 3;
    public static final int REPORT_URI = 4;
    public static final int DOCUMENT_URI = 5;

    private String resourceUrl;
    private String resourceId;
    private int type;

    public RemoteResourceDto(String resourceId, String resourceUrl, int type) {
        this.resourceId = resourceId;
        this.resourceUrl = resourceUrl;
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getResourceId() {
        return resourceId;
    }

    public String getResourceUrl() {
        return resourceUrl;
    }
}
