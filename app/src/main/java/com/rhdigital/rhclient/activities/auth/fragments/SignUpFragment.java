package com.rhdigital.rhclient.activities.auth.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import com.google.android.material.tabs.TabLayout;
import com.rhdigital.rhclient.R;

public class SignUpFragment extends Fragment {

  // Components
  private boolean isParent = true;
  private NavController navController;
  private TabLayout tabLayout;
  private LinearLayout signInRedirect;

  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.sign_up_layout, container, false);

    // Initialise Components
    signInRedirect = view.findViewById(R.id.sign_up_redirect);
    tabLayout = view.findViewById(R.id.sign_up_tab_layout);
    return view;
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    // Navigation
    navController = Navigation.findNavController(view);
    NavController childController = Navigation.findNavController(getActivity(), R.id.nav_host_sign_up);
    NavOptions navOptions = new NavOptions.Builder()
      .setLaunchSingleTop(true)
      .setPopUpTo(childController.getGraph().getStartDestination(), false)
      .build();
    tabLayout.addOnTabSelectedListener(new TabOnClick(childController, navOptions));
    signInRedirect.setOnClickListener(new RedirectSignInOnClick(navController));
  }

  private static class RedirectSignInOnClick implements View.OnClickListener {

    private NavController navController;

    public RedirectSignInOnClick(NavController navController) {
      this.navController = navController;
    }

    @Override
    public void onClick(View view) {
      navController.navigate(R.id.signInFragment);
    }
  }

  private static class TabOnClick implements TabLayout.OnTabSelectedListener {
    private NavController navController;
    private NavOptions navOptions;

    public TabOnClick(NavController navController, NavOptions navOptions){
      this.navController = navController;
      this.navOptions = navOptions;
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
      switch (tab.getPosition()){
        case 0:
          navController.navigate(R.id.signUpPhoneFragment, null,navOptions);
          break;
        case 1:
          navController.navigate(R.id.signUpEmailFragment, null, navOptions);
          break;
      }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
  }
}
