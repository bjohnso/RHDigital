package com.rhdigital.rhclient.activities.rhapp.viewholder;

import android.net.Uri;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.rhdigital.rhclient.R;
import com.rhdigital.rhclient.common.ancestors.BaseViewHolder;
import com.rhdigital.rhclient.common.interfaces.OnClickCallback;
import com.rhdigital.rhclient.room.model.Report;

public class ReportsViewHolder extends BaseViewHolder {

    private Report report;
    private TextView tvTitle;
    private ImageButton imageButton;
    private OnClickCallback onClickCallback;
    private Uri uri;

    public ReportsViewHolder(@NonNull View itemView) {
        super(itemView);
        tvTitle = itemView.findViewById(R.id.tvTitle);
        imageButton = itemView.findViewById(R.id.imageButton);
    }

    @Override
    public void bind(Report report, Uri uri, OnClickCallback onClickCallback) {
        super.bind(report, uri, onClickCallback);
        this.report = report;
        this.uri = uri;
        this.onClickCallback = onClickCallback;
        tvTitle.setText(report.getTitle());
        imageButton.setOnClickListener(view -> onClickCallback.invoke(report, uri));
    }
}
