package com.rhdigital.rhclient.ui.adapters;

import android.animation.AnimatorSet;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

import android.os.Build.VERSION_CODES;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.rhdigital.rhclient.R;
import com.rhdigital.rhclient.database.model.Course;
import com.rhdigital.rhclient.ui.view.CustomLoaderFactory;

import java.util.HashMap;
import java.util.List;


public class CoursesRecyclerViewAdapter extends RecyclerView.Adapter<CoursesRecyclerViewAdapter.CoursesViewHolder> {
    private List<Course> courses;
    private Context context;
    private HashMap<String, Bitmap> bitMap;

    public CoursesRecyclerViewAdapter(Context context) {
      this.context = context;
    }

    @NonNull
    @Override
    public CoursesRecyclerViewAdapter.CoursesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      LayoutInflater inflater = LayoutInflater.from(parent.getContext());
      ViewGroup view = (ViewGroup) inflater.inflate(R.layout.courses_recyclerview_item, parent, false);
      return new CoursesViewHolder(view);
    }

    @RequiresApi(api = VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull CoursesRecyclerViewAdapter.CoursesViewHolder holder, int position) {
        if (courses != null) {
          Course course = courses.get(position);
          holder.headerView.setText(course.getName());
          // Load Image Bitmap
          holder.imageView.setImageBitmap(bitMap.get(course.getId()));
        }
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
        notifyDataSetChanged();
    }

    public void setImageUriMap(HashMap<String, Bitmap> map) {
      this.bitMap = map;
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
        private ImageView imageView;
        private FrameLayout frameLayout;
        private CustomLoaderFactory customLoaderFactory = null;
        private int imageWidth = 0;
        private int imageHeight = 0;

        public CoursesViewHolder(@NonNull View itemView) {
          super(itemView);
          imageView = itemView.findViewById(R.id.courses_card_item_image_view);
          headerView = itemView.findViewById(R.id.courses_text_header_item);
          frameLayout = (FrameLayout) itemView.findViewById(R.id.loader);


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
