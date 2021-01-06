package com.rhdigital.rhclient.common.dto;

public class RemoteResourceDto {

    private String resourceUrl;
    private String resourceId;

    public RemoteResourceDto(String resourceId, String resourceUrl) {
        this.resourceId = resourceId;
        this.resourceUrl = resourceUrl;
    }

    public String getResourceId() {
        return resourceId;
    }

    public String getResourceUrl() {
        return resourceUrl;
    }
}
