package com.rhdigital.rhclient.activities.rhapp.fragments;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
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
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.rhdigital.rhclient.R;
import com.rhdigital.rhclient.activities.rhapp.RHAppActivity;
import com.rhdigital.rhclient.activities.rhapp.adapters.ReportsRecyclerViewAdapter;
import com.rhdigital.rhclient.activities.rhapp.viewmodel.RHAppViewModel;
import com.rhdigital.rhclient.activities.rhapp.viewmodel.ReportsViewModel;
import com.rhdigital.rhclient.common.dto.VideoControlActionDto;
import com.rhdigital.rhclient.common.interfaces.OnClickCallback;
import com.rhdigital.rhclient.common.services.NavigationService;
import com.rhdigital.rhclient.room.model.Report;
import com.rhdigital.rhclient.databinding.FragmentReportsBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class ReportsFragment extends Fragment {

    private final String TAG = "REPORTS_FRAGMENT";

    private RHAppActivity activity;

    // VIEW
    private FragmentReportsBinding binding;

    // VIEW MODEL
    private ReportsViewModel reportsViewModel;
    private RHAppViewModel rhAppViewModel;

    // ADAPTERS
    private ReportsRecyclerViewAdapter reportsRecyclerViewAdapter;

    // OBSERVABLES
    private LiveData<List<Report>> reportsObservable;
    private LiveData<HashMap<String, Uri>> reportsUriObservable;

    // OBSERVERS
    private Observer<List<Report>> reportsObserver;
    private Observer<HashMap<String, Uri>> reportsUriObserver;

    private int width = 0;
    private int height = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentReportsBinding.inflate(getLayoutInflater());

        rhAppViewModel = new ViewModelProvider(getActivity()).get(RHAppViewModel.class);
        reportsViewModel = new ReportsViewModel(getActivity().getApplication());
        reportsViewModel.init();
        binding.setViewModel(reportsViewModel);

        initialiseLiveData();
        initialiseUI();

        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        activity = (RHAppActivity) getActivity();
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
                    reportsUriObserver = new Observer<HashMap<String, Uri>>() {
                        @Override
                        public void onChanged(HashMap<String, Uri> uriMap) {
                            if (uriMap != null) {
                                reportsUriObservable.removeObserver(this);
                                onUpdateReports(reports, uriMap);
                            }
                        }
                    };
                    reportsUriObservable = reportsViewModel.getReportUri(reports);
                    reportsUriObservable.observe(getViewLifecycleOwner(), reportsUriObserver);
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
    private void onUpdateReports(List<Report> reports, HashMap<String, Uri> uriMap) {
        OnClickCallback callback = (args) -> {
            String url = (args[1]).toString();
            reportsViewModel.downloadFile(url)
                    .observe(getViewLifecycleOwner(), res -> {
                        if (res != null) {
                            Report report = (Report)args[0];
                            activity.writeFileToDisk(report.getTitle(), res);
                        } else{
                            Toast.makeText(getContext(), "Download Failed", Toast.LENGTH_LONG).show();
                        }
                    });
        };

        LinkedHashMap<String, List<Report>> reportGroupings = new LinkedHashMap<>();
        for (Report report: reports) {
            ArrayList<Report> grouping = (ArrayList<Report>) reportGroupings.getOrDefault(report.getMonth(), new ArrayList<>());
            grouping.add(report);
            reportGroupings.put(report.getMonth(), grouping);
        }
        reportsRecyclerViewAdapter.setReportGroups(reportGroupings, uriMap, callback);
    }
}
