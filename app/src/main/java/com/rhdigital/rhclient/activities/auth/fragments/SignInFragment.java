package com.rhdigital.rhclient.activities.auth.fragments;

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
import android.widget.AutoCompleteTextView;
import android.widget.Button;
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

    // Temp Animation Testing
    AnimatedVectorDrawable anim;
    ImageView logo;

    //Components
    AutoCompleteTextView emailInput;
    AutoCompleteTextView passwordInput;
    Button submit;
    LinearLayout resetPasswordRedirect;
    LinearLayout signUpRedirect;

    @RequiresApi(api = Build.VERSION_CODES.M)
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sign_in_layout, container, false);

        //Initialise Components
        emailInput = (AutoCompleteTextView) view.findViewById(R.id.sign_in_email_input);
        passwordInput = (AutoCompleteTextView) view.findViewById(R.id.sign_in_password_input);
        submit = (Button) view.findViewById(R.id.sign_in_submit_btn);
        resetPasswordRedirect = (LinearLayout) view.findViewById(R.id.sign_in_helper);
        signUpRedirect = (LinearLayout) view.findViewById(R.id.sign_in_redirect);

        //Animation Testing

        anim = (AnimatedVectorDrawable) getContext().getDrawable(R.drawable.rh_vector_animated);
        logo = view.findViewById(R.id.sign_in_logo);

        anim.registerAnimationCallback(new Animatable2.AnimationCallback() {
          @Override
          public void onAnimationEnd(Drawable drawable) {
            anim.start();
          }
        });

        logo.setImageDrawable(anim);

        anim.start();

        //Set Listeners
        signUpRedirect.setOnClickListener(new SignUpRedirectOnClickListener(this));
        submit.setOnClickListener(new SignInOnClickListener(this));
        return view;
    }

    public void setSubmitDisableTimeout() {
      this.submit.setEnabled(false);
      this.submit.setBackgroundResource(R.drawable.submit_inactive);
      this.scheduledExecutorService.schedule(new GenericTimer(this), 2, TimeUnit.SECONDS);
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
}
