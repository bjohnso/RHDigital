//package com.rhdigital.rhclient.activities.courses.adapters;
//
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.net.Uri;
//
//import android.os.Build.VERSION_CODES;
//
//import android.view.LayoutInflater;
//import android.view.ViewGroup;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.RequiresApi;
//import androidx.recyclerview.widget.RecyclerView;
//
//
//import com.rhdigital.rhclient.R;
//import com.rhdigital.rhclient.activities.courses.services.VideoPlayerService;
//import com.rhdigital.rhclient.activities.courses.view.CoursesViewHolder;
//import com.rhdigital.rhclient.database.model.Course;
//
//import java.util.HashMap;
//import java.util.List;
//
//
//public class CoursesRecyclerViewAdapter extends RecyclerView.Adapter<CoursesViewHolder> {
//    private List<Course> courses;
//    private Context context;
//    private HashMap<String, Bitmap> bitMap;
//    private HashMap<String, Uri> videoURIMap;
//
//    public CoursesRecyclerViewAdapter(Context context) {
//      this.context = context;
//    }
//
//    @NonNull
//    @Override
//    public CoursesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//      LayoutInflater inflater = LayoutInflater.from(parent.getContext());
//      ViewGroup view = (ViewGroup) inflater.inflate(R.layout.courses_recyclerview_item, parent, false);
//      return new CoursesViewHolder(view, context);
//    }
//
//    @RequiresApi(api = VERSION_CODES.M)
//    @Override
//    public void onBindViewHolder(@NonNull CoursesViewHolder holder, int position) {
//        if (courses != null) {
//          Course course = courses.get(position);
//          holder.setCourse(course);
//          // Load Image Bitmap
//          holder.getImageView().setImageBitmap(bitMap.get(course.getId()));
//          if (course.isAuthorised()) {
//            holder.setIsAuthorisedMode(true);
//            holder.setVideoUri(videoURIMap.get(course.getId()));
//          } else {
//            holder.setIsAuthorisedMode(false);
//          }
//        }
//    }
//
//  public void setCourses(List<Course> courses) {
//        this.courses = courses;
//        notifyDataSetChanged();
//    }
//
//    public void setImageUriMap(HashMap<String, Bitmap> map) {
//      this.bitMap = map;
//      notifyDataSetChanged();
//    }
//
//    public void setVideoURIMap(HashMap<String, Uri> map) {
//      this.videoURIMap = map;
//      notifyDataSetChanged();
//    }
//
//    @Override
//    public int getItemCount() {
//        if (courses != null)
//            return courses.size();
//        return 0;
//    }
//}
