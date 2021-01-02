package com.rhdigital.rhclient.activities.rhapp.viewholder;

import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.rhdigital.rhclient.R;
import com.rhdigital.rhclient.database.model.Course;

public class CoursesViewHolder extends RecyclerView.ViewHolder {

    private ImageView imageView;
    private Button actionButton;

    private Course course;
    private Bitmap bitmap;

    private int imageWidth = 0;
    private int imageHeight = 0;

    public CoursesViewHolder(@NonNull View itemView) {
        super(itemView);

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

    public void bind(Course course, Bitmap bitmap) {
        this.course = course;
        this.bitmap = bitmap;

        imageView.setImageBitmap(bitmap);
    }
}
