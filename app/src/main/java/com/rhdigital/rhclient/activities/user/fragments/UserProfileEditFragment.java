package com.rhdigital.rhclient.activities.user.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.common.io.LineReader;
import com.rhdigital.rhclient.R;
import com.rhdigital.rhclient.common.services.NavigationService;

import java.util.HashMap;
import java.util.Map;

public class UserProfileEditFragment extends Fragment {

  private int REQUEST_CODE = 1;

  private LinearLayout firstNameButton;
  private LinearLayout lastNameButton;
  private LinearLayout titleButton;
  private LinearLayout cityButton;
  private LinearLayout countryButton;
  private LinearLayout aboutButton;
  private LinearLayout industryButton;
  private LinearLayout birthdayButton;
  private ImageView backButton;

  private HashMap<Integer, String> buttonIdMap = new HashMap<>();
  private HashMap<String, TextView> textViewMap = new HashMap<>();

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

    //Initialise EditText Map
    textViewMap.put("First Name", view.findViewById(R.id.user_edit_first_name_value));
    textViewMap.put("Last Name", view.findViewById(R.id.user_edit_last_name_value));
    textViewMap.put("Title", view.findViewById(R.id.user_edit_title_value));
    textViewMap.put("City", view.findViewById(R.id.user_edit_city_value));
    textViewMap.put("Country", view.findViewById(R.id.user_edit_country_value));
    textViewMap.put("About", view.findViewById(R.id.user_edit_about_value));
    textViewMap.put("Industry", view.findViewById(R.id.user_edit_industry_value));
    textViewMap.put("Date of Birth", view.findViewById(R.id.user_edit_birthday_value));

    //Initialise Button ID Map
    buttonIdMap.put(R.id.user_profile_edit_first_name_button, "First Name");
    buttonIdMap.put(R.id.user_profile_edit_last_name_button, "Last Name");
    buttonIdMap.put(R.id.user_profile_edit_title_button, "Title");
    buttonIdMap.put(R.id.user_profile_edit_city_button, "City");
    buttonIdMap.put(R.id.user_profile_edit_country_button, "Country");
    buttonIdMap.put(R.id.user_profile_edit_about_button, "About");
    buttonIdMap.put(R.id.user_profile_edit_industry_button, "Industry");
    buttonIdMap.put(R.id.user_profile_edit_birthday_button, "Date of Birth");

    //Set Listeners
    backButton.setOnClickListener(new BackButtonOnClick(getActivity().getLocalClassName()));

    EditOnClick editOnClick = new EditOnClick(this, getParentFragmentManager(), buttonIdMap);
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

  @Override
  public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    if (resultCode != Activity.RESULT_OK) {
      return ;
    }

    if (requestCode == REQUEST_CODE) {
      String propertyName = data.getStringExtra("PROPERTY_NAME");
      String propertyValue = data.getStringExtra("PROPERTY_VALUE");
      TextView textView = textViewMap.get(propertyName);

      Log.d("USEREDIT", "PROPERTY_NAME : " + propertyName + "\nPROPERTY_VALUE : " + propertyValue);
      textView.setVisibility(View.VISIBLE);
      textView.setText(propertyValue);
    }
  }

  public int getREQUEST_CODE() {
    return REQUEST_CODE;
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

    private Fragment fragment;
    private FragmentManager fragmentManager;
    private HashMap<Integer, String> buttonIdMap;

    public EditOnClick(Fragment fragment, FragmentManager fragmentManager, HashMap<Integer, String> buttonIdMap) {
      this.fragment = fragment;
      this.fragmentManager = fragmentManager;
      this.buttonIdMap = buttonIdMap;
    }

    @Override
    public void onClick(View view) {
      DialogFragment dialogFragment;
      Bundle args;

      if (view.getId() == R.id.user_profile_edit_country_button
        || view.getId() == R.id.user_profile_edit_birthday_button) {

      } else {
        args = new Bundle();
        args.putString("PROPERTY_NAME", buttonIdMap.get(view.getId()));
        UserProfileEditFragment userProfileEditFragment = (UserProfileEditFragment) fragment;
        dialogFragment = new UserProfileEditModalFragment();
        dialogFragment.setArguments(args);
        dialogFragment.setTargetFragment(userProfileEditFragment, userProfileEditFragment.getREQUEST_CODE());
        dialogFragment.show(fragmentManager, buttonIdMap.get(view.getId()));
      }
    }
  }
}
