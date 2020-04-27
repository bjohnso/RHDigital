package com.rhdigital.rhclient.activities.auth.listeners;

import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayout;
import com.rhdigital.rhclient.activities.auth.fragments.SignUpFragment;

public class SignUpTabLayoutOnClickListener implements TabLayout.OnTabSelectedListener {

    private Fragment fragment;

    public SignUpTabLayoutOnClickListener(Fragment fragment){
      this.fragment = fragment;
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
      SignUpFragment signUpFragment = (SignUpFragment) fragment;

      switch (tab.getPosition()){
        case 0:
          signUpFragment.setViewPager(0);
          break;
        case 1:
          signUpFragment.setViewPager(1);
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
