package com.rhdigital.rhclient.activities.rhapp.viewholder;

import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.rhdigital.rhclient.R;
import com.rhdigital.rhclient.common.ancestors.BaseViewHolder;
import com.rhdigital.rhclient.common.interfaces.OnClickCallback;
import com.rhdigital.rhclient.room.model.Workbook;

public class WorkbooksViewHolder extends BaseViewHolder {

    private TextView tvTitle;
    private ImageButton actionButton;

    private Workbook workbook;
    private Uri uri;

    public WorkbooksViewHolder(@NonNull View itemView) {
        super(itemView);
        tvTitle = itemView.findViewById(R.id.tvTitle);
        actionButton = itemView.findViewById(R.id.imageButton);
    }

    public void bind(Workbook workbook, Uri uri, OnClickCallback onClickCallback) {
        this.workbook = workbook;
        this.uri = uri;
        this.tvTitle.setText(workbook.getTitle());
        actionButton.setOnClickListener(view -> onClickCallback.invoke(workbook, uri));
    }
}
