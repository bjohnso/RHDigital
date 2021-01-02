package com.rhdigital.rhclient.activities.rhapp.viewholder;

import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rhdigital.rhclient.R;
import com.rhdigital.rhclient.common.interfaces.OnClickCallback;
import com.rhdigital.rhclient.database.model.Program;

public class ProgramsViewHolder extends RecyclerView.ViewHolder {
  private ImageView imageView;
  private Button actionButton;

  private Program program;
  private Bitmap bitmap;

  private int imageWidth = 0;
  private int imageHeight = 0;

  public ProgramsViewHolder(@NonNull View itemView, OnClickCallback callback) {
    super(itemView);
    imageView = itemView.findViewById(R.id.programs_image);
    actionButton = itemView.findViewById(R.id.programs_action_button);

    actionButton.setOnClickListener(view -> {
      callback.invoke(program);
    });

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

  public void bind(Program program, Bitmap bitmap) {
    this.program = program;
    this.bitmap = bitmap;

    imageView.setImageBitmap(bitmap);
  }
}
