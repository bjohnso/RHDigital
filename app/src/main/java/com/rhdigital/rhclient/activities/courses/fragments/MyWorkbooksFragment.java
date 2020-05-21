package com.rhdigital.rhclient.activities.courses.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rhdigital.rhclient.R;
import com.rhdigital.rhclient.database.model.CourseWithWorkbooks;
import com.rhdigital.rhclient.database.viewmodel.WorkbookViewModel;
import com.rhdigital.rhclient.activities.courses.adapters.WorkbooksRecyclerViewAdapter;

import java.util.List;

public class MyWorkbooksFragment extends Fragment {

    //View Model
    private WorkbookViewModel workbookViewModel;

    //Adapters
    private WorkbooksRecyclerViewAdapter workbooksRecyclerViewAdapter;

    //Components
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_workbooks_layout, container, false);
        recyclerView = view.findViewById(R.id.workbooks_recycler_view);

        //Initialise View Model
        workbookViewModel = new WorkbookViewModel(getActivity().getApplication());

        //Initialise Adapter
        workbooksRecyclerViewAdapter = new WorkbooksRecyclerViewAdapter();

        //Create an Observer
        final Observer<List<CourseWithWorkbooks>> workbookObserver = courseWithWorkbooks -> workbooksRecyclerViewAdapter.setWorkbooks(courseWithWorkbooks);

        // Initialise RecyclerView
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(workbooksRecyclerViewAdapter);

        //Call Observer
        workbookViewModel.getAllCoursesWithWorkbooks().observe(getActivity(), workbookObserver);

        return view;
    }

}
