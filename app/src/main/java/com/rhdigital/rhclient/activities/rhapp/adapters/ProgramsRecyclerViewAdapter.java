package com.rhdigital.rhclient.activities.rhapp.adapters;

import android.graphics.Bitmap;

import android.os.Build.VERSION_CODES;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;


import com.rhdigital.rhclient.R;
import com.rhdigital.rhclient.activities.rhapp.viewholder.ProgramsViewHolder;
import com.rhdigital.rhclient.common.interfaces.OnClickCallback;
import com.rhdigital.rhclient.room.model.Program;

import java.util.HashMap;
import java.util.List;


public class ProgramsRecyclerViewAdapter extends RecyclerView.Adapter<ProgramsViewHolder> {
    private List<Program> programs;
    private OnClickCallback callback;
    private HashMap<String, Bitmap> imageBitMap;

    public ProgramsRecyclerViewAdapter(OnClickCallback callback) {
      this.callback = callback;
    }

    @NonNull
    @Override
    public ProgramsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      LayoutInflater inflater = LayoutInflater.from(parent.getContext());
      ViewGroup view = (ViewGroup) inflater.inflate(R.layout.item_programs, parent, false);
      return new ProgramsViewHolder(view, callback);
    }


    @RequiresApi(api = VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull ProgramsViewHolder holder, int position) {
        if (programs != null && imageBitMap != null) {
          Program program = programs.get(position);
          holder.bind(
                  program,
                  imageBitMap.get(program.getId())
          );
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
