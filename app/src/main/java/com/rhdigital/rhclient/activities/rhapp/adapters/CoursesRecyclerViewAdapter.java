package com.rhdigital.rhclient.activities.rhapp.adapters;

import android.graphics.Bitmap;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;
import com.rhdigital.rhclient.R;
import com.rhdigital.rhclient.activities.rhapp.viewholder.CoursesViewHolder;
import com.rhdigital.rhclient.database.model.Course;
import com.rhdigital.rhclient.database.model.Program;

import java.util.HashMap;
import java.util.List;

public class CoursesRecyclerViewAdapter extends RecyclerView.Adapter<CoursesViewHolder> {

    private Program program;
    private List<Course> courses;
    private HashMap<String, Bitmap> imageBitMap;

    public CoursesRecyclerViewAdapter(Program program) {
        this.program = program;
    }

    @NonNull
    @Override
    public CoursesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.view_holder_courses, parent, false);
        return new CoursesViewHolder(view);
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull CoursesViewHolder holder, int position) {
        if (courses != null && imageBitMap != null) {
            Course course = courses.get(position);
            holder.bind(
                    program,
                    course,
                    imageBitMap.get(course.getId())
            );
        }
    }

    public void setProgram(Program program) {
        this.program = program;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
        notifyDataSetChanged();
    }

    public void setPosterUriMap(HashMap<String, Bitmap> map) {
        this.imageBitMap = map;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (courses != null)
            return courses.size();
        return 0;
    }
}
