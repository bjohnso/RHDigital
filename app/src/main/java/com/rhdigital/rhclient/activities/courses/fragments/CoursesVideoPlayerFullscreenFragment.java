package com.rhdigital.rhclient.activities.courses.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.rhdigital.rhclient.R;
import com.rhdigital.rhclient.activities.courses.CoursesActivity;
import com.rhdigital.rhclient.activities.courses.listeners.VideoPlayerFullScreenToggle;
import com.rhdigital.rhclient.activities.courses.services.VideoPlayerService;
import com.rhdigital.rhclient.common.video.VideoView;

public class CoursesVideoPlayerFullscreenFragment extends Fragment {

  private PlayerView playerView;
  private ImageButton backButton;
  private ImageButton maximiseButton;
  private ImageButton minimiseButton;

  @Override
  public void onAttach(@NonNull Context context) {
    super.onAttach(context);
    ((CoursesActivity)getActivity()).revealVideoPlayerFullscreen(true);
    ((CoursesActivity)getActivity()).configureScreenOrientation(true);
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.video_player_fullscreen, container, false);
    playerView = view.findViewById(R.id.video_player);

    backButton = view.findViewById(R.id.exo_exit);
    minimiseButton = view.findViewById(R.id.exo_full_exit);
    maximiseButton = view.findViewById(R.id.exo_full);

    maximiseButton.setVisibility(View.GONE);
    backButton.setVisibility(View.GONE);

    // Set Listeners
    minimiseButton.setOnClickListener(new VideoPlayerFullScreenToggle(getContext()));

    VideoPlayerService.getInstance().initPlayer(getContext(), null, null, playerView);
    return view;
  }

  @Override
  public void onDetach() {
    ((CoursesActivity)getActivity()).configureScreenOrientation(false);
    ((CoursesActivity)getActivity()).revealVideoPlayerFullscreen(false);
    super.onDetach();
  }
}
