package com.rhdigital.rhclient.activities.user.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.google.firebase.auth.FirebaseAuth;
import com.rhdigital.rhclient.R;
import com.rhdigital.rhclient.activities.courses.CoursesActivity;
import com.rhdigital.rhclient.activities.user.UserActivity;
import com.rhdigital.rhclient.common.services.NavigationService;
import com.rhdigital.rhclient.database.model.User;
import com.rhdigital.rhclient.database.viewmodel.UserViewModel;

public class UserProfileFragment extends Fragment {

  private LiveData<User> userObservable;
  private LiveData<Bitmap> userProfilePhotoObservable;
  private LiveData<Boolean> imageUploadObservable;
  private UserViewModel userViewModel;

  private ImageButton backButton;
  private ImageView profileImageButton;
  private TextView nameView;
  private TextView usernameView;
  private Button editProfileButton;
  private Button settingsButton;
  private Button privacyPolicyButton;
  private Button aboutPolicyButton;
  private Button logoutButton;

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.user_profile_layout, container, false);

    //Initialise View Components
    backButton = view.findViewById(R.id.user_profile_back_button);

    nameView = view.findViewById(R.id.user_profile_edit_name);
    nameView.setVisibility(View.GONE);
    usernameView = view.findViewById(R.id.user_profile_edit_username);
    usernameView.setVisibility(View.GONE);

    profileImageButton = view.findViewById(R.id.user_profile_edit_image_button);

    editProfileButton = view.findViewById(R.id.user_profile_edit_profile_button);
    settingsButton = view.findViewById(R.id.user_profile_settings_button);
    aboutPolicyButton = view.findViewById(R.id.user_profile_about_button);
    privacyPolicyButton = view.findViewById(R.id.user_profile_privacy_policy_button);
    logoutButton = view.findViewById(R.id.user_profile_logout_button);

    //Set Listeners
    backButton.setOnClickListener(new BackButtonOnClick(getContext()));
    profileImageButton.setOnClickListener(new ImageButtonOnClick(getContext()));
    editProfileButton.setOnClickListener(new EditProfileOnClick(getActivity().getLocalClassName()));
    settingsButton.setOnClickListener(new SettingsOnClick(getActivity().getLocalClassName()));
    aboutPolicyButton.setOnClickListener(new DocumentButtonOnClick(getContext(), "about"));
    privacyPolicyButton.setOnClickListener(new DocumentButtonOnClick(getContext(), "privacy_policy"));
    logoutButton.setOnClickListener(new UserProfileLogoutOnClick(getContext()));

    userViewModel = ((UserActivity)getActivity()).getUserViewModel();

    //Observers
    final Observer<Bitmap> userProfileImageObserver = new Observer<Bitmap>() {
      @Override
      public void onChanged(Bitmap bitmap) {
        if (bitmap != null) {
          profileImageButton.setImageBitmap(bitmap);
        } else {
          Toast.makeText(getContext(), R.string.server_error_user, Toast.LENGTH_LONG);
        }
      }
    };

    final Observer<User> userObserver = new Observer<User>() {
      @Override
      public void onChanged(User user) {
        if (user != null) {
          userObservable.removeObserver(this::onChanged);
          if (user.getName() != null && !user.getName().isEmpty()) {
            nameView.setText(user.getName());
            nameView.setVisibility(View.VISIBLE);
          }
          if (user.getEmail() != null && !user.getEmail().isEmpty()) {
            usernameView.setText(user.getEmail());
            usernameView.setVisibility(View.VISIBLE);
          }
        } else {
          Toast.makeText(getContext(), R.string.server_error_user, Toast.LENGTH_LONG);
        }
      }
    };

    //This Observer Reacts to an update on the upload status of an image, and triggers a new fetch of that image from the remote
    //Finally this triggers the userProfileImageObserver to set this new Bitmap to the View
    final Observer<Boolean> imageUploadObserver = status -> {
      if (status && profileImageButton.getWidth() > 0 && profileImageButton.getHeight() > 0) {
        if (userProfilePhotoObservable != null) {
          userProfilePhotoObservable.removeObservers(getActivity());
        }
        userProfilePhotoObservable = userViewModel
          .getProfilePhoto(getContext(),
            FirebaseAuth.getInstance().getUid(),
            profileImageButton.getWidth(),
            profileImageButton.getHeight());
        userProfilePhotoObservable.observe(getActivity(), userProfileImageObserver);
      } else if (!status) {
        Toast.makeText(getContext(), R.string.server_error_image_upload, Toast.LENGTH_LONG);
      }
    };

    profileImageButton.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
      @Override
      public void onGlobalLayout() {
        if (profileImageButton.getHeight() > 0 && profileImageButton.getWidth() > 0) {
          profileImageButton.getViewTreeObserver().removeOnGlobalLayoutListener(this::onGlobalLayout);
          userProfilePhotoObservable = userViewModel
            .getProfilePhoto(getContext(),
              FirebaseAuth.getInstance().getUid(),
              profileImageButton.getWidth(),
              profileImageButton.getHeight());
          userProfilePhotoObservable.observe(getActivity(), userProfileImageObserver);
        }
      }
    });

    userObservable = userViewModel.getAuthenticatedUser(FirebaseAuth.getInstance().getUid());
    userObservable.observe(getActivity(), userObserver);

    imageUploadObservable = ((UserActivity)getActivity()).getImageUploadObservable();
    imageUploadObservable.observe(getActivity(), imageUploadObserver);

    return view;
  }

  public static class ImageButtonOnClick implements View.OnClickListener {
    private Context context;

    public ImageButtonOnClick(Context context) {
      this.context = context;
    }

    @Override
    public void onClick(View view) {
      ((UserActivity)context).openImageChooser();
    }
  }

  public static class DocumentButtonOnClick implements View.OnClickListener {
    private Context context;
    private String endpoint;

    public DocumentButtonOnClick(Context context, String endpoint) {
      this.context = context;
      this.endpoint = endpoint;
    }

    @Override
    public void onClick(View view) {
      Uri uri = ((UserActivity)context).getDocumentUri(endpoint);
      context.startActivity(new Intent(Intent.ACTION_VIEW, uri));
    }
  }

  public static class UserProfileLogoutOnClick implements View.OnClickListener {

    private Context context;

    public UserProfileLogoutOnClick(Context context) {
      this.context = context;
    }

    @Override
    public void onClick(View view) {
      ((UserActivity)context).logout();
    }
  }

  public static class EditProfileOnClick implements View.OnClickListener {

    private String className;

    public EditProfileOnClick (String className) {
      this.className = className;
    }

    @Override
    public void onClick(View view) {
      NavigationService.getINSTANCE().navigate(className, R.id.userProfileEditFragment, null, null);
    }
  }

  public static class SettingsOnClick implements View.OnClickListener {

    private String className;

    public SettingsOnClick (String className) {
      this.className = className;
    }

    @Override
    public void onClick(View view) {
      NavigationService.getINSTANCE().navigate(className, R.id.userProfileSettingsFragment, null,null);
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
