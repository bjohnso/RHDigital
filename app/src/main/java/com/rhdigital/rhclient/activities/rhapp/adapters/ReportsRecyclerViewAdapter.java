package com.rhdigital.rhclient.activities.rhapp.adapters;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.rhdigital.rhclient.R;
import com.rhdigital.rhclient.activities.rhapp.viewholder.ReportsGroupViewHolder;
import com.rhdigital.rhclient.activities.rhapp.viewholder.ReportsViewHolder;
import com.rhdigital.rhclient.common.ancestors.BaseViewHolder;
import com.rhdigital.rhclient.room.model.Report;
import com.rhdigital.rhclient.databinding.ItemReportsGroupBinding;

import java.util.LinkedHashMap;
import java.util.List;

public class ReportsRecyclerViewAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private static final int REPORTS_GROUP = 0;
    private static final int REPORTS = 1;

    private List<Report> reports;
    private LinkedHashMap<String, List<Report>> reportMap;

    public ReportsRecyclerViewAdapter() {
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = null;
        switch (viewType) {
            case REPORTS:
               return new ReportsViewHolder(inflater.inflate(R.layout.item_reports, parent, false));
            case REPORTS_GROUP:
                return new ReportsGroupViewHolder(ItemReportsGroupBinding.inflate(inflater, parent, false));
        }
        return new BaseViewHolder(view);
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        if (reports != null) {
            holder.bind(reports.get(position));
        } else if (reportMap != null) {
            String key = (String) reportMap.keySet().toArray()[position];
            List<Report> value = (List<Report>) reportMap.values().toArray()[position];
            holder.bind(key, value);
        }
    }

    public void setReports(List<Report> reports) {
        this.reports = reports;
        this.reportMap = null;
        notifyDataSetChanged();
    }

    public void setReportGroups(LinkedHashMap<String, List<Report>> reportMap) {
        this.reportMap = reportMap;
        this.reports = null;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (reports != null) {
            return REPORTS;
        } else if (reportMap != null) {
            return REPORTS_GROUP;
        }
        return -1;
    }

    @Override
    public int getItemCount() {
        if (reports != null) {
            return reports.size();
        } else if (reportMap != null) {
            return reportMap.keySet().size();
        }
        return 0;
    }
}
