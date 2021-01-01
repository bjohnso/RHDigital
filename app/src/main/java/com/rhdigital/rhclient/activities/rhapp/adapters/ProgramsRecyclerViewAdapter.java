package com.rhdigital.rhclient.activities.rhapp.adapters;

import android.content.Context;
import android.graphics.Bitmap;

import android.os.Build.VERSION_CODES;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;


import com.rhdigital.rhclient.R;
import com.rhdigital.rhclient.activities.rhapp.view.ProgramsViewHolder;
import com.rhdigital.rhclient.database.model.Program;

import java.util.HashMap;
import java.util.List;


public class ProgramsRecyclerViewAdapter extends RecyclerView.Adapter<ProgramsViewHolder> {
    private List<Program> programs;
    private Context context;
    private HashMap<String, Bitmap> imageBitMap;

    public ProgramsRecyclerViewAdapter(Context context) {
      this.context = context;
    }

    @NonNull
    @Override
    public ProgramsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      LayoutInflater inflater = LayoutInflater.from(parent.getContext());
      ViewGroup view = (ViewGroup) inflater.inflate(R.layout.view_holder_programs, parent, false);
      return new ProgramsViewHolder(view, context);
    }

    @RequiresApi(api = VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull ProgramsViewHolder holder, int position) {
        if (programs != null) {
          Program program = programs.get(position);

          // Load Image Bitmap
          if (imageBitMap != null) {
            holder.getImageView().setImageBitmap(imageBitMap.get(program.getId()));
          }
        }
    }

    public void setPrograms(List<Program> programs) {
        this.programs = programs;
        notifyDataSetChanged();
    }

    public void setPosterUriMap(HashMap<String, Bitmap> map) {
      this.imageBitMap = map;
      notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (programs != null)
            return programs.size();
        return 0;
    }
}
