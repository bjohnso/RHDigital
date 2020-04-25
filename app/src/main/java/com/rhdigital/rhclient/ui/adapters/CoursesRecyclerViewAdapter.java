package com.rhdigital.rhclient.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rhdigital.rhclient.R;
import com.rhdigital.rhclient.database.model.Course;

import java.util.List;

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

    @Override
    public void onBindViewHolder(@NonNull CoursesRecyclerViewAdapter.CoursesViewHolder holder, int position) {
        if (courses != null) {
            Course course = courses.get(position);
            holder.headerView.setText(course.getName());
            int id = parent.getResources().getIdentifier(course.getThumbnailURL(), "drawable", parent.getContext().getPackageName());
            holder.imageView.setImageResource(id);
        } else {

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
