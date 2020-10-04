package com.rhdigital.rhclient.activities.rhapp.view;

import android.content.Context;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;

import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rhdigital.rhclient.R;

public class ProgramsViewHolder extends RecyclerView.ViewHolder {
  private ImageView imageView;
  private Button actionButton;

  private Package program;

  private Context context;

  private int imageWidth = 0;
  private int imageHeight = 0;

  public ProgramsViewHolder(@NonNull View itemView, Context context) {
    super(itemView);
    this.context = context;

    imageView = itemView.findViewById(R.id.programs_image);
    actionButton = itemView.findViewById(R.id.programs_action_button);

    // View Tree Management
    imageView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
      @Override
      public void onGlobalLayout() {
        if (imageView.getHeight() > 0 && imageView.getWidth() > 0) {
          imageHeight = imageView.getHeight();
          imageWidth = imageView.getWidth();
          imageView.getViewTreeObserver().removeOnGlobalLayoutListener(this::onGlobalLayout);
        }
      }
    });

  }

  public ImageView getImageView() {
    return imageView;
  }
}
