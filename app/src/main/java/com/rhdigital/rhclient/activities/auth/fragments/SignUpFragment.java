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
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.tabs.TabLayout;
import com.rhdigital.rhclient.R;
import com.rhdigital.rhclient.common.services.NavigationService;

public class SignUpFragment extends Fragment {

  // Components
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
    //Navigation
    NavHostFragment navHostFragment = (NavHostFragment) getChildFragmentManager().findFragmentById(R.id.nav_host_sign_up);
    NavController navController = navHostFragment.getNavController();
    NavigationService.getINSTANCE().initNav(getClass().getName(),
      navController,
      R.navigation.sign_up_nav_graph,
      R.id.signUpPhoneFragment);
    tabLayout.addOnTabSelectedListener(new TabOnClick(getClass().getName()));
    signInRedirect.setOnClickListener(new RedirectSignInOnClick(getActivity().getLocalClassName()));
  }

  private static class RedirectSignInOnClick implements View.OnClickListener {

    private String parentClassName;

    public RedirectSignInOnClick(String parentClassName) {
      this.parentClassName = parentClassName;
    }

    @Override
    public void onClick(View view) {
      NavigationService.getINSTANCE().navigate(parentClassName, R.id.signInFragment, null, null);
    }
  }

  private static class TabOnClick implements TabLayout.OnTabSelectedListener {
    private String className;

    public TabOnClick(String className){
      this.className = className;
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
      switch (tab.getPosition()){
        case 0:
          NavigationService.getINSTANCE().navigate(className, R.id.signUpPhoneFragment, null, null);
          break;
        case 1:
          NavigationService.getINSTANCE().navigate(className, R.id.signUpEmailFragment, null, null);
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
