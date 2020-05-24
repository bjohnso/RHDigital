package com.rhdigital.rhclient.activities.user.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.rhdigital.rhclient.R;
import com.rhdigital.rhclient.common.services.NavigationService;

public class UserProfileEditFragment extends Fragment {

  private Button firstNameButton;
  private Button lastNameButton;
  private Button titleButton;
  private Button cityButton;
  private Button countryButton;
  private Button aboutButton;
  private Button industryButton;
  private Button birthdayButton;
  private ImageView backButton;

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.user_profile_edit_layout, container, false);

    //Initialise View Components
    backButton = view.findViewById(R.id.user_profile_edit_back_button);
    firstNameButton = view.findViewById(R.id.user_profile_edit_first_name_button);
    lastNameButton = view.findViewById(R.id.user_profile_edit_last_name_button);
    titleButton = view.findViewById(R.id.user_profile_edit_title_button);
    cityButton = view.findViewById(R.id.user_profile_edit_city_button);
    countryButton = view.findViewById(R.id.user_profile_edit_country_button);
    aboutButton = view.findViewById(R.id.user_profile_edit_about_button);
    industryButton = view.findViewById(R.id.user_profile_edit_industry_button);
    birthdayButton = view.findViewById(R.id.user_profile_edit_birthday_button);

    //Set Listeners
    backButton.setOnClickListener(new BackButtonOnClick(getActivity().getLocalClassName()));

    EditOnClick editOnClick = new EditOnClick(getChildFragmentManager());
    firstNameButton.setOnClickListener(editOnClick);
    lastNameButton.setOnClickListener(editOnClick);
    titleButton.setOnClickListener(editOnClick);
    cityButton.setOnClickListener(editOnClick);
    countryButton.setOnClickListener(editOnClick);
    aboutButton.setOnClickListener(editOnClick);
    industryButton.setOnClickListener(editOnClick);
    birthdayButton.setOnClickListener(editOnClick);

    return view;
  }

  public static class BackButtonOnClick implements View.OnClickListener {

    private String className;

    public BackButtonOnClick(String className) {
      this.className = className;
    }

    @Override
    public void onClick(View view) {
      NavigationService.getINSTANCE().navigate(className,
        R.id.userProfileFragment,
        null);
    }
  }

  public static class EditOnClick implements View.OnClickListener {

    FragmentManager fragmentManager;

    public EditOnClick(FragmentManager fragmentManager) {
      this.fragmentManager = fragmentManager;
    }

    @Override
    public void onClick(View view) {
      DialogFragment dialogFragment;
      Bundle args;
      switch (view.getId()) {
        case R.id.user_profile_edit_first_name_button:
          dialogFragment = new UserProfileEditModalFragment();
          args = new Bundle();
          args.putString("PROPERTY_NAME", "First Name");
          dialogFragment.setArguments(args);
          dialogFragment.show(fragmentManager, "First Name");
          break;
        case R.id.user_profile_edit_last_name_button:
          dialogFragment = new UserProfileEditModalFragment();
          args = new Bundle();
          args.putString("PROPERTY_NAME", "Last Name");
          dialogFragment.setArguments(args);
          dialogFragment.show(fragmentManager, "Last Name");
          break;
        case R.id.user_profile_edit_title_button:
          dialogFragment = new UserProfileEditModalFragment();
          args = new Bundle();
          args.putString("PROPERTY_NAME", "Title");
          dialogFragment.setArguments(args);
          dialogFragment.show(fragmentManager, "Title");
          break;
        case R.id.user_profile_edit_city_button:
          dialogFragment = new UserProfileEditModalFragment();
          args = new Bundle();
          args.putString("PROPERTY_NAME", "City");
          dialogFragment.setArguments(args);
          dialogFragment.show(fragmentManager, "City");
          break;
        case R.id.user_profile_edit_country_button:
          break;
        case R.id.user_profile_edit_about_button:
          dialogFragment = new UserProfileEditModalFragment();
          args = new Bundle();
          args.putString("PROPERTY_NAME", "About");
          dialogFragment.setArguments(args);
          dialogFragment.show(fragmentManager, "About");
          break;
        case R.id.user_profile_edit_industry_button:
          dialogFragment = new UserProfileEditModalFragment();
          args = new Bundle();
          args.putString("PROPERTY_NAME", "Industry");
          dialogFragment.setArguments(args);
          dialogFragment.show(fragmentManager, "Industry");
          break;
        case R.id.user_profile_edit_birthday_button:
          break;
      }
    }
  }
}
