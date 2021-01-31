package com.rhdigital.rhclient.common.dto;

import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
import com.rhdigital.rhclient.room.model.Video;

public class VideoPlayerDto {
    private PlayerView playerView;
    private Video video;
    private SimpleExoPlayer exoPlayer;

    public VideoPlayerDto(PlayerView playerView, Video video) {
        this.playerView = playerView;
        this.video = video;
    }

    public void setExoPlayer(SimpleExoPlayer exoPlayer) {
        this.exoPlayer = exoPlayer;
    }

    public SimpleExoPlayer getExoPlayer() {
        return exoPlayer;
    }

    public PlayerView getPlayerView() {
        return playerView;
    }

    public void setPlayerView(PlayerView playerView) {
        this.playerView = playerView;
    }

    public Video getVideo() {
        return video;
    }

    public void setVideo(Video video) {
        this.video = video;
    }
}
