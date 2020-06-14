package com.rhdigital.rhclient.activities.auth.fragments;

import android.animation.AnimatorSet;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.rhdigital.rhclient.R;
import com.rhdigital.rhclient.activities.auth.AuthActivity;
import com.rhdigital.rhclient.activities.auth.services.Authenticator;
import com.rhdigital.rhclient.common.loader.CustomLoaderFactory;
import com.rhdigital.rhclient.common.services.NavigationService;
import com.rhdigital.rhclient.database.model.User;
import com.rhdigital.rhclient.database.viewmodel.UserViewModel;

public class SignInWelcomeFragment extends Fragment {

  // Animation
  private AnimatedVectorDrawable anim;
  private ImageView logo;
  private CustomLoaderFactory customLoaderFactory = null;
  private FrameLayout frameLayout;
  private TextView welcome;

  //Observable
  private LiveData<Boolean> authData;

  //ViewModel
  private UserViewModel userViewModel;

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.sign_in_welcome_layout, container, false);

    userViewModel = ((AuthActivity)getActivity()).getUserViewModel();

    // Animation
    anim = (AnimatedVectorDrawable) getContext().getDrawable(R.drawable.rh_vector_animation);
    logo = view.findViewById(R.id.sign_in_logo);
    logo.setImageDrawable(anim);
    anim.start();
    frameLayout = (FrameLayout) view.findViewById(R.id.loader);
    welcome = view.findViewById(R.id.welcome);

    frameLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
      @Override
      public void onGlobalLayout() {
        if (frameLayout.getHeight() > 0 && frameLayout.getWidth() > 0) {
          frameLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this::onGlobalLayout);
          initLoaderFactory();
        }
      }
    });

    FirebaseUser firebaseUser;
    if ((firebaseUser = FirebaseAuth.getInstance().getCurrentUser()) != null) {
      if (firebaseUser.isEmailVerified()) {
        LiveData<User> userTask = userViewModel.getAuthenticatedUser(firebaseUser.getUid());
        userTask.observe(getViewLifecycleOwner(), user -> {
          if (user != null) {
            userTask.removeObservers(getViewLifecycleOwner());
            welcome.setText("Welcome " + user.getName());
            addLoader();
          }

          authData = Authenticator.getInstance()
            .getLaunchReadyObservable();
          authData.observe(getViewLifecycleOwner(), launch -> {
            authData.removeObservers(getViewLifecycleOwner());
            if (launch) {
              ((AuthActivity) getActivity()).launchCoursesActivity();
            } else {
              NavigationService.getINSTANCE()
                .navigate(getActivity().getLocalClassName(),
                  R.id.signInFragment,
                  null,
                  null);
            }
          });
        });
      } else {
        NavigationService.getINSTANCE()
          .navigate(getActivity().getLocalClassName(),
            R.id.EmailVerificationFragment,
            null,
            null);
      }
    } else {
      NavigationService.getINSTANCE()
        .navigate(getActivity().getLocalClassName(),
          R.id.signInFragment,
          null,
          null);
    }

    return view;
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
  }

  private boolean initLoaderFactory() {
    if (frameLayout.getHeight() > 0 && frameLayout.getWidth() > 0) {
      if (customLoaderFactory == null) {
        customLoaderFactory = new CustomLoaderFactory(
          getContext(),
          frameLayout.getWidth(),
          frameLayout.getHeight(),
          4,
          35,
          25);
        return true;
      }
    }
    return false;
  }

  public void addLoader() {
    if (customLoaderFactory != null) {
      if (!initLoaderFactory()) {
        removeLoader();
      }
      for (View v : customLoaderFactory.getChildren()) {
        frameLayout.addView(v);
      }

      for (AnimatorSet a : customLoaderFactory.createAnimations()) {
        a.start();
      }
    }
  }

  public void removeLoader() {
    for (AnimatorSet a : customLoaderFactory.createAnimations()) {
      a.end();
    }
    for (View v : customLoaderFactory.getChildren()) {
      frameLayout.removeView(v);
    }
  }
}
