package com.rhdigital.rhclient.activities.rhapp.viewholder;

import android.app.Application;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.rhdigital.rhclient.R;
import com.rhdigital.rhclient.activities.rhapp.adapters.WorkbooksRecyclerViewAdapter;
import com.rhdigital.rhclient.activities.rhapp.viewmodel.WorkbooksViewModel;
import com.rhdigital.rhclient.database.model.Course;
import com.rhdigital.rhclient.database.model.Program;
import com.rhdigital.rhclient.database.model.Workbook;

import java.util.List;

public class CoursesViewHolder extends RecyclerView.ViewHolder {

    private ImageView imageView;
    private RecyclerView recyclerView;
    private Button actionButton;
    private TextView tvProgramTitle;
    private TextView tvCourseTitle;
    private TextView tvAuthor;

    private WorkbooksRecyclerViewAdapter workbooksRecyclerViewAdapter;
    private Observer<List<Workbook>> workbooksObserver;
    private LiveData<List<Workbook>> workbooksObservable;

    // VIEW MODEL
    private WorkbooksViewModel workbooksViewModel;

    private Program program;
    private Course course;
    private Bitmap bitmap;

    private int imageWidth = 0;
    private int imageHeight = 0;

    public CoursesViewHolder(@NonNull View itemView) {
        super(itemView);

        workbooksViewModel = new WorkbooksViewModel((Application) itemView.getContext().getApplicationContext());

        imageView = itemView.findViewById(R.id.ivImage);
        recyclerView = itemView.findViewById(R.id.rvWorkbooks);
        actionButton = itemView.findViewById(R.id.programs_action_button);

        tvProgramTitle = itemView.findViewById(R.id.tvProgramTitle);
        tvCourseTitle = itemView.findViewById(R.id.tvCourseTitle);
        tvAuthor = itemView.findViewById(R.id.tvAuthor);

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

    public void bind(@NonNull Program program, @NonNull Course course, Bitmap bitmap) {
        this.program = program;
        this.course = course;
        this.bitmap = bitmap;

        workbooksViewModel.init(course.getId())
                .observe((LifecycleOwner) itemView.getContext(), complete -> {
                    if (complete) {
                        Log.d("WORKBOOKS", "COMPLETE");
                        initialiseLiveData();
                    }
                });

        imageView.setImageBitmap(bitmap);
        tvProgramTitle.setText(program.getTitle());
        tvCourseTitle.setText(course.getTitle());
        tvAuthor.setText(course.getAuthor());
        initialiseRecyclerView();
    }

    private void initialiseLiveData() {
        workbooksObserver = new Observer<List<Workbook>>() {
            @Override
            public void onChanged(List<Workbook> workbooks) {
                if (workbooks != null) {
                    workbooksObservable.removeObserver(this);
                    onUpdateWorkbooks(workbooks);
                } else {
                    Toast.makeText(itemView.getContext(), R.string.server_error_workbooks, Toast.LENGTH_LONG).show();
                }
            }
        };

        workbooksObservable = workbooksViewModel.getWorkbooks();
        workbooksObservable.observe((LifecycleOwner) itemView.getContext(), workbooksObserver);
    }

    private void initialiseRecyclerView() {
        workbooksRecyclerViewAdapter = new WorkbooksRecyclerViewAdapter();
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(10);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_AUTO);
        recyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
        recyclerView.setAdapter(workbooksRecyclerViewAdapter);
    }

    private void onUpdateWorkbooks(List<Workbook> workbooks) {
        workbooksRecyclerViewAdapter.setWorkbooks(workbooks);
    }
}
