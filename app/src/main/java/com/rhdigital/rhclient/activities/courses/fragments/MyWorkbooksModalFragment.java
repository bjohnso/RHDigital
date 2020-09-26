//package com.rhdigital.rhclient.activities.courses.fragments;
//
//import android.app.Activity;
//import android.app.Dialog;
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.DialogFragment;
//
//import com.rhdigital.rhclient.R;
//
//public class MyWorkbooksModalFragment extends DialogFragment{
//
//  private Button viewButton;
//  private Button saveButton;
//
//  @NonNull
//  @Override
//  public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
//    return super.onCreateDialog(savedInstanceState);
//  }
//
//  @Nullable
//  @Override
//  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//    if (getArguments() != null) {
//      View view = inflater.inflate(R.layout.workbooks_item_modal_download_dialog_layout, container, false);
//      saveButton = view.findViewById(R.id.workbooks_modal_download_save_button);
//      viewButton = view.findViewById(R.id.workbooks_modal_download_view_button);
//
//      saveButton.setOnClickListener(new ButtonOnClick(this));
//      viewButton.setOnClickListener(new ButtonOnClick(this));
//      return view;
//    }
//    return null;
//  }
//
//  @Override
//  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//    super.onViewCreated(view, savedInstanceState);
//
//  }
//
//  public void sendDataToParent(String value) {
//    if (getTargetFragment() == null) {
//      this.dismiss();
//      return;
//    }
//    Intent intent = new Intent();
//    intent.putExtra("ACTION", value);
//    intent.putExtra("ID", getArguments().getString("ID"));
//    intent.putExtra("URL", getArguments().getString("URL"));
//    getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
//    this.dismiss();
//  }
//
//  public static class ButtonOnClick implements View.OnClickListener {
//    private DialogFragment dialogFragment;
//
//    public ButtonOnClick(DialogFragment dialogFragment) {
//      this.dialogFragment = dialogFragment;
//    }
//
//    @Override
//    public void onClick(View view) {
//      if (view.getId() == R.id.workbooks_modal_download_save_button) {
//        ((MyWorkbooksModalFragment) dialogFragment).sendDataToParent("SAVE");
//      } else if (view.getId() == R.id.workbooks_modal_download_view_button) {
//        ((MyWorkbooksModalFragment) dialogFragment).sendDataToParent("VIEW");
//      }
//    }
//  }
//}
//
