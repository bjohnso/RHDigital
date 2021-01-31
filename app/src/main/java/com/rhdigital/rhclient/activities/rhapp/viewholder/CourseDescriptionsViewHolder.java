package com.rhdigital.rhclient.activities.rhapp.viewholder;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.rhdigital.rhclient.R;
import com.rhdigital.rhclient.common.ancestors.BaseViewHolder;
import com.rhdigital.rhclient.room.model.CourseDescription;

public class CourseDescriptionsViewHolder extends BaseViewHolder {
    TextView tvDescription;

    public CourseDescriptionsViewHolder(@NonNull View itemView) {
        super(itemView);
        tvDescription = itemView.findViewById(R.id.tvDescription);
    }

    public void bind(CourseDescription courseDescription) {
        tvDescription.setText(courseDescription.getDescription());
    }
}
