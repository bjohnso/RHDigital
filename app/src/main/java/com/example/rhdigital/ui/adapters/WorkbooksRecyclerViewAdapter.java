package com.example.rhdigital.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rhdigital.R;
import com.example.rhdigital.database.model.Course;
import com.example.rhdigital.database.model.Workbook;

import java.util.List;

public class WorkbooksRecyclerViewAdapter extends RecyclerView.Adapter<WorkbooksRecyclerViewAdapter.WorkbooksViewHolder> {
    private List<Workbook> workbooks;
    private ViewGroup parent;

    public WorkbooksRecyclerViewAdapter() { }

    @NonNull
    @Override
    public WorkbooksRecyclerViewAdapter.WorkbooksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        this.parent = parent;
        View view = inflater.inflate(R.layout.workbooks_recycler_item, parent, false);
        return new WorkbooksViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkbooksRecyclerViewAdapter.WorkbooksViewHolder holder, int position) {
        if (workbooks != null) {
            Workbook workbook = workbooks.get(position);
            holder.headerView.setText(workbook.getName()
                    + " - "
                    + workbook.getDescription()
            );
        }
    }

    public void setWorkbooks(List<Workbook> workbooks) {
        this.workbooks = workbooks;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (this.workbooks != null) {
            return this.workbooks.size();
        }
        return 0;
    }

    public static class WorkbooksViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView headerView;

        public WorkbooksViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.workbooks_card_image_holder);
            headerView = itemView.findViewById(R.id.workbooks_text_header_item);
        }
    }
}
