package com.rhdigital.rhclient.activities.user.fragments;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;

import com.rhdigital.rhclient.R;
import com.rhdigital.rhclient.activities.user.UserActivity;
import com.rhdigital.rhclient.common.services.NavigationService;
import com.rhdigital.rhclient.database.model.User;
import com.rhdigital.rhclient.database.viewmodel.UserViewModel;

import java.util.HashMap;

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
  private Button saveButton;
  private ImageView backButton;

  private HashMap<Integer, String> buttonIdMap = new HashMap<>();
  private HashMap<String, TextView> textViewMap = new HashMap<>();
  private User user;

  private LiveData<User> userObservable;

  public UserProfileEditFragment() {
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.user_profile_edit_layout, container, false);

    //Initialise View Components
    saveButton = view.findViewById(R.id.user_profile_edit_save_button);
    backButton = view.findViewById(R.id.user_profile_edit_back_button);
    firstNameButton = view.findViewById(R.id.user_profile_edit_first_name_button);
    lastNameButton = view.findViewById(R.id.user_profile_edit_last_name_button);
    titleButton = view.findViewById(R.id.user_profile_edit_title_button);
    cityButton = view.findViewById(R.id.user_profile_edit_city_button);
    countryButton = view.findViewById(R.id.user_profile_edit_country_button);
    aboutButton = view.findViewById(R.id.user_profile_edit_about_button);
    industryButton = view.findViewById(R.id.user_profile_edit_industry_button);
    //birthdayButton = view.findViewById(R.id.user_profile_edit_birthday_button);

    //Initialise EditText Map
    textViewMap.put("First Name", view.findViewById(R.id.user_edit_first_name_value));
    textViewMap.put("Last Name", view.findViewById(R.id.user_edit_last_name_value));
    textViewMap.put("Title", view.findViewById(R.id.user_edit_title_value));
    textViewMap.put("City", view.findViewById(R.id.user_edit_city_value));
    textViewMap.put("Country", view.findViewById(R.id.user_edit_country_value));
    textViewMap.put("About", view.findViewById(R.id.user_edit_about_value));
    textViewMap.put("Industry", view.findViewById(R.id.user_edit_industry_value));
    //textViewMap.put("Date of Birth", view.findViewById(R.id.user_edit_birthday_value));

    //Initialise Button ID Map
    buttonIdMap.put(R.id.user_profile_edit_first_name_button, "First Name");
    buttonIdMap.put(R.id.user_profile_edit_last_name_button, "Last Name");
    buttonIdMap.put(R.id.user_profile_edit_title_button, "Title");
    buttonIdMap.put(R.id.user_profile_edit_city_button, "City");
    buttonIdMap.put(R.id.user_profile_edit_country_button, "Country");
    buttonIdMap.put(R.id.user_profile_edit_about_button, "About");
    buttonIdMap.put(R.id.user_profile_edit_industry_button, "Industry");
    //buttonIdMap.put(R.id.user_profile_edit_birthday_button, "Date of Birth");

    //Set Listeners
    backButton.setOnClickListener(new BackButtonOnClick(this, getActivity().getLocalClassName()));
    saveButton.setOnClickListener(new SaveButtonOnClick(this));

    EditOnClick editOnClick = new EditOnClick(this,
      getParentFragmentManager(),
      buttonIdMap,
      textViewMap);
    firstNameButton.setOnClickListener(editOnClick);
    lastNameButton.setOnClickListener(editOnClick);
    titleButton.setOnClickListener(editOnClick);
    cityButton.setOnClickListener(editOnClick);
    countryButton.setOnClickListener(editOnClick);
    aboutButton.setOnClickListener(editOnClick);
    industryButton.setOnClickListener(editOnClick);
    //birthdayButton.setOnClickListener(editOnClick);

    userObservable = ((UserActivity)getActivity()).getUser();

    user = userObservable.getValue();
    if (user != null) {
      initialiseTextViewValues(user);
    }

    userObservable.observe(getViewLifecycleOwner(), u -> {
      //Set text to edit fields
      initialiseTextViewValues(u);
      user = u;
    });

    return view;
  }

  public User getUser() {
    return user;
  }

  public HashMap<Integer, String> getButtonIdMap() {
    return buttonIdMap;
  }

  public HashMap<String, TextView> getTextViewMap() {
    return textViewMap;
  }

  private void initialiseTextViewValues(User user) {
    if (user != null) {
      String firstName = user.getName();
      String lastName = user.getSurname();
      String title = user.getTitle();
      String city = user.getCity();
      String country = user.getCountry();
      String about = user.getAbout();
      String industry = user.getIndustry();

      if (firstName != null && !firstName.isEmpty()) {
        textViewMap.get("First Name").setVisibility(View.VISIBLE);
        textViewMap.get("First Name").setText(firstName);
      }
      if (lastName != null && !lastName.isEmpty()) {
        textViewMap.get("Last Name").setVisibility(View.VISIBLE);
        textViewMap.get("Last Name").setText(lastName);
      }
      if (title != null && !title.isEmpty()) {
        textViewMap.get("Title").setVisibility(View.VISIBLE);
        textViewMap.get("Title").setText(title);
      }
      if (city != null && !city.isEmpty()) {
        textViewMap.get("City").setVisibility(View.VISIBLE);
        textViewMap.get("City").setText(city);
      }
      if (country != null && !country.isEmpty()) {
        textViewMap.get("Country").setVisibility(View.VISIBLE);
        textViewMap.get("Country").setText(country);
      }
      if (about != null && !about.isEmpty()) {
        textViewMap.get("About").setVisibility(View.VISIBLE);
        textViewMap.get("About").setText(about);
      }
      if (industry != null && !industry.isEmpty()) {
        textViewMap.get("Industry").setVisibility(View.VISIBLE);
        textViewMap.get("Industry").setText(industry);
      }
    }
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    if (resultCode != Activity.RESULT_OK) {
      return ;
    }

    if (requestCode == REQUEST_CODE) {
      String propertyName = data.getStringExtra("PROPERTY_NAME");
      String propertyValue = data.getStringExtra("PROPERTY_VALUE");

      if (propertyName.equalsIgnoreCase("CONFIRM")) {
        if (propertyValue.equalsIgnoreCase("SAVE")) {
          saveButton.performClick();
          backButton.performClick();
        } else {
          NavigationService.getINSTANCE().navigate(getActivity().getLocalClassName(),
            R.id.userProfileFragment,
            null);
        }
      } else {
        TextView textView = textViewMap.get(propertyName);

        Log.d("USEREDIT", "PROPERTY_NAME : " + propertyName + "\nPROPERTY_VALUE : " + propertyValue);
        textView.setText(propertyValue);
        if (!propertyValue.isEmpty())
          textView.setVisibility(View.VISIBLE);
        else
          textView.setVisibility(View.GONE);
      }
    }
  }

  public int getREQUEST_CODE() {
    return REQUEST_CODE;
  }

  public static class BackButtonOnClick implements View.OnClickListener {

    private UserProfileEditFragment userProfileEditFragment;
    private String className;
    private HashMap<String, TextView> textViewMap;

    public BackButtonOnClick(UserProfileEditFragment userProfileEditFragment, String className) {
      this.userProfileEditFragment = userProfileEditFragment;
      this.className = className;
      this.textViewMap = userProfileEditFragment.getTextViewMap();
    }

    @Override
    public void onClick(View view) {
      User user = userProfileEditFragment.getUser();

      if (!user.getName().equals(textViewMap.get("First Name").getText().toString())
        || !user.getSurname().equals(textViewMap.get("Last Name").getText().toString())
        || !user.getTitle().equals(textViewMap.get("Title").getText().toString())
        || !user.getCity().equals(textViewMap.get("City").getText().toString())
        || !user.getCountry().equals(textViewMap.get("Country").getText().toString())
        || !user.getAbout().equals(textViewMap.get("About").getText().toString())
        || !user.getIndustry().equals(textViewMap.get("Industry").getText().toString())) {
        DialogFragment dialogFragment;
        Bundle args = new Bundle();
        args.putString("PROPERTY_NAME", "CONFIRM");
        dialogFragment = new UserProfileEditModalFragment();
        dialogFragment.setArguments(args);
        dialogFragment.setTargetFragment(userProfileEditFragment, userProfileEditFragment.getREQUEST_CODE());
        dialogFragment.show(userProfileEditFragment.getParentFragmentManager(), "CONFIRM");
      } else {
        NavigationService.getINSTANCE().navigate(className,
          R.id.userProfileFragment,
          null);
      }
    }
  }

  public static class EditOnClick implements View.OnClickListener {
    private Fragment fragment;
    private FragmentManager fragmentManager;
    private HashMap<Integer, String> buttonIdMap;
    private HashMap<String, TextView> editTextMap;

    public EditOnClick(Fragment fragment, FragmentManager fragmentManager, HashMap<Integer, String> buttonIdMap, HashMap<String, TextView> editTextMap) {
      this.fragment = fragment;
      this.fragmentManager = fragmentManager;
      this.buttonIdMap = buttonIdMap;
      this.editTextMap = editTextMap;
    }

    @Override
    public void onClick(View view) {
      DialogFragment dialogFragment;
      Bundle args;

//      if (view.getId() == R.id.user_profile_edit_birthday_button) {
//
//      } else {
        args = new Bundle();
        String propertyName = buttonIdMap.get(view.getId());
        String propertyValue = editTextMap.get(propertyName).getText().toString();
        args.putString("PROPERTY_NAME", propertyName);
        args.putString("PROPERTY_VALUE", propertyValue);
        UserProfileEditFragment userProfileEditFragment = (UserProfileEditFragment) fragment;
        dialogFragment = new UserProfileEditModalFragment();
        dialogFragment.setArguments(args);
        dialogFragment.setTargetFragment(userProfileEditFragment, userProfileEditFragment.getREQUEST_CODE());
        dialogFragment.show(fragmentManager, buttonIdMap.get(view.getId()));
      //}
    }
  }

  public static class SaveButtonOnClick implements View.OnClickListener {
    UserProfileEditFragment userProfileEditFragment;
    UserActivity userActivity;
    UserViewModel userViewModel;
    User user;
    HashMap<String, TextView> textViewMap;

    public SaveButtonOnClick(UserProfileEditFragment userProfileEditFragment) {
      this.userProfileEditFragment = userProfileEditFragment;
      this.userActivity = (UserActivity) userProfileEditFragment.getActivity();
      this.userViewModel = userActivity.getUserViewModel();
      this.textViewMap = userProfileEditFragment.getTextViewMap();
    }

    @Override
    public void onClick(View view) {
      // Update user object
      this.user = userProfileEditFragment.getUser();
      user.setName(textViewMap.get("First Name").getText().toString());
      user.setSurname(textViewMap.get("Last Name").getText().toString());
      user.setTitle(textViewMap.get("Title").getText().toString());
      user.setCity(textViewMap.get("City").getText().toString());
      user.setCountry(textViewMap.get("Country").getText().toString());
      user.setAbout(textViewMap.get("About").getText().toString());
      user.setIndustry(textViewMap.get("Industry").getText().toString());

      userActivity.updateUser(user, userProfileEditFragment.getContext());
    }
  }
}
