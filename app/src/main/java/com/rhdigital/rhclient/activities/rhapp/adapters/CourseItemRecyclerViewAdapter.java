package com.rhdigital.rhclient.activities.rhapp.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rhdigital.rhclient.R;
import com.rhdigital.rhclient.activities.rhapp.viewholder.CourseDescriptionsViewHolder;
import com.rhdigital.rhclient.activities.rhapp.viewholder.WorkbooksViewHolder;
import com.rhdigital.rhclient.common.ancestors.BaseViewHolder;
import com.rhdigital.rhclient.common.interfaces.OnClickCallback;
import com.rhdigital.rhclient.room.model.CourseDescription;
import com.rhdigital.rhclient.room.model.Workbook;

import java.util.List;

public class CourseItemRecyclerViewAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    public static final int WORKBOOK = 0;
    public static final int DESCRIPTION = 1;

    private List<Workbook> workbooks;
    private OnClickCallback callback;
    private List<CourseDescription> descriptions;

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ViewGroup view = null;
        switch (viewType) {
            case WORKBOOK:
                return new WorkbooksViewHolder(
                        inflater.inflate(R.layout.item_workbooks, parent, false)
                );
            case DESCRIPTION:
                return new CourseDescriptionsViewHolder(
                        inflater.inflate(R.layout.item_course_descriptions, parent, false)
                );
        }
        return new BaseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        if (workbooks != null) {
            Workbook workbook = workbooks.get(position);
            holder.bind(workbook);
        } else if (descriptions != null) {
            CourseDescription courseDescription = descriptions.get(position);
            holder.bind(courseDescription);
        }
    }

    public void setWorkbooks(List<Workbook> workbooks) {
        this.descriptions = null;
        this.workbooks = workbooks;
        notifyDataSetChanged();
    }

    public void setCourseDescriptions(List<CourseDescription> descriptions) {
        this.workbooks = null;
        this.descriptions = descriptions;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (workbooks != null)
            return WORKBOOK;
        else if (descriptions != null)
            return DESCRIPTION;
        return -1;
    }

    @Override
    public int getItemCount() {
        if (workbooks != null)
            return workbooks.size();
        else if (descriptions != null)
            return descriptions.size();
        return 0;
    }
}
