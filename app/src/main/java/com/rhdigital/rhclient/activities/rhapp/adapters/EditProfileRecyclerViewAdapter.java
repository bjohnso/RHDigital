package com.rhdigital.rhclient.activities.rhapp.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rhdigital.rhclient.R;
import com.rhdigital.rhclient.activities.rhapp.viewholder.EditProfileViewHolder;
import com.rhdigital.rhclient.common.dto.UserFieldDto;
import com.rhdigital.rhclient.common.interfaces.OnClickCallback;

import java.util.List;

public class EditProfileRecyclerViewAdapter extends RecyclerView.Adapter<EditProfileViewHolder>{

    private List<UserFieldDto> fields;
    private OnClickCallback callback;

    public void updateData(List<UserFieldDto> fields, OnClickCallback callback) {
        this.fields = fields;
        this.callback = callback;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public EditProfileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new EditProfileViewHolder(inflater.inflate(R.layout.item_edit_profile, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull EditProfileViewHolder holder, int position) {
        holder.bind(fields.get(position), callback);
    }

    @Override
    public int getItemCount() {
        return fields.size();
    }
}
