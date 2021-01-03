package com.rhdigital.rhclient.activities.rhapp.viewholder;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rhdigital.rhclient.R;
import com.rhdigital.rhclient.database.model.Workbook;

public class WorkbooksViewHolder extends RecyclerView.ViewHolder {

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
