package com.rhdigital.rhclient.activities.courses.view;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.rhdigital.rhclient.R;
import com.rhdigital.rhclient.activities.courses.fragments.MyWorkbooksFragment;
import com.rhdigital.rhclient.activities.courses.fragments.MyWorkbooksModalFragment;
import com.rhdigital.rhclient.activities.user.fragments.UserProfileEditFragment;
import com.rhdigital.rhclient.activities.user.fragments.UserProfileEditModalFragment;
import com.rhdigital.rhclient.database.viewmodel.WorkbookViewModel;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;

public class WorkbooksViewHolder extends RecyclerView.ViewHolder {
  private Fragment fragment;
  private ImageView imageView;
  private TextView headerView;
  private List<View> workbookButtons;
  private WorkbookViewModel workbookViewModel;

  public WorkbooksViewHolder(@NonNull ViewGroup itemView) {
    super(itemView);
    imageView = itemView.findViewById(R.id.workbooks_image_item);
    headerView = itemView.findViewById(R.id.workbooks_text_header_item);
  }

  public void setFragment(Fragment fragment) {
    this.fragment = fragment;
  }

  public void addWorkbookButton(View workbookButton, Uri uri) {
    if (this.workbookButtons == null) {
      this.workbookButtons = new ArrayList<>();
    }
    workbookButton.setOnClickListener(new WorkbookOnClick(fragment, workbookViewModel, uri));
    this.workbookButtons.add(workbookButton);
  }

  public void setWorkbookViewModel(WorkbookViewModel workbookViewModel) {
    this.workbookViewModel = workbookViewModel;
  }

  public ImageView getImageView() {
    return imageView;
  }

  public TextView getHeaderView() {
    return headerView;
  }

  public static class WorkbookOnClick implements View.OnClickListener {
    private Context context;
    private Fragment fragment;
    private FragmentManager fragmentManager;
    private WorkbookViewModel workbookViewModel;
    private Uri uri;

    public WorkbookOnClick(Fragment fragment, WorkbookViewModel workbookViewModel, Uri uri) {
      this.context = fragment.getContext();
      this.fragment = fragment;
      this.fragmentManager = fragment.getParentFragmentManager();
      this.workbookViewModel = workbookViewModel;
      this.uri = uri;
    }

    @Override
    public void onClick(View view) {
      workbookViewModel.downloadWorkbook(uri.toString())
        .observe((LifecycleOwner) context, new Observer<ResponseBody>() {
        @Override
        public void onChanged(ResponseBody responseBody) {
          if (responseBody != null) {
            DialogFragment dialogFragment;
            
            MyWorkbooksFragment myWorkbooksFragment = (MyWorkbooksFragment) fragment;
            dialogFragment = new MyWorkbooksModalFragment();
            dialogFragment.setTargetFragment(myWorkbooksFragment, myWorkbooksFragment.getREQUEST_CODE());
            dialogFragment.show(fragmentManager, uri.toString());
          } else {
            Toast.makeText(context, "Download Failed", Toast.LENGTH_LONG).show();;
          }
        }
      });
    }
  }
}
