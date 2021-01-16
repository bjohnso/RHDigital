package com.rhdigital.rhclient.activities.rhapp.fragments;

import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.rhdigital.rhclient.R;
import com.rhdigital.rhclient.activities.rhapp.viewmodel.ProfileViewModel;
import com.rhdigital.rhclient.database.model.User;
import com.rhdigital.rhclient.databinding.FragmentProfileBinding;


public class ProfileFragment extends Fragment {

    private final String TAG = "USER_FRAGMENT";

    // VIEW
    private FragmentProfileBinding binding;

    // VIEW MODEL
    private ProfileViewModel profileViewModel;

    private User user;

    // OBSERVABLES
    private LiveData<User> userObservable;

    // OBSERVERS
    private Observer<User> userObserver;

    private int width = 0;
    private int height = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(getLayoutInflater());

        profileViewModel = new ProfileViewModel(getActivity().getApplication());
        profileViewModel.init();
        binding.setViewModel(profileViewModel);

        initialiseLiveData();
        initialiseUI();

        return binding.getRoot();
    }

    private void initialiseUI() {
        calculateImageDimensions();
    }

    private void initialiseLiveData() {
        userObserver = new Observer<User>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onChanged(User u) {
                if (u != null) {
                    userObservable.removeObserver(this);
                    user = u;
                } else {
                    Toast.makeText(getContext(), R.string.server_error_user, Toast.LENGTH_LONG).show();
                }
            }
        };

        userObservable = profileViewModel.getUser("");
        userObservable.observe(getViewLifecycleOwner(), userObserver);
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
