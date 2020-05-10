package com.rhdigital.rhclient.activities.auth.fragments;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.drawable.Animatable2;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
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
import androidx.vectordrawable.graphics.drawable.Animatable2Compat;
import androidx.vectordrawable.graphics.drawable.Animatable2Compat.AnimationCallback;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;

import com.rhdigital.rhclient.R;
import com.rhdigital.rhclient.activities.auth.listeners.SignInOnClickListener;
import com.rhdigital.rhclient.activities.auth.listeners.SignUpRedirectOnClickListener;
import com.rhdigital.rhclient.ui.view.CustomLoaderFactory;
import com.rhdigital.rhclient.util.GenericTimer;

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

    //Components
    private AutoCompleteTextView emailInput;
    private AutoCompleteTextView passwordInput;
    private Button submit;
    private LinearLayout resetPasswordRedirect;
    private LinearLayout signUpRedirect;
    private FrameLayout frameLayout;

    private TextView line;

    private int loaderHeight = 0;
    private int loaderWidth = 0;

    @RequiresApi(api = Build.VERSION_CODES.M)
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sign_in_layout, container, false);

        //Initialise Components
        emailInput = (AutoCompleteTextView) view.findViewById(R.id.sign_in_email_input);
        passwordInput = (AutoCompleteTextView) view.findViewById(R.id.sign_in_password_input);
        submit = (Button) view.findViewById(R.id.sign_in_submit_btn);
        resetPasswordRedirect = (LinearLayout) view.findViewById(R.id.sign_in_helper);
        signUpRedirect = (LinearLayout) view.findViewById(R.id.sign_in_redirect);
        frameLayout = (FrameLayout) view.findViewById(R.id.loader);

        line = (TextView) view.findViewById(R.id.line);

        //Animation
        anim = (AnimatedVectorDrawable) getContext().getDrawable(R.drawable.rh_vector_animation);
        logo = view.findViewById(R.id.sign_in_logo);
        logo.setImageDrawable(anim);
        anim.start();

        frameLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
          @Override
          public void onGlobalLayout() {
            frameLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this::onGlobalLayout);
            initLoaderFactory();
          }
        });

        //Set Listeners
        signUpRedirect.setOnClickListener(new SignUpRedirectOnClickListener(this));
        submit.setOnClickListener(new SignInOnClickListener(this));
        return view;
    }

    public void setFieldsValidated() {
      signUpRedirect.setVisibility(View.INVISIBLE);
      resetPasswordRedirect.setVisibility(View.INVISIBLE);
      line.setVisibility(View.INVISIBLE);
      this.passwordInput.setVisibility(View.INVISIBLE);
      this.emailInput.setVisibility(View.INVISIBLE);
      this.submit.setBackgroundResource(R.drawable.background_transparent);
      this.submit.setTextColor(getActivity().getResources().getColor(R.color.active));
      this.submit.setText("Welcome");

    }

    public void setSubmitDisableTimeout() {
      this.scheduledExecutorService.schedule(new GenericTimer(this), 2, TimeUnit.SECONDS);
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
}
