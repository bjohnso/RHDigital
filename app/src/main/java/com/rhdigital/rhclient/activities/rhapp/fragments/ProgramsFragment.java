package com.rhdigital.rhclient.activities.rhapp.fragments;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rhdigital.rhclient.R;

public class ProgramsFragment extends Fragment {

    //Components
    private RecyclerView recyclerView;

    private int width = 0;
    private int height = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_programs, container, false);
        recyclerView = view.findViewById(R.id.programs_recycler);

        calculateImageDimensions();


        // Initialise RecyclerView
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setItemViewCacheSize(10);
//        recyclerView.setDrawingCacheEnabled(true);
//        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_AUTO);
//        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        return view;
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
}




