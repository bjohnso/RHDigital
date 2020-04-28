package com.rhdigital.rhclient.ui.adapters;

import android.graphics.drawable.Animatable2;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.rhdigital.rhclient.R;
import com.rhdigital.rhclient.database.model.Course;
import com.rhdigital.rhclient.util.RemoteImageConnector;

import java.util.HashMap;
import java.util.List;

import static android.view.ViewGroup.LayoutParams.*;

public class CoursesRecyclerViewAdapter extends RecyclerView.Adapter<CoursesRecyclerViewAdapter.CoursesViewHolder> {
    private List<Course> courses;
    private ViewGroup parent;

    public CoursesRecyclerViewAdapter() {
    }

    @NonNull
    @Override
    public CoursesRecyclerViewAdapter.CoursesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        this.parent = parent;
        View view = inflater.inflate(R.layout.courses_recyclerview_item, parent, false);
        return new CoursesViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull CoursesRecyclerViewAdapter.CoursesViewHolder holder, int position) {
        if (courses != null) {
          Course course = courses.get(position);
          holder.headerView.setText(course.getName());

          // Initialise A Loader Animation
          AnimatedVectorDrawable loader = (AnimatedVectorDrawable) this.parent.getContext().getDrawable(R.drawable.spinner_animation);
          // TODO : Make Animation Listener Backwards Compatible
          loader.registerAnimationCallback(new Animatable2.AnimationCallback() {
            @Override
            public void onAnimationEnd(Drawable drawable) {
              loader.start();
            }
          });
          loader.start();

          holder.imageView.requestLayout();
          holder.imageView.getLayoutParams().height = 100;
          holder.imageView.getLayoutParams().width = 100;

          // Build URL for this image
          RemoteImageConnector
            .getInstance()
            .getResourceURL(parent.getContext(), course.getThumbnailURL())
            .getDownloadUrl().addOnSuccessListener(uri -> {
              // Fetch image from firebase cloud
              Glide
                .with(parent.getContext())
                .load(uri)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .dontAnimate()
                .dontTransform()
                .placeholder(loader)
                .listener(new RequestListener<Uri, GlideDrawable>() {
                  @Override
                  public boolean onException(Exception e, Uri model, Target<GlideDrawable> target, boolean isFirstResource) {
                    return false;
                  }

                  @Override
                  public boolean onResourceReady(GlideDrawable resource, Uri model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                    if (!isFromMemoryCache) {
                      loader.clearAnimationCallbacks();
                      loader.stop();
                      holder.imageView.setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT));
                      holder.imageView.setImageDrawable(resource);
                    }
                    return true;
                  }
                }).into(holder.imageView);
            });
        }
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
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

        public CoursesViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.courses_card_item_image_view);
            headerView = itemView.findViewById(R.id.courses_text_header_item);
        }
    }
}
