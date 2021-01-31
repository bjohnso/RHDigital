package com.rhdigital.rhclient.activities.rhapp.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.rhdigital.rhclient.R;
import com.rhdigital.rhclient.common.ancestors.BaseViewHolder;
import com.rhdigital.rhclient.room.model.Report;

public class ReportsViewHolder extends BaseViewHolder {

    private Report report;
    private TextView tvTitle;

    public ReportsViewHolder(@NonNull View itemView) {
        super(itemView);
        tvTitle = itemView.findViewById(R.id.tvTitle);
    }

    @Override
    public void bind(Report report) {
        super.bind(report);
        this.report = report;
        tvTitle.setText(report.getTitle());
    }
}
