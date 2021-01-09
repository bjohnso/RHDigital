package com.rhdigital.rhclient.activities.rhapp.fragments;

import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.rhdigital.rhclient.R;
import com.rhdigital.rhclient.activities.rhapp.adapters.ReportsRecyclerViewAdapter;
import com.rhdigital.rhclient.activities.rhapp.viewmodel.ReportsViewModel;
import com.rhdigital.rhclient.database.model.Report;
import com.rhdigital.rhclient.databinding.FragmentReportsBinding;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class ReportsFragment extends Fragment {

    private final String TAG = "REPORTS_FRAGMENT";

    // VIEW
    private FragmentReportsBinding binding;

    // VIEW MODEL
    private ReportsViewModel reportsViewModel;

    // ADAPTERS
    private ReportsRecyclerViewAdapter reportsRecyclerViewAdapter;

    // OBSERVABLES
    private LiveData<List<Report>> reportsObservable;

    // OBSERVERS
    private Observer<List<Report>> reportsObserver;

    private int width = 0;
    private int height = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentReportsBinding.inflate(getLayoutInflater());

        reportsViewModel = new ReportsViewModel(getActivity().getApplication());
        reportsViewModel.init();
        binding.setViewModel(reportsViewModel);

        initialiseLiveData();
        initialiseUI();

        return binding.getRoot();
    }

    private void initialiseUI() {
        calculateImageDimensions();
        initialiseRecyclerView();
    }

    private void initialiseLiveData() {
        reportsRecyclerViewAdapter = new ReportsRecyclerViewAdapter();
        reportsObserver = new Observer<List<Report>>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onChanged(List<Report> reports) {
                if (reports != null) {
                    reportsObservable.removeObserver(this);
                    onUpdateReports(reports);
                } else {
                    Toast.makeText(getContext(), R.string.server_error_reports, Toast.LENGTH_LONG).show();
                }
            }
        };

        reportsObservable = reportsViewModel.getReports();
        reportsObservable.observe(getViewLifecycleOwner(), reportsObserver);
    }

    private void initialiseRecyclerView() {
        binding.reportsRecycler.setHasFixedSize(true);
        binding.reportsRecycler.setItemViewCacheSize(10);
        binding.reportsRecycler.setDrawingCacheEnabled(true);
        binding.reportsRecycler.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_AUTO);
        binding.reportsRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.reportsRecycler.setAdapter(reportsRecyclerViewAdapter);
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void onUpdateReports(List<Report> reports) {
        LinkedHashMap<String, List<Report>> reportGroupings = new LinkedHashMap<>();
        for (Report report: reports) {
            ArrayList<Report> grouping = (ArrayList<Report>) reportGroupings.getOrDefault(report.getMonth(), new ArrayList<>());
            grouping.add(report);
            reportGroupings.put(report.getMonth(), grouping);
        }
        reportsRecyclerViewAdapter.setReportGroups(reportGroupings);
    }
}
