package com.rhdigital.rhclient.activities.video;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.google.android.exoplayer2.ui.PlayerView;
import com.rhdigital.rhclient.R;

public class VideoActivity extends Activity {

    private PlayerView playerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        playerView = findViewById(R.id.video_player);
    }
}
