package com.example.rhdigital.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rhdigital.R;
import com.example.rhdigital.database.model.Course;

import java.util.List;

public class CoursesRecyclerViewAdapter extends RecyclerView.Adapter<CoursesRecyclerViewAdapter.CoursesViewHolder> {
    private List<Course> courses;

    public CoursesRecyclerViewAdapter() {
    }

    @NonNull
    @Override
    public CoursesRecyclerViewAdapter.CoursesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.courses_recyclerview_item, parent, false);
        return new CoursesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CoursesRecyclerViewAdapter.CoursesViewHolder holder, int position) {
        if (courses != null) {
            Course course = courses.get(position);
            holder.textView.setText("Name: " + course.getName());
        } else {
            holder.textView.setText("No Courses");
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
        private TextView textView;

        public CoursesViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.courses_item_text);
        }
    }
}
