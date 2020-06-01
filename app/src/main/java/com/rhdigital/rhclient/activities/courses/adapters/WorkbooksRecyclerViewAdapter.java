package com.rhdigital.rhclient.activities.courses.adapters;

import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.rhdigital.rhclient.R;
import com.rhdigital.rhclient.activities.courses.view.WorkbooksViewHolder;
import com.rhdigital.rhclient.database.model.Course;
import com.rhdigital.rhclient.database.model.CourseWithWorkbooks;
import com.rhdigital.rhclient.database.model.Workbook;
import com.rhdigital.rhclient.common.services.RemoteResourceService;

import java.util.HashMap;
import java.util.List;

public class WorkbooksRecyclerViewAdapter extends RecyclerView.Adapter<WorkbooksViewHolder> {
    private List<CourseWithWorkbooks> coursesWithWorkbooks;
    private HashMap<String, Bitmap> bitMap;
    private HashMap<String, List<Uri>> workbookUriMap;
    private ViewGroup parent;

    public WorkbooksRecyclerViewAdapter() { }

    @NonNull
    @Override
    public WorkbooksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        this.parent = parent;
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.workbooks_recycler_item, parent, false);
        return new WorkbooksViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkbooksViewHolder holder, int position) {
        if (coursesWithWorkbooks != null) {
          //Extract Embedded Course Object
          CourseWithWorkbooks courseWithWorkbooks = this.coursesWithWorkbooks.get(position);
          Course course = courseWithWorkbooks.getCourse();

          //Set courseName
          holder.getHeaderView().setText(course.getName());

          holder.getImageView().setImageBitmap(bitMap.get(course.getId()));

          //Extract Module List Holder from Recycler Item
          LayoutInflater inflater = LayoutInflater.from(parent.getContext());
          ViewGroup itemContainer = ((ViewGroup) holder.itemView).findViewById(R.id.workbooks_card_module_list_holder);

          //Clear Nested Module Views From holder
          itemContainer.removeAllViews();

          //Add Modules to Holder
          for (Workbook workbook : courseWithWorkbooks.getWorkbooks()) {
            View view = inflater.inflate(R.layout.workbooks_recycler_item_book, parent, false);
            TextView workbookName = (TextView) view.findViewWithTag("workbook_name");
            workbookName.setText(workbook.getName());
            itemContainer.addView(view);
          }
        }
    }

    public void setWorkbooks(List<CourseWithWorkbooks> coursesWithWorkbooks) {
        this.coursesWithWorkbooks = coursesWithWorkbooks;
        notifyDataSetChanged();
    }

    public void setWorkbookURI(HashMap<String, List<Uri>> workbookUriMap) {
      this.workbookUriMap = workbookUriMap;
      notifyDataSetChanged();
    }

  public void setImageUriMap(HashMap<String, Bitmap> map) {
    this.bitMap = map;
    notifyDataSetChanged();
  }

    @Override
    public int getItemCount() {
        if (this.coursesWithWorkbooks != null) {
            return this.coursesWithWorkbooks.size();
        }
        return 0;
    }
}
