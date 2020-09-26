//package com.rhdigital.rhclient.activities.courses.adapters;
//
//import android.graphics.Bitmap;
//import android.net.Uri;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.fragment.app.Fragment;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.rhdigital.rhclient.R;
//import com.rhdigital.rhclient.activities.courses.view.WorkbooksViewHolder;
//import com.rhdigital.rhclient.database.model.Course;
//import com.rhdigital.rhclient.database.model.embedded.CourseWithWorkbooks;
//import com.rhdigital.rhclient.database.model.Workbook;
//import com.rhdigital.rhclient.database.viewmodel.WorkbookViewModel;
//
//import java.util.HashMap;
//import java.util.List;
//
//public class WorkbooksRecyclerViewAdapter extends RecyclerView.Adapter<WorkbooksViewHolder> {
//  private WorkbookViewModel workbookViewModel;
//  private List<CourseWithWorkbooks> coursesWithWorkbooks;
//  private HashMap<String, Bitmap> bitMap;
//  private HashMap<String, HashMap<String, Uri>> workbookUriMap;
//  private Fragment fragment;
//  private ViewGroup parent;
//
//    public WorkbooksRecyclerViewAdapter(Fragment fragment, WorkbookViewModel workbookViewModel) {
//      this.fragment = fragment;
//      this.workbookViewModel = workbookViewModel;
//    }
//
//    @NonNull
//    @Override
//    public WorkbooksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
//        this.parent = parent;
//        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.workbooks_recycler_item, parent, false);
//        return new WorkbooksViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull WorkbooksViewHolder holder, int position) {
//        if (coursesWithWorkbooks != null) {
//          //Extract Embedded Course Object
//          CourseWithWorkbooks courseWithWorkbooks = this.coursesWithWorkbooks.get(position);
//          Course course = courseWithWorkbooks.getCourse();
//
//          holder.setFragment(fragment);
//
//          //Set courseName
//          holder.getHeaderView().setText(course.getName());
//
//          holder.getImageView().setImageBitmap(bitMap.get(course.getId()));
//
//          //Extract Module List Holder from Recycler Item
//          LayoutInflater inflater = LayoutInflater.from(parent.getContext());
//          ViewGroup itemContainer = ((ViewGroup) holder.itemView).findViewById(R.id.workbooks_card_module_list_holder);
//
//          //Clear Nested Module Views From holder
//          itemContainer.removeAllViews();
//
//          //Add Modules to Holder
//          for (Workbook workbook : courseWithWorkbooks.getWorkbooks()) {
//            View view = inflater.inflate(R.layout.workbooks_recycler_item_book, parent, false);
//            TextView workbookName = (TextView) view.findViewWithTag("workbook_name");
//            workbookName.setText(workbook.getName());
//            itemContainer.addView(view);
//            HashMap<String, Uri> uriMap = workbookUriMap.get(course.getId());
//            holder.addWorkbookButton(workbook, view, uriMap.get(workbook.getId()));
//          }
//        }
//    }
//
//    public void setWorkbooks(List<CourseWithWorkbooks> coursesWithWorkbooks) {
//        this.coursesWithWorkbooks = coursesWithWorkbooks;
//        notifyDataSetChanged();
//    }
//
//    public void setWorkbookURI(HashMap<String, HashMap<String, Uri>> workbookUriMap) {
//      this.workbookUriMap = workbookUriMap;
//      notifyDataSetChanged();
//    }
//
//  public void setImageUriMap(HashMap<String, Bitmap> map) {
//    this.bitMap = map;
//    notifyDataSetChanged();
//  }
//
//    @Override
//    public int getItemCount() {
//        if (this.coursesWithWorkbooks != null) {
//            return this.coursesWithWorkbooks.size();
//        }
//        return 0;
//    }
//}
