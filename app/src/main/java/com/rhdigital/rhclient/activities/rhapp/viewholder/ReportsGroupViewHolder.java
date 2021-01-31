package com.rhdigital.rhclient.activities.rhapp.viewholder;

import android.net.Uri;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.rhdigital.rhclient.activities.rhapp.adapters.ReportsRecyclerViewAdapter;
import com.rhdigital.rhclient.common.ancestors.BaseViewHolder;
import com.rhdigital.rhclient.common.interfaces.OnClickCallback;
import com.rhdigital.rhclient.room.model.Report;
import com.rhdigital.rhclient.databinding.ItemReportsGroupBinding;

import java.util.HashMap;
import java.util.List;

public class ReportsGroupViewHolder extends BaseViewHolder {

    private ItemReportsGroupBinding binding;
    private ReportsRecyclerViewAdapter reportsRecyclerViewAdapter;
    private OnClickCallback onClickCallback;
    private List<Report> reports;
    private HashMap<String, Uri> uriMap;

    public ReportsGroupViewHolder(ItemReportsGroupBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public void bind(String reportGroup, List<Report> reports, HashMap<String, Uri> uriMap, OnClickCallback onClickCallback) {
        this.reports = reports;
        this.uriMap = uriMap;
        this.onClickCallback = onClickCallback;
        this.binding.tvTitle.setText(reportGroup);
        initialiseRecyclerView();
        this.binding.icAction.setOnClickListener(view -> {
            Boolean isReportsCollapsed = binding.getReportsCollapsed();
            binding.setReportsCollapsed(!isReportsCollapsed);
            binding.notifyPropertyChanged(binding.recyclerView.getId());
        });
    }

    private void initialiseRecyclerView() {
        reportsRecyclerViewAdapter = new ReportsRecyclerViewAdapter();
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setItemViewCacheSize(10);
        binding.recyclerView.setDrawingCacheEnabled(true);
        binding.recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_AUTO);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
        binding.recyclerView.setAdapter(reportsRecyclerViewAdapter);
        reportsRecyclerViewAdapter.setReports(reports, uriMap, onClickCallback);
    }
}
