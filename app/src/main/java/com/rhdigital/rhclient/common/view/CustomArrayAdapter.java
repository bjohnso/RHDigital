package com.rhdigital.rhclient.common.view;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.rhdigital.rhclient.R;
import com.rhdigital.rhclient.activities.user.fragments.UserProfileEditModalFragment;

import java.util.ArrayList;
import java.util.List;

public class CustomArrayAdapter extends ArrayAdapter {

  private UserProfileEditModalFragment userProfileEditModalFragment;
  private Context context;
  private List<String> objects;
  private String currentSelection;

  public CustomArrayAdapter(@NonNull Context context, Fragment fragment, ArrayList<String> objects, String current) {
    super(context, 0, objects);
    this.userProfileEditModalFragment = (UserProfileEditModalFragment)fragment;
    this.context = context;
    this.objects = objects;
    this.currentSelection = current;
  }

  @NonNull
  @Override
  public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
    View listItem = LayoutInflater.from(context).inflate(
        R.layout.user_profile_edit_modal_list_item_layout,
        parent,
        false);

    LinearLayout linearLayout = listItem.findViewById(R.id.user_profile_edit_modal_list_item_country);
    RadioButton radioButton = listItem.findViewById(R.id.user_profile_edit_modal_list_item_radiobutton);
    TextView textView = listItem.findViewById(R.id.user_profile_edit_modal_list_item_text);
    textView.setText(objects.get(position));

    if (objects.get(position).equalsIgnoreCase(currentSelection)){
      Log.d("ARRAYADAPTER", currentSelection);
      radioButton.toggle();
    }

    linearLayout.setOnClickListener(new CountryOnClick(userProfileEditModalFragment));

    return listItem;
  }

  public static class CountryOnClick implements View.OnClickListener {

    private UserProfileEditModalFragment userProfileEditModalFragment;

    public CountryOnClick(Fragment fragment) {
      userProfileEditModalFragment = (UserProfileEditModalFragment) fragment;
    }

    @Override
    public void onClick(View view) {
      TextView textView = (TextView)view.findViewById(R.id.user_profile_edit_modal_list_item_text);
      userProfileEditModalFragment.sendDataToParent(textView.getText().toString());
    }
  }
}
