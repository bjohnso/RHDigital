package com.rhdigital.rhclient.activities.user.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
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

public class UserProfileEditModalFragment extends DialogFragment {

  private String propertyNameText;
  private TextView propertyName;
  private EditText editText;
  private Button cancelButton;
  private Button okButton;

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
    cancelButton = view.findViewById(R.id.user_profile_edit_modal_cancel_button);
    okButton = view.findViewById(R.id.user_profile_modal_ok_button);
    editText = view.findViewById(R.id.user_profile_edit_modal_input);
    editText.requestFocus();
    if (getArguments() != null) {
      this.propertyNameText = getArguments().getString("PROPERTY_NAME");
      propertyName.setText(propertyNameText);
    }

    ButtonOnClick buttonOnClick = new ButtonOnClick(this);

    cancelButton.setOnClickListener(buttonOnClick);
    okButton.setOnClickListener(buttonOnClick);
    return view;
  }

  public void sendDataToParent() {
    if (getTargetFragment() == null) {
      this.dismiss();
      return;
    }
    Intent intent = new Intent();
    intent.putExtra("PROPERTY_NAME", propertyNameText);
    intent.putExtra("PROPERTY_VALUE", editText.getText().toString());
    getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
    this.dismiss();
  }

  public static class ButtonOnClick implements View.OnClickListener {
    private DialogFragment dialogFragment;

    public ButtonOnClick(DialogFragment dialogFragment) {
      this.dialogFragment = dialogFragment;
    }

    @Override
    public void onClick(View view) {
      if (view.getId() == R.id.user_profile_modal_ok_button) {
        ((UserProfileEditModalFragment)dialogFragment).sendDataToParent();
      } else if (view.getId() == R.id.user_profile_edit_modal_cancel_button) {
        dialogFragment.dismiss();
      }
    }
  }
}
