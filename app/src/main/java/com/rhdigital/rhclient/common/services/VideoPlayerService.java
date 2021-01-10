package com.rhdigital.rhclient.common.services;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.LifecycleOwner;

import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.rhdigital.rhclient.R;
import com.rhdigital.rhclient.common.dto.RemoteResourceDto;
import com.rhdigital.rhclient.common.dto.VideoPlayerDto;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class VideoPlayerService {

  private static VideoPlayerService INSTANCE;
  private boolean isFullScreen = false;
  private HashMap<String, VideoPlayerDto> videoPlayerMap = new HashMap<>();
  private String preserve;

  //TODO : CREATE HOLDER SERVICE

  private VideoPlayerService() {}

  public static VideoPlayerService getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new VideoPlayerService();
    }
    return INSTANCE;
  }

  public void initVideoPlayer(Context context, VideoPlayerDto videoPlayer) {
    attachNewVideoStream(context, videoPlayer);
  }

  private void attachNewVideoStream(Context context, VideoPlayerDto videoPlayer) {
      RemoteResourceService remoteResourceService = new RemoteResourceService();
      List<RemoteResourceDto> resourceList = Arrays.asList(
              new RemoteResourceDto(videoPlayer.getVideo().getId(), videoPlayer.getVideo().getVideoUrl())
      );
      remoteResourceService.getAllVideoURI(resourceList, 0,0).observe((LifecycleOwner) context, resources -> {
        if (resources != null) {
          DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(
                  context,
                  Util.getUserAgent(
                          context,
                          context.getString(R.string.app_name)
                  )
          );
          MediaSource mediaSource = new ProgressiveMediaSource.Factory(dataSourceFactory)
                  .createMediaSource(resources.get(
                          videoPlayer.getVideo().getId()
                  ));
          SimpleExoPlayer simpleExoPlayer = new SimpleExoPlayer.Builder(context).build();
          simpleExoPlayer.prepare(mediaSource);
          videoPlayer.setExoPlayer(simpleExoPlayer);
          videoPlayer.getPlayerView().setPlayer(simpleExoPlayer);
          videoPlayerMap.put(videoPlayer.getVideo().getId(), videoPlayer);
        }
      });
  }

  public void resumeVideoStream(Context context, String videoId, PlayerView playerView) {
      VideoPlayerDto videoPlayer = this.videoPlayerMap.get(videoId);
      if (videoPlayer != null) {
          SimpleExoPlayer player = videoPlayer.getExoPlayer();
          if (player == null) {
              player = new SimpleExoPlayer.Builder(context).build();
              videoPlayer.setExoPlayer(player);
          }
          PlayerView.switchTargetView(
                  player,
                  videoPlayer.getPlayerView(),
                  playerView);
          videoPlayer.setPlayerView(playerView);
      }
  }

  public void destroyVideoStream(String videoId) {
      if (!videoId.equals(preserve)) {
          VideoPlayerDto videoPlayer = videoPlayerMap.get(videoId);
          if (videoPlayer != null) {
              PlayerView playerView = videoPlayer.getPlayerView();
              if (playerView != null) {
                  SimpleExoPlayer simpleExoPlayer = (SimpleExoPlayer) playerView.getPlayer();
                  if (simpleExoPlayer != null) {
                      simpleExoPlayer.release();
                      simpleExoPlayer = null;
                  }
                  playerView = null;
              }
              videoPlayerMap.remove(videoPlayer);
          }
      }
  }

  public void destroyAllVideoStreams(Boolean hardDestroy) {
      if (hardDestroy) {
          preserve = null;
      }
      for (String key : this.videoPlayerMap.keySet()) {
          destroyVideoStream(key);
      }
  }

  public void setPreserve(String videoId) {
      this.preserve = videoId;
  }

    public String getPreserve() { return preserve; }

    //  public void initPlayer(Context context, VideoPlayerDto videoPlayer) {
//    if (isFullScreen && validateViewHolderID(video.getId())) {
//      Log.d("VIDEO_PLAYER_SERVICE", "MINIMISE");
//      playerMinimise(context, view);
//      return;
//    } else if (!isFullScreen) {
//      // TODO : USE NAVIGATION SERVICE TO PASS ID FROM VIEWHOLDER TO FULLSCREENFRAGMENT FOR THIS COMPARISON
//      if (minPlayerView != null && maxPlayerView == null && id == null) {
//        Log.d("VIDEOPLAYERSERVICE", "MAXIMISE - THERE IS ALREADY AN ACTIVE VIDEO RENDER... CASTING THIS TO FULLSCREEN");
//        playerMaximise(context, view);
//        return;
//      } else {
//        if (minPlayerView != null) {
//          Log.d("VIDEOPLAYERSERVICE", "DUPLICATE ATTACH - THERE IS ALREADY AN ACTIVE VIDEO RENDER... DESTROYING CURRENT RENDER");
//          if (currentHolder != null)
//            currentHolder.revealPlayer(false);
//          destroyVideo();
//        }
//        Log.d("VIDEOPLAYERSERVICE", "NEW ATTACH - CREATING VIDEO RENDER");
//        minPlayerView = view;
//        minPlayerID = id;
//        if (player == null) {
//          player = new SimpleExoPlayer.Builder(context).build();
//        }
//
//        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(
//          context,
//          Util.getUserAgent(
//            context,
//            context.getString(R.string.app_name)));
//        mediaSource = new ProgressiveMediaSource.Factory(dataSourceFactory)
//          .createMediaSource(Uri.parse(url));
//
//        minPlayerView.setPlayer(player);
//        enableVideo();
//      }
//    }
//  }

//  private void playerMaximise (Context context, PlayerView view) {
//    maxPlayerView = view;
//    if (minPlayerView != null) {
//      if (player == null) {
//        player = new SimpleExoPlayer.Builder(context).build();
//      }
//      PlayerView.switchTargetView(player, minPlayerView, view);
//    }
//    isFullScreen = true;
//  }
//
//  private void playerMinimise (Context context, PlayerView view) {
//    minPlayerView = view;
//    Log.d("VIDEOPLAYERSERVICE", "PLAYER IS MINIMISING");
//    if (maxPlayerView != null) {
//      Log.d("VIDEOPLAYERSERVICE", "MAX PLAYER FOUND");
//      if (player == null) {
//        player = new SimpleExoPlayer.Builder(context).build();
//      }
//      PlayerView.switchTargetView(player, maxPlayerView, view);
//    }
//    maxPlayerView = null;
//    isFullScreen = false;
//  }
//
//  public void enableVideo() {
//    isVideoEnabled = true;
//    player.prepare(mediaSource);
//  }
//
//  public boolean validateViewHolderID(String id) {
//    return this.minPlayerID.equals(id) ? true : false;
//  }
//
//  public boolean isVideoEnabled() {
//    return isVideoEnabled;
//  }
//
//  public void destroyVideo() {
//    Log.d("VIDEOPLAYERSERVICE", "DESTROYED");
//    isVideoEnabled = false;
//    minPlayerView = null;
//    currentHolder = null;
//    maxPlayerView = null;
//    if (player != null)
//      player.release();
//    player = null;
//  }
//
//  public SimpleExoPlayer getPlayer() {
//    return player;
//  }
//
//  public PlayerView getMaxPlayerView() {
//    return maxPlayerView;
//  }
//
//  public PlayerView getMinPlayerView() {
//    return minPlayerView;
//  }
//
//  public boolean isFullScreen() {
//    return isFullScreen;
//  }
//
//  public void toggleFullscreen() {
//    this.isFullScreen = !isFullScreen;
//  }
}
