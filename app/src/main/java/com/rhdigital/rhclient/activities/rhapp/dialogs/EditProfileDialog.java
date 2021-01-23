package com.rhdigital.rhclient.activities.rhapp.dialogs;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.rhdigital.rhclient.R;
import com.rhdigital.rhclient.common.dto.UserFieldDto;

public class EditProfileDialog extends DialogFragment {

    private UserFieldDto userField;
    private TextView tvProperty;
    private EditText etValue;
    private Button buttonCancel;
    private Button buttonOk;

    public EditProfileDialog(UserFieldDto userField) {
        this.userField = userField;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_edit_profile, container, false);
        tvProperty = view.findViewById(R.id.tvTitle);
        etValue = view.findViewById(R.id.etProperty);
        buttonCancel = view.findViewById(R.id.buttonCancel);
        bindUI();
        return view;
    }

    private void bindUI() {
        tvProperty.setText(userField.getField());
        etValue.setText(userField.getValue());
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
    }
}
