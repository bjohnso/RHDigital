package com.rhdigital.rhclient.activities.courses.view;

import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rhdigital.rhclient.R;

import java.util.ArrayList;
import java.util.List;

public class WorkbooksViewHolder extends RecyclerView.ViewHolder {
  private ImageView imageView;
  private TextView headerView;
  private List<View> workbookButtons;

  public WorkbooksViewHolder(@NonNull ViewGroup itemView) {
    super(itemView);
    imageView = itemView.findViewById(R.id.workbooks_image_item);
    headerView = itemView.findViewById(R.id.workbooks_text_header_item);
  }

  public void addWorkbookButton(View workbookButton, Uri uri) {
    if (this.workbookButtons == null) {
      this.workbookButtons = new ArrayList<>();
    }
    workbookButton.setOnClickListener(new WorkbookOnClick(uri));
    this.workbookButtons.add(workbookButton);
  }

  public ImageView getImageView() {
    return imageView;
  }

  public TextView getHeaderView() {
    return headerView;
  }

  public static class WorkbookOnClick implements View.OnClickListener {

    private Uri uri;

    public WorkbookOnClick(Uri uri) {
      this.uri = uri;
    }

    @Override
    public void onClick(View view) {
      Log.d("WORKBOOK", uri.getPath());
    }
  }
}
