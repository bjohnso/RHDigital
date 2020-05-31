package com.rhdigital.rhclient.activities.courses.view;

import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rhdigital.rhclient.R;

public class WorkbooksViewHolder extends RecyclerView.ViewHolder {
  private ImageView imageView;
  private TextView headerView;

  public WorkbooksViewHolder(@NonNull ViewGroup itemView) {
    super(itemView);
    imageView = itemView.findViewById(R.id.workbooks_image_item);
    headerView = itemView.findViewById(R.id.workbooks_text_header_item);
  }

  public ImageView getImageView() {
    return imageView;
  }

  public TextView getHeaderView() {
    return headerView;
  }
}
