package com.rhdigital.rhclient.activities.auth.fragments;

import android.animation.AnimatorSet;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.rhdigital.rhclient.R;
import com.rhdigital.rhclient.activities.auth.AuthActivity;
import com.rhdigital.rhclient.activities.auth.services.Authenticator;
import com.rhdigital.rhclient.activities.courses.CoursesActivity;
import com.rhdigital.rhclient.common.loader.CustomLoaderFactory;
import com.rhdigital.rhclient.common.services.NavigationService;
import com.rhdigital.rhclient.common.util.GenericTimer;
import com.rhdigital.rhclient.database.model.User;
import com.rhdigital.rhclient.database.viewmodel.UserViewModel;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SignInFragment extends Fragment {

    //Threading
    private ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
    private Handler handler = new Handler(Looper.getMainLooper()) {
      @Override
      public void handleMessage(@NonNull Message msg) {
        if (msg.what == 1) {
          submit.setEnabled(true);
          submit.setBackgroundResource(R.drawable.submit_active);
        }
      }
    };

    // Animation
    private AnimatedVectorDrawable anim;
    private ImageView logo;
    private CustomLoaderFactory customLoaderFactory = null;

    // Components
    private AutoCompleteTextView emailInput;
    private AutoCompleteTextView passwordInput;
    private Button submit;
    private LinearLayout resetPasswordRedirect;
    private LinearLayout signUpRedirect;
    private FrameLayout frameLayout;

    //Observables

    private TextView line;
    private TextView welcome;

    private int loaderHeight = 0;
    private int loaderWidth = 0;

    @RequiresApi(api = Build.VERSION_CODES.M)
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sign_in_layout, container, false);

        // Initialise Components
        emailInput = (AutoCompleteTextView) view.findViewById(R.id.sign_in_email_input);
        passwordInput = (AutoCompleteTextView) view.findViewById(R.id.sign_in_password_input);
        submit = (Button) view.findViewById(R.id.sign_in_submit_btn);
        resetPasswordRedirect = (LinearLayout) view.findViewById(R.id.sign_in_helper);
        signUpRedirect = (LinearLayout) view.findViewById(R.id.sign_in_redirect);
        frameLayout = (FrameLayout) view.findViewById(R.id.loader);

        line = (TextView) view.findViewById(R.id.line);
        welcome = (TextView) view.findViewById(R.id.welcome);

        // Animation
        anim = (AnimatedVectorDrawable) getContext().getDrawable(R.drawable.rh_vector_animation);
        logo = view.findViewById(R.id.sign_in_logo);
        logo.setImageDrawable(anim);
        anim.start();

        frameLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
          @Override
          public void onGlobalLayout() {
            if (frameLayout.getHeight() > 0 && frameLayout.getWidth() > 0) {
              frameLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this::onGlobalLayout);
              initLoaderFactory();
            }
          }
        });
        return view;
    }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    if (firebaseUser != null) {
      Log.d("SIGNIN", "PREAUTH!");
      signIn(false);
    }

    //Set Listeners
    signUpRedirect.setOnClickListener(new RedirectSignUpOnClick(getActivity().getLocalClassName()));
    submit.setOnClickListener(new SubmitOnClick(this));
  }

  public void signIn(boolean authenticate) {
      AuthActivity authActivity = ((AuthActivity)getActivity());
      if (authenticate) {
        authActivity
          .signIn(getEmailText(), getPasswordText())
          .observe(getViewLifecycleOwner(), isSuccessful -> {
            if (isSuccessful) {
              setFieldsValidated();
              addLoader();
            } else {
              setSubmitDisableTimeout();
            }
          });
    } else {
        setFieldsValidated();
        addLoader();
        authActivity.launchCoursesActivity();
    }
  }

  public void setFieldsValidated() {
    UserViewModel userViewModel = ((AuthActivity)getActivity()).getUserViewModel();
    userViewModel.getAuthenticatedUser(FirebaseAuth.getInstance().getUid()).observe(getViewLifecycleOwner(), user -> {
      if (user != null) {
        this.signUpRedirect.setVisibility(View.INVISIBLE);
        this.resetPasswordRedirect.setVisibility(View.INVISIBLE);
        this.line.setVisibility(View.INVISIBLE);
        this.passwordInput.setVisibility(View.INVISIBLE);
        this.emailInput.setVisibility(View.INVISIBLE);
        this.submit.setVisibility(View.INVISIBLE);
        this.welcome.setText("Welcome " + user.getName());
        this.welcome.setVisibility(View.VISIBLE);
      }
    });
    }

    public void setSubmitDisableTimeout() {
      this.scheduledExecutorService.schedule(new GenericTimer(this), 3, TimeUnit.SECONDS);
    }

    public void setSubmitDisable() {
      this.submit.setEnabled(false);
      this.submit.setBackgroundResource(R.drawable.submit_inactive);
    }

    public String getEmailText() {
      return this.emailInput.getText().toString();
    }

    public String getPasswordText() {
      return this.passwordInput.getText().toString();
    }

  public Handler getHandler() {
    return handler;
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

  // Listeners
  public static class SubmitOnClick implements View.OnClickListener {

      private Fragment fragment;

      public SubmitOnClick(Fragment fragment) {
        this.fragment = fragment;
      }

    @Override
    public void onClick(View view) {
        SignInFragment signInFragment = (SignInFragment) fragment;
        signInFragment.setSubmitDisable();
        signInFragment.signIn(true);
    }
  }

  public static class RedirectSignUpOnClick implements View.OnClickListener {

      private String parentClassName;

      public RedirectSignUpOnClick(String parentClassName) {
        this.parentClassName = parentClassName;
      }

    @Override
    public void onClick(View view) {
        NavigationService.getINSTANCE()
          .navigate(parentClassName,
            R.id.signUpFragment,
            null, null);
    }
  }
}
