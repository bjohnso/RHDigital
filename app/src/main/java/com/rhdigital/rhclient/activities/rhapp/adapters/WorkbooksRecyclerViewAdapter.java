package com.rhdigital.rhclient.activities.rhapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rhdigital.rhclient.R;
import com.rhdigital.rhclient.activities.rhapp.viewholder.WorkbooksViewHolder;
import com.rhdigital.rhclient.database.model.Workbook;

import java.util.List;

public class WorkbooksRecyclerViewAdapter extends RecyclerView.Adapter<WorkbooksViewHolder> {

    private List<Workbook> workbooks;

    @NonNull
    @Override
    public WorkbooksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.view_holder_workbooks, parent, false);
        return new WorkbooksViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkbooksViewHolder holder, int position) {
        if (workbooks != null) {
            Workbook workbook = workbooks.get(position);
            holder.bind(workbook);
        }
    }

    public void setWorkbooks(List<Workbook> workbooks) {
        this.workbooks = workbooks;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (workbooks != null)
            return workbooks.size();
        return 0;
    }
}
