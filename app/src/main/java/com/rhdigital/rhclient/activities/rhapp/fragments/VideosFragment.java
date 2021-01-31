package com.rhdigital.rhclient.activities.rhapp.fragments;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.rhdigital.rhclient.R;
import com.rhdigital.rhclient.activities.rhapp.viewmodel.RHAppViewModel;
import com.rhdigital.rhclient.common.services.NavigationService;
import com.rhdigital.rhclient.common.services.VideoPlayerService;
import com.rhdigital.rhclient.room.model.Video;
import com.rhdigital.rhclient.databinding.FragmentVideosBinding;


public class VideosFragment extends Fragment {

    private final String TAG = "VIDEO_FRAGMENT";

    private RHAppViewModel rhAppViewModel;

    // VIDEO CONTROLS
    private ImageButton minimiseButton;

    // VIEW
    private FragmentVideosBinding binding;

    private Video video;

    private int width = 0;
    private int height = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = FragmentVideosBinding.inflate(getLayoutInflater());
        rhAppViewModel = new ViewModelProvider(getActivity()).get(RHAppViewModel.class);
        initialiseUI();
        if (getArguments() != null && getArguments().getParcelable("videoData") != null) {
            this.video = getArguments().getParcelable("videoData");
            VideoPlayerService.getInstance().resumeVideoStream(getContext(), video.getId(), binding.videoPlayer);
            initialiseVideoControls();
        } else {
            exit();
        }
        return binding.getRoot();
    }

    private void initialiseUI() {
        rhAppViewModel.isFullscreenMode.postValue(true);
        calculateImageDimensions();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        RHAppViewModel rhAppViewModel = new ViewModelProvider(getActivity()).get(RHAppViewModel.class);
        rhAppViewModel.isFullscreenMode.postValue(false);
    }

    private void calculateImageDimensions() {
        float dip = 220f;
        Resources r = getResources();
        float px = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dip,
                r.getDisplayMetrics()
        );

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        width = displayMetrics.widthPixels;
        height = Math.round(px);
    }

    private void initialiseVideoControls() {
        binding.getRoot().findViewById(R.id.exo_full).setVisibility(View.GONE);
        minimiseButton = binding.getRoot().findViewById(R.id.exo_full_exit);
        minimiseButton.setOnClickListener(view -> {
            VideoPlayerService.getInstance().setPreserve(video.getId());
            exit();
        });
    }

    private void exit() {
        NavigationService.getINSTANCE().navigateBack(getActivity().getLocalClassName());
    }
}
