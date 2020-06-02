package com.rhdigital.rhclient.activities.courses.view;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rhdigital.rhclient.R;
import com.rhdigital.rhclient.activities.courses.fragments.MyWorkbooksFragment;
import com.rhdigital.rhclient.activities.courses.fragments.MyWorkbooksModalFragment;
import com.rhdigital.rhclient.database.model.Workbook;

import java.util.ArrayList;
import java.util.List;

public class WorkbooksViewHolder extends RecyclerView.ViewHolder {
  private Fragment fragment;
  private ImageView imageView;
  private TextView headerView;
  private List<View> workbookButtons;

  public WorkbooksViewHolder(@NonNull ViewGroup itemView) {
    super(itemView);
    imageView = itemView.findViewById(R.id.workbooks_image_item);
    headerView = itemView.findViewById(R.id.workbooks_text_header_item);
  }

  public void setFragment(Fragment fragment) {
    this.fragment = fragment;
  }

  public void addWorkbookButton(Workbook workbook, View workbookButton, Uri uri) {
    if (this.workbookButtons == null) {
      this.workbookButtons = new ArrayList<>();
    }
    workbookButton.setOnClickListener(new WorkbookOnClick(workbook, fragment, uri));
    this.workbookButtons.add(workbookButton);
  }

  public ImageView getImageView() {
    return imageView;
  }

  public TextView getHeaderView() {
    return headerView;
  }

  public static class WorkbookOnClick implements View.OnClickListener {
    private Context context;
    private Workbook workbook;
    private Fragment fragment;
    private FragmentManager fragmentManager;
    private Uri uri;

    public WorkbookOnClick(Workbook workbook, Fragment fragment, Uri uri) {
      this.context = fragment.getContext();
      this.workbook = workbook;
      this.fragment = fragment;
      this.fragmentManager = fragment.getParentFragmentManager();
      this.uri = uri;
    }

    @Override
    public void onClick(View view) {
      DialogFragment dialogFragment;
      Bundle bundle = new Bundle();
      bundle.putString("NAME", workbook.getWorkbookURL());
      bundle.putString("URL", uri.toString());
      MyWorkbooksFragment myWorkbooksFragment = (MyWorkbooksFragment) fragment;
      dialogFragment = new MyWorkbooksModalFragment();
      dialogFragment.setArguments(bundle);
      dialogFragment.setTargetFragment(myWorkbooksFragment, myWorkbooksFragment.getREQUEST_CODE());
      dialogFragment.show(fragmentManager, uri.toString());
    }
  }
}
