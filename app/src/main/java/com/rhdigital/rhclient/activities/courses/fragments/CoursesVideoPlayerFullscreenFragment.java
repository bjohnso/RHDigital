package com.rhdigital.rhclient.activities.courses.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import com.google.android.exoplayer2.ui.PlayerView;
import com.rhdigital.rhclient.R;
import com.rhdigital.rhclient.activities.courses.listeners.FullscreenToggleOnClick;
import com.rhdigital.rhclient.activities.courses.services.VideoPlayerService;
import com.rhdigital.rhclient.common.services.NavigationService;

public class CoursesVideoPlayerFullscreenFragment extends Fragment {

  private PlayerView playerView;
  private ImageButton backButton;
  private ImageButton maximiseButton;
  private ImageButton minimiseButton;

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

    VideoPlayerService.getInstance().initPlayer(getContext(), null, null, playerView);
    return view;
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
      NavController navController = Navigation.findNavController(view);

    NavigationService.getINSTANCE().addNav(getClass().getName(), navController);
    // Set Listeners
    minimiseButton.setOnClickListener(new FullscreenToggleOnClick(getContext(), getClass().getName(), null));
  }
}
