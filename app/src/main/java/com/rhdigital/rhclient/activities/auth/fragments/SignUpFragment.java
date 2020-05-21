package com.rhdigital.rhclient.activities.auth.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayout;
import com.rhdigital.rhclient.R;
import com.rhdigital.rhclient.activities.auth.listeners.SignInRedirectOnClickListener;
import com.rhdigital.rhclient.activities.auth.listeners.SignUpTabLayoutOnClickListener;
import com.rhdigital.rhclient.common.view.RHFragment;
import com.rhdigital.rhclient.common.adapters.SectionsStatePagerAdapter;
import com.rhdigital.rhclient.common.loader.CustomViewPager;

import java.util.HashMap;

public class SignUpFragment extends Fragment implements RHFragment {

  // Components
  private boolean isParent = true;
  private TabLayout tabLayout;
  private LinearLayout signInRedirect;
  private CustomViewPager customViewPager;

  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.sign_up_layout, container, false);

    // Initialise Components
    tabLayout = (TabLayout) view.findViewById(R.id.sign_up_tab_layout);
    signInRedirect = (LinearLayout) view.findViewById(R.id.sign_up_redirect);
    customViewPager = (CustomViewPager) view.findViewById(R.id.sign_up_view_pager);

    // Set Listeners
    tabLayout.addOnTabSelectedListener(new SignUpTabLayoutOnClickListener(this));
    signInRedirect.setOnClickListener(new SignInRedirectOnClickListener(this));

    setUpViewPager(customViewPager);

    return view;
  }

  private void setUpViewPager(CustomViewPager customViewPager){
    SectionsStatePagerAdapter sectionsStatePagerAdapter = new SectionsStatePagerAdapter(getChildFragmentManager());
    // Fragments
    sectionsStatePagerAdapter.setFragmentPagingMap(new HashMap<Integer, String>(){
      {put(0, SignUpPhoneFragment.class.getName());
        put(1, SignUpEmailFragment.class.getName());}});

    customViewPager.setAdapter(sectionsStatePagerAdapter);
    customViewPager.setSwipeable(false);
  }

  public int getViewPager(){
    return customViewPager.getCurrentItem();
  }

  public void setViewPager(int position){
    customViewPager.setCurrentItem(position);
  }

  @Override
  public boolean isParent() {
    return false;
  }

  @Override
  public void setIsParent(boolean parent) {

  }
}
