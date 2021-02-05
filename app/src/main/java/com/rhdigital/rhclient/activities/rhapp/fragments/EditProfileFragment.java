package com.rhdigital.rhclient.activities.rhapp.fragments;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.rhdigital.rhclient.R;
import com.rhdigital.rhclient.RHApplication;
import com.rhdigital.rhclient.activities.rhapp.RHAppActivity;
import com.rhdigital.rhclient.activities.rhapp.adapters.EditProfileRecyclerViewAdapter;
import com.rhdigital.rhclient.activities.rhapp.delegates.EditProfileDialogDelegate;
import com.rhdigital.rhclient.activities.rhapp.dialogs.EditProfileConfirmDialog;
import com.rhdigital.rhclient.activities.rhapp.dialogs.EditProfileDialog;
import com.rhdigital.rhclient.activities.rhapp.viewmodel.ProfileViewModel;
import com.rhdigital.rhclient.common.dto.UserFieldDto;
import com.rhdigital.rhclient.common.interfaces.OnClickCallback;
import com.rhdigital.rhclient.common.services.NavigationService;
import com.rhdigital.rhclient.databinding.FragmentEditProfileBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class EditProfileFragment extends RHAppFragment {

    private final String TAG = "EDIT_USER_FRAGMENT";

    private RHAppActivity activity;

    // VIEW
    private FragmentEditProfileBinding binding;

    // VIEW MODEL
    private ProfileViewModel profileViewModel;

    private EditProfileRecyclerViewAdapter basicAdapter;

    private int width = 0;
    private int height = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentEditProfileBinding.inflate(getLayoutInflater());

        profileViewModel = new ViewModelProvider(getActivity()).get(ProfileViewModel.class);
        profileViewModel.initEdit();
        binding.setViewModel(profileViewModel);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        initialiseUI();
        initialiseRecyclerViews();

        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        this.activity = (RHAppActivity) getActivity();
        ((RHApplication)activity.getApplication()).setCurrentFragment(this);
    }

    private void initialiseUI() {
        calculateImageDimensions();
    }

    private void saveUserDetails() {
        profileViewModel.updateRemoteUser()
                .addOnCompleteListener(result -> {
                    if (result.isSuccessful()) {
                        Toast.makeText(
                                getActivity(),
                                getResources().getString(R.string.user_update),
                                Toast.LENGTH_LONG
                        ).show();
                        profileViewModel.fetchRemoteUser().observe(getViewLifecycleOwner(), inserts -> navigateBack());
                    } else {
                        Toast.makeText(
                                getActivity(),
                                getResources().getString(R.string.server_error_user_update),
                                Toast.LENGTH_LONG
                        ).show();
                    }
                });
    }

    private void presentConfirmationDialog() {
        EditProfileDialogDelegate delegate = new EditProfileDialogDelegate() {
            @Override
            public void onComplete(UserFieldDto userField) { }

            @Override
            public void onComplete(Boolean shouldSave) {
                if (shouldSave) {
                    saveUserDetails();
                } else {
                    navigateBack();
                }
            }
        };

        EditProfileConfirmDialog dialog = new EditProfileConfirmDialog(delegate);
        dialog.show(getParentFragmentManager(), "edit_profile_confirm");
    }

    private void initialiseRecyclerViews() {
        OnClickCallback callback = (args) -> {

            EditProfileDialogDelegate delegate = new EditProfileDialogDelegate() {
                @Override
                public void onComplete(UserFieldDto userField) {
                    profileViewModel.updateUserFieldMap(userField);
                    basicAdapter.updateData(profileViewModel.userFieldMap);
                }

                @Override
                public void onComplete(Boolean shouldSave) { }
            };

            UserFieldDto userField = (UserFieldDto) args[0];
            EditProfileDialog dialog = new EditProfileDialog(delegate, userField);
            dialog.show(getParentFragmentManager(), "edit_profile_dialog");
        };

        basicAdapter = new EditProfileRecyclerViewAdapter();
        binding.recyclerViewBasic.setHasFixedSize(true);
        binding.recyclerViewBasic.setItemViewCacheSize(10);
        binding.recyclerViewBasic.setDrawingCacheEnabled(true);
        binding.recyclerViewBasic.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_AUTO);
        binding.recyclerViewBasic.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerViewBasic.setAdapter(basicAdapter);

        basicAdapter.updateData(profileViewModel.userFieldMap);
        basicAdapter.updateCallback(callback);
    }

    private void navigateBack() {
        NavigationService.getINSTANCE()
                .navigate(
                        getActivity().getLocalClassName(),
                        R.id.profileFragment, null, null
                );
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
    public void onAction() {
        saveUserDetails();
    }

    @Override
    public void onBack() {
        if (profileViewModel.hasChanges) {
            presentConfirmationDialog();
        } else {
            navigateBack();
        }
    }

    @Override
    public void onDestroy() {
        ((RHApplication)getActivity().getApplication()).setCurrentFragment(null);
        super.onDestroy();
    }
}
