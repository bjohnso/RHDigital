package com.rhdigital.rhclient.activities.rhapp.fragments;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.rhdigital.rhclient.activities.rhapp.adapters.EditProfileRecyclerViewAdapter;
import com.rhdigital.rhclient.activities.rhapp.viewmodel.ProfileViewModel;
import com.rhdigital.rhclient.common.dto.UserFieldDto;
import com.rhdigital.rhclient.common.interfaces.OnClickCallback;
import com.rhdigital.rhclient.database.model.User;
import com.rhdigital.rhclient.databinding.FragmentEditProfileBinding;

import java.util.Arrays;
import java.util.List;

public class EditProfileFragment extends Fragment {

    private final String TAG = "USER_FRAGMENT";

    // VIEW
    private FragmentEditProfileBinding binding;

    // VIEW MODEL
    private ProfileViewModel profileViewModel;

    private EditProfileRecyclerViewAdapter basicAdapter;
    private EditProfileRecyclerViewAdapter sensitiveAdapter;

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

    private void initialiseUI() {
        calculateImageDimensions();
    }

    private void initialiseRecyclerViews() {
        User user = profileViewModel.user.getValue();

        OnClickCallback callback = (action) -> { };

        List<UserFieldDto> basicFields = Arrays.asList(
                new UserFieldDto("First Name", user.getName()),
                new UserFieldDto("Last Name", user.getSurname()),
                new UserFieldDto("Title", user.getTitle()),
                new UserFieldDto("City", user.getCity()),
                new UserFieldDto("Country", user.getCountry()),
                new UserFieldDto("About", user.getAbout()),
                new UserFieldDto("Industry", user.getIndustry())
        );

        List<UserFieldDto> sensitiveFields = Arrays.asList(
                new UserFieldDto("Data of Birth", "")
        );

        basicAdapter = new EditProfileRecyclerViewAdapter();
        binding.recyclerViewBasic.setHasFixedSize(true);
        binding.recyclerViewBasic.setItemViewCacheSize(10);
        binding.recyclerViewBasic.setDrawingCacheEnabled(true);
        binding.recyclerViewBasic.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_AUTO);
        binding.recyclerViewBasic.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerViewBasic.setAdapter(basicAdapter);

        sensitiveAdapter = new EditProfileRecyclerViewAdapter();
        binding.recyclerViewSensitive.setHasFixedSize(true);
        binding.recyclerViewSensitive.setItemViewCacheSize(10);
        binding.recyclerViewSensitive.setDrawingCacheEnabled(true);
        binding.recyclerViewSensitive.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_AUTO);
        binding.recyclerViewSensitive.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerViewSensitive.setAdapter(sensitiveAdapter);

        basicAdapter.updateData(basicFields, callback);
        sensitiveAdapter.updateData(sensitiveFields, callback);
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
}
