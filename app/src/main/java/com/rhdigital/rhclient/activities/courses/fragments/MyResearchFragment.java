package com.rhdigital.rhclient.activities.courses.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.rhdigital.rhclient.R;
import com.rhdigital.rhclient.activities.courses.CoursesActivity;

public class MyResearchFragment extends Fragment {

    //Components
    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.my_research_layout, container, false);

        ((CoursesActivity)getActivity()).setToolbarTitle("Research");


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
