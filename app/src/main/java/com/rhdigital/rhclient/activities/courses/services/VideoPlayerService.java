//package com.rhdigital.rhclient.activities.courses.services;
//
//import android.content.Context;
//import android.net.Uri;
//import android.util.Log;
//
//import com.google.android.exoplayer2.SimpleExoPlayer;
//import com.google.android.exoplayer2.source.MediaSource;
//import com.google.android.exoplayer2.source.ProgressiveMediaSource;
//import com.google.android.exoplayer2.ui.PlayerView;
//import com.google.android.exoplayer2.upstream.DataSource;
//import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
//import com.google.android.exoplayer2.util.Util;
//import com.rhdigital.rhclient.R;
//import com.rhdigital.rhclient.activities.courses.view.CoursesViewHolder;
//
//public class VideoPlayerService {
//
//  private static VideoPlayerService INSTANCE;
//  private SimpleExoPlayer player;
//  private MediaSource mediaSource;
//  private boolean isFullScreen = false;
//  private boolean isVideoEnabled = false;
//  private String minPlayerID = "";
//  private PlayerView minPlayerView;
//  private PlayerView maxPlayerView;
//  private CoursesViewHolder currentHolder;
//
//  //TODO : CREATE HOLDER SERVICE
//
//  private VideoPlayerService() {}
//
//  public static VideoPlayerService getInstance() {
//    if (INSTANCE == null) {
//      INSTANCE = new VideoPlayerService();
//    }
//    return INSTANCE;
//  }
//
//  public void initPlayer(Context context, Uri uri, String id, CoursesViewHolder holder, PlayerView view) {
//    if (isFullScreen && validateViewHolderID(id)) {
//      Log.d("VIDEOPLAYERSERVICE", "MINIMISE");
//      currentHolder = holder;
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
//        this.currentHolder = holder;
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
//          .createMediaSource(uri);
//
//        minPlayerView.setPlayer(player);
//        enableVideo();
//      }
//    }
//  }
//
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
//}
