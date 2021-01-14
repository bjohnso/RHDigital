package com.rhdigital.rhclient.common.dto;

import com.rhdigital.rhclient.database.model.Video;

public class VideoControlActionDto {
    public static final int BACK_NAV = 0;
    public static final int MAXIMISE = 1;
    public static final int MINIMIZE = 2;

    private Video video;
    private int actionType;

    public VideoControlActionDto(int actionType, Video video) {
        this.actionType = actionType;
        this.video = video;
    }

    public int getActionType() {
        return actionType;
    }

    public void setActionType(int actionType) {
        this.actionType = actionType;
    }

    public Video getVideo() {
        return video;
    }

    public void setVideo(Video video) {
        this.video = video;
    }
}
