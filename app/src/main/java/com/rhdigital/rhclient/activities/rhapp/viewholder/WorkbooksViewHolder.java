package com.rhdigital.rhclient.activities.rhapp.viewholder;

import android.net.Uri;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.rhdigital.rhclient.R;
import com.rhdigital.rhclient.common.ancestors.BaseViewHolder;
import com.rhdigital.rhclient.common.interfaces.OnClickCallback;
import com.rhdigital.rhclient.room.model.Workbook;

public class WorkbooksViewHolder extends BaseViewHolder {

    private TextView tvTitle;
    private ConstraintLayout container;

    private Workbook workbook;
    private Uri uri;

    public WorkbooksViewHolder(@NonNull View itemView) {
        super(itemView);
        tvTitle = itemView.findViewById(R.id.tvTitle);
        container = itemView.findViewById(R.id.clContainer);
    }

    public void bind(Workbook workbook, Uri uri, OnClickCallback onClickCallback) {
        this.workbook = workbook;
        this.uri = uri;
        this.tvTitle.setText(workbook.getTitle());
        container.setOnClickListener(view -> onClickCallback.invoke(workbook, uri));
    }
}
