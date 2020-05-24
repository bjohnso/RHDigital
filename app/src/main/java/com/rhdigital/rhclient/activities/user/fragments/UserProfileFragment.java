package com.rhdigital.rhclient.activities.user.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.rhdigital.rhclient.R;
import com.rhdigital.rhclient.activities.courses.CoursesActivity;
import com.rhdigital.rhclient.common.services.NavigationService;

public class UserProfileFragment extends Fragment {

  ImageButton backButton;
  Button editProfileButton;

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.user_profile_layout, container, false);

    //Initialise View Components
    backButton = view.findViewById(R.id.user_profile_back_button);
    editProfileButton = view.findViewById(R.id.user_profile_edit_profile_button);

    //Set Listeners
    backButton.setOnClickListener(new BackButtonOnClick(getContext()));
    editProfileButton.setOnClickListener(new EditProfileOnClick(getActivity().getLocalClassName()));

    return view;
  }

  public static class EditProfileOnClick implements View.OnClickListener {

    private String className;

    public EditProfileOnClick (String className) {
      this.className = className;
    }

    @Override
    public void onClick(View view) {
      NavigationService.getINSTANCE().navigate(className, R.id.userProfileEditFragment, null);
    }
  }

  public static class BackButtonOnClick implements View.OnClickListener {

    private Context context;

    public BackButtonOnClick(Context context) {
      this.context = context;
    }

    @Override
    public void onClick(View view) {
      Intent intent = new Intent(context, CoursesActivity.class);
      context.startActivity(intent);
    }
  }
}
