package com.rhdigital.rhclient.activities.rhapp.viewholder;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.rhdigital.rhclient.R;
import com.rhdigital.rhclient.common.ancestors.BaseViewHolder;
import com.rhdigital.rhclient.room.model.Workbook;

public class WorkbooksViewHolder extends BaseViewHolder {

    private TextView tvTitle;
    private Button actionButton;

    private Workbook workbook;

    public WorkbooksViewHolder(@NonNull View itemView) {
        super(itemView);
        tvTitle = itemView.findViewById(R.id.tvTitle);
    }

    public void bind(Workbook workbook) {
        this.workbook = workbook;
        this.tvTitle.setText(workbook.getTitle());
    }
}
