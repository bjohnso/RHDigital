package com.rhdigital.rhclient.common.ancestors;

import android.graphics.Bitmap;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rhdigital.rhclient.database.model.Course;
import com.rhdigital.rhclient.database.model.CourseDescription;
import com.rhdigital.rhclient.database.model.Program;
import com.rhdigital.rhclient.database.model.Workbook;

public class BaseViewHolder extends RecyclerView.ViewHolder {

    public BaseViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    public void bind(Program program, Bitmap bitmap) {}

    public void bind(Program program, Course course, Bitmap bitmap) {}

    public void bind(Course course) {}

    public void bind(CourseDescription courseDescription) {}

    public void bind(Workbook workbook) {}
}
