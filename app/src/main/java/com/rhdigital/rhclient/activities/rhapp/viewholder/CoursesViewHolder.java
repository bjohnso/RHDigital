package com.rhdigital.rhclient.activities.rhapp.viewholder;

import android.app.Application;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.rhdigital.rhclient.R;
import com.rhdigital.rhclient.activities.rhapp.adapters.CourseItemRecyclerViewAdapter;
import com.rhdigital.rhclient.activities.rhapp.viewmodel.CourseItemViewModel;
import com.rhdigital.rhclient.common.ancestors.BaseViewHolder;
import com.rhdigital.rhclient.database.model.Course;
import com.rhdigital.rhclient.database.model.CourseDescription;
import com.rhdigital.rhclient.database.model.Program;
import com.rhdigital.rhclient.database.model.Workbook;
import com.rhdigital.rhclient.databinding.ItemCoursesBinding;

import java.util.List;
import java.util.Observable;

public class CoursesViewHolder extends BaseViewHolder {

    private ItemCoursesBinding binding;
    private ImageView imageView;
    private RecyclerView recyclerView;
    private TextView tvProgramTitle;
    private TextView tvCourseTitle;
    private TextView tvAuthor;

    private CourseItemRecyclerViewAdapter courseItemRecyclerViewAdapter;
    private Observer<List<Workbook>> workbooksObserver;
    private LiveData<List<Workbook>> workbooksObservable;

    private Observer<List<CourseDescription>> descriptionsObserver;
    private LiveData<List<CourseDescription>> descriptionsObservable;

    // VIEW MODEL
    private CourseItemViewModel courseItemViewModel;

    private Program program;
    private Course course;
    private Bitmap bitmap;

    private int imageWidth = 0;
    private int imageHeight = 0;

    public CoursesViewHolder(@NonNull ItemCoursesBinding binding) {
        super(binding.getRoot());

        this.binding = binding;

        courseItemViewModel = new CourseItemViewModel((Application) binding.getRoot().getContext().getApplicationContext());

        imageView = binding.ivImage;
        tvProgramTitle = binding.tvProgramTitle;
        tvCourseTitle = binding.tvCourseTitle;
        tvAuthor = binding.tvAuthor;

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

        binding.setCourse(course);

        if (course.isAuthorised()) {
            recyclerView = binding.viewWorkbooks.recyclerView;
        } else {
            recyclerView = binding.viewCourseDescriptions.recyclerView;
        }

        courseItemViewModel.init(course.getId())
                .observe((LifecycleOwner) itemView.getContext(), complete -> {
                    if (complete) {
                        if (course.isAuthorised()) {
                            initialiseWorkbooksLiveData();
                        } else {
                            initialiseDescriptionsLiveData();
                        }
                    }
                });

        imageView.setImageBitmap(bitmap);
        tvProgramTitle.setText(program.getTitle());
        tvCourseTitle.setText(course.getTitle());
        tvAuthor.setText(course.getAuthor());
        initialiseRecyclerView();
    }

    private void initialiseWorkbooksLiveData() {
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

        workbooksObservable = courseItemViewModel.getWorkbooks();
        workbooksObservable.observe((LifecycleOwner) itemView.getContext(), workbooksObserver);
    }

    private void initialiseDescriptionsLiveData() {
        descriptionsObserver = new Observer<List<CourseDescription>>() {
            @Override
            public void onChanged(List<CourseDescription> descriptions) {
                if (descriptions != null) {
                    descriptionsObservable.removeObserver(this);
                    onUpdateCourseDescriptions(descriptions);
                } else {
                    Toast.makeText(itemView.getContext(), R.string.server_error_courses, Toast.LENGTH_LONG).show();
                }
            }
        };

        descriptionsObservable = courseItemViewModel.getCourseDescriptions();
        descriptionsObservable.observe((LifecycleOwner) itemView.getContext(), descriptionsObserver);
    }

    private void initialiseRecyclerView() {
        courseItemRecyclerViewAdapter = new CourseItemRecyclerViewAdapter();
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(10);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_AUTO);
        recyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
        recyclerView.setAdapter(courseItemRecyclerViewAdapter);
    }

    private void onUpdateWorkbooks(List<Workbook> workbooks) {
        courseItemRecyclerViewAdapter.setWorkbooks(workbooks);
    }

    private void onUpdateCourseDescriptions(List<CourseDescription> descriptions) {
        courseItemRecyclerViewAdapter.setCourseDescriptions(descriptions);
    }
}
