package com.rhdigital.rhclient.activities.rhapp.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.rhdigital.rhclient.R;
import com.rhdigital.rhclient.common.ancestors.BaseViewHolder;
import com.rhdigital.rhclient.common.dto.UserFieldDto;
import com.rhdigital.rhclient.common.interfaces.OnClickCallback;

public class EditProfileViewHolder extends BaseViewHolder {

    private TextView tvField;
    private TextView tvValue;

    public EditProfileViewHolder(@NonNull View itemView) {
        super(itemView);
        tvField = itemView.findViewById(R.id.tvField);
        tvValue = itemView.findViewById(R.id.tvValue);
    }

    @Override
    public void bind(UserFieldDto userField, OnClickCallback callback) {
        tvField.setText(userField.getField());
        tvValue.setText(userField.getValue());
        itemView.setOnClickListener(view -> {
            callback.invoke(userField);
        });
    }
}
