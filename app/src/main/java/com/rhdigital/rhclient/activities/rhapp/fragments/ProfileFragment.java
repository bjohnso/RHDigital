package com.rhdigital.rhclient.activities.rhapp.fragments;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseAuth;
import com.rhdigital.rhclient.R;
import com.rhdigital.rhclient.RHApplication;
import com.rhdigital.rhclient.activities.rhapp.RHAppActivity;
import com.rhdigital.rhclient.activities.rhapp.viewmodel.ProfileViewModel;
import com.rhdigital.rhclient.activities.rhapp.viewmodel.RHAppViewModel;
import com.rhdigital.rhclient.common.services.NavigationService;
import com.rhdigital.rhclient.room.model.User;
import com.rhdigital.rhclient.databinding.FragmentProfileBinding;

import java.util.HashMap;

public class ProfileFragment extends RHAppFragment {

    private final String TAG = "USER_FRAGMENT";

    private RHAppActivity activity;

    // VIEW
    private FragmentProfileBinding binding;

    // VIEW MODEL
    private ProfileViewModel profileViewModel;
    private RHAppViewModel rhAppViewModel;

    // OBSERVABLES
    private LiveData<User> userObservable;
    private LiveData<HashMap<String, Bitmap>> userProfilePhotoObservable;

    // OBSERVERS
    private Observer<User> userObserver;
    private Observer<HashMap<String, Bitmap>> userProfilePhotoObserver;

    private int width = 0;
    private int height = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(getLayoutInflater());

        rhAppViewModel = new ViewModelProvider(getActivity()).get(RHAppViewModel.class);
        profileViewModel = new ViewModelProvider(getActivity()).get(ProfileViewModel.class);
        profileViewModel.init();
        binding.setViewModel(profileViewModel);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        initialiseLiveData();
        initialiseUI();
        initialiseClickListeners();

        return binding.getRoot();
    }

    @Override
    public void onPause() {
        hideShimmer();
        super.onPause();
    }

    private void showShimmer() {
        binding.profileImage.setVisibility(View.GONE);
        binding.shimmerContainer.setVisibility(View.VISIBLE);
        binding.shimmerContainer.startShimmer();
    }

    private void hideShimmer() {
        binding.shimmerContainer.stopShimmer();
        binding.shimmerContainer.setVisibility(View.GONE);
        binding.profileImage.setVisibility(View.VISIBLE);
    }

    @Override
    public void onStart() {
        super.onStart();
        this.activity = (RHAppActivity) getActivity();
        ((RHApplication)activity.getApplication()).setCurrentFragment(this);
    }

    private void initialiseUI() {
        binding.profileImage.getViewTreeObserver()
                .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
          @Override
          public void onGlobalLayout() {
            if (binding.profileImage.getHeight() > 0 && binding.profileImage.getWidth() > 0) {
                binding.profileImage.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                userProfilePhotoObservable = profileViewModel
                        .getProfilePhoto(
                                getContext(),
                  FirebaseAuth.getInstance().getUid(),
                  binding.profileImage.getWidth(),
                  binding.profileImage.getHeight());
              userProfilePhotoObservable.observe(getActivity(), userProfilePhotoObserver);
              showShimmer();
            }
          }
        });
        calculateImageDimensions();
    }

    private void initialiseLiveData() {
        userProfilePhotoObserver = map -> {
            hideShimmer();
            if (map != null && map.get(FirebaseAuth.getInstance().getUid()) != null) {
                binding.profileImage.setImageBitmap(
                        map.get(FirebaseAuth.getInstance().getUid())
                );
            }
        };

        userObserver = new Observer<User>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onChanged(User u) {
                if (u != null) {
                    userObservable.removeObserver(this);
                    profileViewModel.user.setValue(u);
                    profileViewModel.initUserFieldMap(u);
                } else {
                    Toast.makeText(getContext(), R.string.server_error_user, Toast.LENGTH_LONG).show();
                }
            }
        };

        userObservable = profileViewModel.getUser(FirebaseAuth.getInstance().getUid());
        userObservable.observe(getViewLifecycleOwner(), userObserver);
    }

    private void initialiseClickListeners() {
        binding.profileImage.setOnClickListener(view -> {
            openImageChooser();
        });
        binding.buttonEditProfile.setOnClickListener(view -> {
            NavigationService.getINSTANCE()
                    .navigate(
                            getActivity().getLocalClassName(),
                            R.id.editProfileFragment, null, null
                    );
        });
        binding.buttonSettings.setOnClickListener(view ->
                Toast.makeText(getContext(), getResources().getString(R.string.description_coming_soon), Toast.LENGTH_LONG).show()
        );
        binding.buttonAbout.setOnClickListener(view ->
                Toast.makeText(getContext(), getResources().getString(R.string.description_coming_soon), Toast.LENGTH_LONG).show()
        );
        binding.buttonPrivacyPolicy.setOnClickListener(view ->
                Toast.makeText(getContext(), getResources().getString(R.string.description_coming_soon), Toast.LENGTH_LONG).show()
        );
        binding.buttonLogout.setOnClickListener(view -> {
            RHAppActivity activity = (RHAppActivity) getActivity();
            activity.logout();
        });
    }

    private void openImageChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        String mimeTypes[] = {"image/png", "image/jpeg"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        getActivity().startActivityForResult(intent, RHAppActivity.IMAGE_PICKER_CODE);
    }

    private void calculateImageDimensions() {
        float dip = 220f;
        Resources r = getResources();
        float px = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dip,
                r.getDisplayMetrics()
        );

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        width = displayMetrics.widthPixels;
        height = Math.round(px);
    }

    @Override
    public void onImageUpload() {
        showShimmer();
        userProfilePhotoObservable = profileViewModel
                .getProfilePhoto(
                        getContext(),
                        FirebaseAuth.getInstance().getUid(),
                        binding.profileImage.getWidth(),
                        binding.profileImage.getHeight());
        userProfilePhotoObservable.observe(getActivity(), userProfilePhotoObserver);
    }

    @Override
    public void onDestroy() {
        ((RHApplication)activity.getApplication()).setCurrentFragment(null);
        super.onDestroy();
    }
}
