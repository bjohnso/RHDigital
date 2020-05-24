package com.rhdigital.rhclient.activities.user.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.rhdigital.rhclient.R;

public class UserProfileEditModalFragment extends DialogFragment {

  private String propertyNameText;
  private TextView propertyName;

  @NonNull
  @Override
  public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
    return super.onCreateDialog(savedInstanceState);
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.user_profile_edit_modal_layout, container, false);
    propertyName = view.findViewById(R.id.user_profile_edit_modal_property_name);
    if (getArguments() != null) {
      this.propertyNameText = getArguments().getString("PROPERTY_NAME");
      propertyName.setText(propertyNameText);
    }
    return view;
  }
}
