package com.rhdigital.rhclient.activities.rhapp.dialogs;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.rhdigital.rhclient.R;
import com.rhdigital.rhclient.activities.rhapp.delegates.EditProfileDialogDelegate;

public class EditProfileConfirmDialog extends DialogFragment {

    private EditProfileDialogDelegate delegate;
    private Button buttonNo;
    private Button buttonYes;

    public EditProfileConfirmDialog(EditProfileDialogDelegate delegate) {
        this.delegate = delegate;
        setCancelable(false);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_edit_profile_confirm, container, false);
        buttonYes = view.findViewById(R.id.buttonYes);
        buttonNo = view.findViewById(R.id.buttonNo);
        bindUI();
        return view;
    }

    private void bindUI() {

        buttonNo.setOnClickListener(view -> {
            delegate.onComplete(false);
            dismiss();
        });

        buttonYes.setOnClickListener(view -> {
            delegate.onComplete(true);
            dismiss();
        });
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
    }

}
