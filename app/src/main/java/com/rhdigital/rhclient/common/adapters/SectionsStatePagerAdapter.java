package com.rhdigital.rhclient.common.adapters;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.HashMap;

public class SectionsStatePagerAdapter extends FragmentStateAdapter {

  HashMap<Integer, String> fragmentPagingMap = new HashMap<>();

  public SectionsStatePagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
    super(fragmentManager, lifecycle);
  }

  public void setFragmentPagingMap(HashMap<Integer, String> fragmentPagingMap) {
    this.fragmentPagingMap = fragmentPagingMap;
  }

  @NonNull
  @Override
  public Fragment createFragment(int position) {
    try {
      return (Fragment) Class.forName(fragmentPagingMap.get(position)).newInstance();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (InstantiationException e) {
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
    return null;
  }

  @Override
  public int getItemCount() {
    return fragmentPagingMap.size();
  }
}
