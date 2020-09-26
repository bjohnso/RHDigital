//package com.rhdigital.rhclient.activities.user.fragments;
//
//import android.app.Activity;
//import android.app.Dialog;
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ListView;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.DialogFragment;
//
//import com.rhdigital.rhclient.R;
//import com.rhdigital.rhclient.common.view.CustomArrayAdapter;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Locale;
//
//public class UserProfileEditModalFragment extends DialogFragment {
//
//  private String propertyNameText;
//  private TextView propertyName;
//  private EditText editText;
//  private ListView listView;
//  private Button cancelButton;
//  private Button okButton;
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
//      View view;
//      if ((propertyNameText = getArguments()
//        .getString("PROPERTY_NAME"))
//        .equalsIgnoreCase("CONFIRM")) {
//        view = inflater.inflate(R.layout.user_profile_edit_modal_confirm_dialog_layout, container, false);
//        cancelButton = view.findViewById(R.id.user_profile_edit_modal_confirm_save_button);
//        okButton = view.findViewById(R.id.user_profile_edit_modal_confirm_discard_button);
//
//        ButtonOnClick buttonOnClick = new ButtonOnClick(this);
//
//        cancelButton.setOnClickListener(buttonOnClick);
//        okButton.setOnClickListener(buttonOnClick);
//      }
//      else if (!(propertyNameText = getArguments()
//        .getString("PROPERTY_NAME"))
//        .equalsIgnoreCase("Country")) {
//
//        view = inflater.inflate(R.layout.user_profile_edit_modal_simple_layout, container, false);
//        propertyName = view.findViewById(R.id.user_profile_edit_modal_property_name);
//        cancelButton = view.findViewById(R.id.user_profile_edit_modal_cancel_button);
//        okButton = view.findViewById(R.id.user_profile_modal_ok_button);
//        editText = view.findViewById(R.id.user_profile_edit_modal_input);
//
//        propertyName.setText(propertyNameText);
//        editText.setText(getArguments().getString("PROPERTY_VALUE"));
//
//        ButtonOnClick buttonOnClick = new ButtonOnClick(this);
//
//        cancelButton.setOnClickListener(buttonOnClick);
//        okButton.setOnClickListener(buttonOnClick);
//      } else {
//        view = inflater.inflate(R.layout.user_profile_edit_modal_list_layout, container, false);
//        propertyName = view.findViewById(R.id.user_profile_edit_modal_property_name);
//        propertyName.setText(propertyNameText);
//
//        Locale[] locales = Locale.getAvailableLocales();
//        ArrayList<String> countries = new ArrayList<>();
//
//        for (Locale l: locales) {
//          String country = l.getDisplayCountry();
//          if (country.trim().length() > 0 && !countries.contains(country))
//            countries.add(l.getDisplayCountry());
//        }
//        Collections.sort(countries);
//
//        String propertyValue = getArguments().getString("PROPERTY_VALUE");
//        listView = view.findViewById(R.id.user_profile_edit_modal_list);
//        CustomArrayAdapter arrayAdapter = new CustomArrayAdapter(getContext(),
//          this,
//          countries,
//          propertyValue);
//        listView.setAdapter(arrayAdapter);
//        getArguments().putInt("CURRENT_COUNTRY_INDEX", countries.indexOf(getArguments().getString("PROPERTY_VALUE")));
//      }
//      return view;
//    }
//    return null;
//  }
//
//  @Override
//  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//    super.onViewCreated(view, savedInstanceState);
//    if (getArguments().getString("PROPERTY_NAME").equalsIgnoreCase("Country")
//      && getArguments().getString("PROPERTY_VALUE") != null) {
//      int currentCountryIndex = getArguments().getInt("CURRENT_COUNTRY_INDEX");
//      listView.setSelection(currentCountryIndex);
//    }
//  }
//
//  public void sendDataToParent(String value) {
//    if (getTargetFragment() == null) {
//      this.dismiss();
//      return;
//    }
//    if (value == null) {
//      value = editText.getText().toString();
//    }
//    Intent intent = new Intent();
//    intent.putExtra("PROPERTY_NAME", propertyNameText);
//    intent.putExtra("PROPERTY_VALUE", value);
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
//      if (dialogFragment.getArguments().getString("PROPERTY_NAME").equalsIgnoreCase("CONFIRM")) {
//        if (view.getId() == R.id.user_profile_edit_modal_confirm_save_button) {
//          ((UserProfileEditModalFragment) dialogFragment).sendDataToParent("SAVE");
//        } else if (view.getId() == R.id.user_profile_edit_modal_confirm_discard_button) {
//          ((UserProfileEditModalFragment) dialogFragment).sendDataToParent("DISCARD");
//        }
//      } else {
//        if (view.getId() == R.id.user_profile_modal_ok_button) {
//          ((UserProfileEditModalFragment) dialogFragment).sendDataToParent(null);
//        } else if (view.getId() == R.id.user_profile_edit_modal_cancel_button) {
//          dialogFragment.dismiss();
//        }
//      }
//    }
//  }
//}
