package com.rhdigital.rhclient.activities.courses.adapters;

import android.animation.AnimatorSet;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

import android.os.Build.VERSION_CODES;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.rhdigital.rhclient.R;
import com.rhdigital.rhclient.activities.courses.listeners.CourseItemViewBackOnClick;
import com.rhdigital.rhclient.activities.courses.listeners.CourseItemViewWatchNowOnClick;
import com.rhdigital.rhclient.database.model.Course;
import com.rhdigital.rhclient.common.loader.CustomLoaderFactory;

import java.util.HashMap;
import java.util.List;


public class CoursesRecyclerViewAdapter extends RecyclerView.Adapter<CoursesRecyclerViewAdapter.CoursesViewHolder> {
    private List<Course> courses;
    private Context context;
    private HashMap<String, Bitmap> bitMap;
    private HashMap<String, Uri> videoURIMap;

    public CoursesRecyclerViewAdapter(Context context) {
      this.context = context;
    }

    @NonNull
    @Override
    public CoursesRecyclerViewAdapter.CoursesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      LayoutInflater inflater = LayoutInflater.from(parent.getContext());
      ViewGroup view = (ViewGroup) inflater.inflate(R.layout.courses_recyclerview_item, parent, false);
      return new CoursesViewHolder(view, context);
    }

    @RequiresApi(api = VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull CoursesRecyclerViewAdapter.CoursesViewHolder holder, int position) {
        if (courses != null) {
          Course course = courses.get(position);
          holder.headerView.setText(course.getName());
          // Load Image Bitmap
          holder.imageView.setImageBitmap(bitMap.get(course.getId()));
          if (course.isAuthorised()) {
            holder.setIsAuthorisedMode(true);
            holder.videoTitle.setText(course.getName());
            holder.setVideoUri(videoURIMap.get(course.getId()));
            holder.initVideoPlayer();
          } else {
            holder.setIsAuthorisedMode(false);
          }
        }
    }

  @Override
  public void onViewDetachedFromWindow(@NonNull CoursesViewHolder holder) {
    super.onViewDetachedFromWindow(holder);
    holder.disableVideo();
    holder.destroyVideo();
  }

  public void setCourses(List<Course> courses) {
        this.courses = courses;
        notifyDataSetChanged();
    }

    public void setImageUriMap(HashMap<String, Bitmap> map) {
      this.bitMap = map;
      notifyDataSetChanged();
    }

    public void setVideoURIMap(HashMap<String, Uri> map) {
      this.videoURIMap = map;
      notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (courses != null)
            return courses.size();
        return 0;
    }

    public static class CoursesViewHolder extends RecyclerView.ViewHolder {
        private TextView headerView;
        private TextView videoTitle;
        private ImageView imageView;
        private Button actionButton;
        private ImageButton backButton;
        private FrameLayout frameLayout;
        private PlayerView videoPlayer;
        private RelativeLayout videoContainer;
        private RelativeLayout itemContent;
        private SimpleExoPlayer player;
        private CustomLoaderFactory customLoaderFactory = null;
        private int imageWidth = 0;
        private int imageHeight = 0;

        private boolean isAuthorisedMode = false;

        private Context context;
        private Uri videUri;
        private MediaSource mediaSource;

        public CoursesViewHolder(@NonNull View itemView, Context context) {
          super(itemView);

          this.context = context;

          videoTitle = itemView.findViewById(R.id.video_title);
          videoContainer = itemView.findViewById(R.id.video_container);
          itemContent = itemView.findViewById(R.id.item_content);
          imageView = itemView.findViewById(R.id.courses_card_item_image_view);
          headerView = itemView.findViewById(R.id.courses_text_header_item);
          frameLayout = (FrameLayout) itemView.findViewById(R.id.loader);
          videoPlayer = (PlayerView) itemView.findViewById(R.id.video_player);
          actionButton = itemView.findViewById(R.id.courses_item_action_button);
          backButton = itemView.findViewById(R.id.video_back_button);

          // OnClickListeners
          backButton.setOnClickListener(new CourseItemViewBackOnClick(itemView.getContext(), this));

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

          frameLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
              if (initLoaderFactory()) {
                addLoader();
                frameLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this::onGlobalLayout);
              }
            }
          });
        }

        public void setIsAuthorisedMode(boolean authorisedMode) {
          this.isAuthorisedMode = authorisedMode;
          if (actionButton.hasOnClickListeners()) {
            actionButton.setOnClickListener(null);
          }
          if (isAuthorisedMode) {
            actionButton.setOnClickListener(new CourseItemViewWatchNowOnClick(itemView.getContext(), this));
            actionButton.setText("Watch Now");
          } else {
            //TODO: Enable More Info Functionality
            actionButton.setText("More Info");
          }
        }

        // Video Player
        public void setVideoUri(Uri uri) {
          this.videUri = uri;
        }

        public void initVideoPlayer() {
          if (player == null) {
            player = new SimpleExoPlayer.Builder(context).build();
            videoPlayer.setPlayer(player);
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(context,
              Util.getUserAgent(context, context.getString(R.string.app_name)));
            mediaSource = new ProgressiveMediaSource.Factory(dataSourceFactory)
              .createMediaSource(videUri);
          }
        }

        public void enableVideo() {
          videoContainer.setVisibility(View.VISIBLE);
          itemContent.setVisibility(View.GONE);
        }

        public void disableVideo() {
          videoContainer.setVisibility(View.GONE);
          itemContent.setVisibility(View.VISIBLE);
        }

        public void startVideo() {
          if (player == null) {
            initVideoPlayer();
          }
          enableVideo();
          player.prepare(this.mediaSource);
        }

        public void destroyVideo() {
          if (player != null) {
            disableVideo();
            player.release();
            player = null;
          }
        }

        // Loader
        private boolean initLoaderFactory() {
          if (frameLayout.getHeight() > 0 && frameLayout.getWidth() > 0) {
            if (customLoaderFactory == null) {
              customLoaderFactory = new CustomLoaderFactory(
                itemView.getContext(),
                frameLayout.getWidth(),
                frameLayout.getHeight(),
                4,
                35,
                25);
              return true;
            }
          }
          return false;
        }

      public void addLoader() {
          if (customLoaderFactory != null) {
            if (!initLoaderFactory()) {
              removeLoader();
            }
            for (View v : customLoaderFactory.getChildren()) {
              frameLayout.addView(v);
            }

            for (AnimatorSet a : customLoaderFactory.createAnimations()) {
              a.start();
            }
          }
      }

      public void removeLoader() {
          for (AnimatorSet a : customLoaderFactory.createAnimations()) {
            a.end();
          }
          for (View v : customLoaderFactory.getChildren()) {
            frameLayout.removeView(v);
          }
      }
    }
}
