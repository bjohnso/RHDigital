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
import com.rhdigital.rhclient.database.model.CourseWithWorkbooks;
import com.rhdigital.rhclient.database.model.Workbook;

import java.util.List;

public class WorkbooksRecyclerViewAdapter extends RecyclerView.Adapter<WorkbooksRecyclerViewAdapter.WorkbooksViewHolder> {
    private List<CourseWithWorkbooks> coursesWithWorkbooks;
    private ViewGroup parent;

    public WorkbooksRecyclerViewAdapter() { }

    @NonNull
    @Override
    public WorkbooksRecyclerViewAdapter.WorkbooksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        this.parent = parent;
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.workbooks_recycler_item, parent, false);
        return new WorkbooksViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkbooksRecyclerViewAdapter.WorkbooksViewHolder holder, int position) {
        if (coursesWithWorkbooks != null) {
          //Extract Embedded Course Object
          CourseWithWorkbooks courseWithWorkbooks = this.coursesWithWorkbooks.get(position);
          Course course = courseWithWorkbooks.getCourse();

          //Set image to ImageView
          int id = parent.getResources().getIdentifier(course.getThumbnailURL(), "drawable", parent.getContext().getPackageName());
          holder.imageView.setImageResource(id);

          //Set courseName
          holder.headerView.setText(course.getName());

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

    @Override
    public int getItemCount() {
        if (this.coursesWithWorkbooks != null) {
            return this.coursesWithWorkbooks.size();
        }
        return 0;
    }

    public static class WorkbooksViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView headerView;

        public WorkbooksViewHolder(@NonNull ViewGroup itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.workbooks_image_item);
            headerView = itemView.findViewById(R.id.workbooks_text_header_item);
        }
    }
}
