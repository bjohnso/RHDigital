package com.rhdigital.rhclient.activities.rhapp.viewholder;

import android.app.Application;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.exoplayer2.ui.PlayerView;
import com.rhdigital.rhclient.R;
import com.rhdigital.rhclient.activities.rhapp.adapters.CourseItemRecyclerViewAdapter;
import com.rhdigital.rhclient.activities.rhapp.viewmodel.CourseItemViewModel;
import com.rhdigital.rhclient.common.ancestors.PlayerViewHolder;
import com.rhdigital.rhclient.common.dto.VideoControlActionDto;
import com.rhdigital.rhclient.common.dto.VideoPlayerDto;
import com.rhdigital.rhclient.common.interfaces.OnClickCallback;
import com.rhdigital.rhclient.common.services.VideoPlayerService;
import com.rhdigital.rhclient.room.model.Course;
import com.rhdigital.rhclient.room.model.CourseDescription;
import com.rhdigital.rhclient.room.model.Program;
import com.rhdigital.rhclient.room.model.Video;
import com.rhdigital.rhclient.room.model.Workbook;
import com.rhdigital.rhclient.databinding.ItemCoursesBinding;

import java.util.HashMap;
import java.util.List;

public class CoursesViewHolder extends PlayerViewHolder {

    private ItemCoursesBinding binding;
    private PlayerView videoPlayer;
    private ImageView imageView;
    private RecyclerView recyclerView;
    private TextView tvProgramTitle;
    private TextView tvCourseTitle;
    private TextView tvAuthor;

    // VIDEO CONTROLS
    private ImageButton maximiseButton;

    private CourseItemRecyclerViewAdapter courseItemRecyclerViewAdapter;

    private Observer<List<Workbook>> workbooksObserver;
    private LiveData<List<Workbook>> workbooksObservable;

    private Observer<HashMap<String, Uri>> workbookUriMapObserver;
    private LiveData<HashMap<String, Uri>> workbookUriMapObservable;

    private Observer<Video> videoObserver;
    private LiveData<Video> videoObservable;

    private Observer<List<CourseDescription>> descriptionsObserver;
    private LiveData<List<CourseDescription>> descriptionsObservable;

    // VIEW MODEL
    private CourseItemViewModel courseItemViewModel;

    private VideoPlayerService videoPlayerService;

    private OnClickCallback videoCallback;
    private OnClickCallback workbookCallback;

    private Course course;
    private Video video;

    private int imageWidth = 0;
    private int imageHeight = 0;

    public CoursesViewHolder(@NonNull ItemCoursesBinding binding) {
        super(binding.getRoot());

        this.binding = binding;

        courseItemViewModel = new CourseItemViewModel((Application) binding.getRoot().getContext().getApplicationContext());
        videoPlayerService = VideoPlayerService.getInstance();

        imageView = binding.ivImage;
        videoPlayer = binding.videoPlayer;
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

    public void bind(@NonNull Program program,
                     @NonNull Course course, Bitmap bitmap,
                     @NonNull OnClickCallback videoCallback,
                     @NonNull OnClickCallback workbookCallback) {
        this.course = course;
        this.videoCallback = videoCallback;
        this.workbookCallback = workbookCallback;

        binding.setCourse(course);

        if (course.isAuthorised()) {
            recyclerView = binding.viewWorkbooks.recyclerView;
            initialiseWorkbooksControls();
            initialiseVideoControls();
        } else {
            recyclerView = binding.viewCourseDescriptions.recyclerView;
        }

        courseItemViewModel.init(course.getId())
                .observe((LifecycleOwner) itemView.getContext(), complete -> {
                    if (complete) {
                        if (course.isAuthorised()) {
                            initialiseWorkbooksLiveData();
                            initialiseVideoLiveData();
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

    public void destroy() {
        if (video != null) {
            VideoPlayerService.getInstance().destroyVideoStream(video.getId());
        }
    }

    private void initialiseWorkbooksLiveData() {
        workbooksObserver = new Observer<List<Workbook>>() {
            @Override
            public void onChanged(List<Workbook> workbooks) {
                if (workbooks != null) {
                    workbooksObservable.removeObserver(this);
                    courseItemViewModel.workbooks = workbooks;

                    workbookUriMapObservable = courseItemViewModel.getWorkBookUriMap();
                    workbookUriMapObservable.observe((LifecycleOwner) itemView.getContext(), workbookUriMapObserver);
                } else {
                    Toast.makeText(itemView.getContext(), R.string.server_error_workbooks, Toast.LENGTH_LONG).show();
                }
            }
        };

        workbookUriMapObserver = new Observer<HashMap<String, Uri>>() {
            @Override
            public void onChanged(HashMap<String, Uri> uriHashMap) {
                if (uriHashMap != null) {
                    workbookUriMapObservable.removeObserver(this);
                    onUpdateWorkbooks(courseItemViewModel.workbooks, uriHashMap);
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

    private void initialiseVideoLiveData() {
        videoObserver = new Observer<Video>() {
            @Override
            public void onChanged(Video v) {
                if (v != null) {
                    videoObservable.removeObserver(this);
                    video = v;
                    initVideoPlayer();
                } else {
                    Toast.makeText(itemView.getContext(), R.string.server_error_videos, Toast.LENGTH_LONG).show();
                }
            }
        };
        videoObservable = courseItemViewModel.getVideo(course.getId());
        videoObservable.observe((LifecycleOwner) itemView.getContext(), videoObserver);
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

    private void initialiseVideoControls() {
        binding.getRoot().findViewById(R.id.exo_full_exit).setVisibility(View.GONE);
        maximiseButton = binding.getRoot().findViewById(R.id.exo_full);
        maximiseButton.setOnClickListener(view -> {
            VideoPlayerService.getInstance().setPreserve(video.getId());
            videoCallback.invoke(
                    new VideoControlActionDto(
                            VideoControlActionDto.MAXIMISE,
                            video
                    )
            );
        });
    }

    private void initialiseWorkbooksControls() {
        binding.viewWorkbooks.icExpand.setOnClickListener(view -> {
            Boolean isWorkbooksCollapsed = binding.viewWorkbooks.getWorkbooksCollapsed();
            binding.viewWorkbooks.setWorkbooksCollapsed(!isWorkbooksCollapsed);
            binding.viewWorkbooks.notifyPropertyChanged(binding.viewWorkbooks.recyclerView.getId());
        });
    }

    private void onUpdateWorkbooks(List<Workbook> workbooks, HashMap<String, Uri> workbooksUriMap) {
        courseItemRecyclerViewAdapter.setWorkbooks(workbooks, workbooksUriMap, workbookCallback);
    }

    private void onUpdateCourseDescriptions(List<CourseDescription> descriptions) {
        courseItemRecyclerViewAdapter.setCourseDescriptions(descriptions);
    }

    public void revealPlayer(boolean playerReveal) {
        binding.setIsPlayerMode(playerReveal);
        binding.notifyPropertyChanged(binding.ivImage.getId());
        binding.notifyPropertyChanged(binding.videoPlayer.getId());
    }

    public void initVideoPlayer() {
        revealPlayer(true);
        String preserved = this.videoPlayerService.getPreserve();
        if (video.getId().equals(preserved)) {
            Log.d("INIT VIDEO", "RESUME");
            this.videoPlayerService.resumeVideoStream(
                    binding.getRoot().getContext(),
                    video.getId(),
                    binding.videoPlayer
            );
        } else {
            Log.d("INIT VIDEO", "CREATE NEW");
            this.videoPlayerService.initVideoPlayer(
                    binding.getRoot().getContext(), new VideoPlayerDto(
                            binding.videoPlayer,
                            video
                    ));
        }
    }
}
