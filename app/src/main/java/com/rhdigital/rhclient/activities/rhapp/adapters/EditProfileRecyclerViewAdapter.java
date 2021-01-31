package com.rhdigital.rhclient.activities.rhapp.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rhdigital.rhclient.R;
import com.rhdigital.rhclient.activities.rhapp.viewholder.EditProfileViewHolder;
import com.rhdigital.rhclient.common.dto.UserFieldDto;
import com.rhdigital.rhclient.common.interfaces.OnClickCallback;

import java.util.LinkedHashMap;

public class EditProfileRecyclerViewAdapter extends RecyclerView.Adapter<EditProfileViewHolder>{

    private LinkedHashMap<String, UserFieldDto> fields;
    private OnClickCallback callback;

    public void updateData(LinkedHashMap<String, UserFieldDto> fields) {
        this.fields = fields;
        notifyDataSetChanged();
    }

    public void updateCallback(OnClickCallback callback) {
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
        holder.bind((UserFieldDto) fields.values().toArray()[position], callback);
    }

    @Override
    public int getItemCount() {
        return fields.size();
    }
}
