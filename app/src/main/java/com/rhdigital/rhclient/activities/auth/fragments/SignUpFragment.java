package com.rhdigital.rhclient.activities.auth.fragments;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.rhdigital.rhclient.R;

public class SignUpFragment extends Fragment {

    //Components
    AutoCompleteTextView emailInput;
    AutoCompleteTextView passwordInput;
    Button submit;
    TextView disclaimer;
    TextView signInRedirect;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sign_up_layout, container, false);

        //Initialise Components
        emailInput = (AutoCompleteTextView) view.findViewById(R.id.sign_up_email_input);
        passwordInput = (AutoCompleteTextView) view.findViewById(R.id.sign_up_password_input);
        submit = (Button) view.findViewById(R.id.sign_up_submit_btn);
        disclaimer = (TextView) view.findViewById(R.id.sign_up_disclaimer_text);
        signInRedirect = (TextView) view.findViewById(R.id.sign_up_sign_in_redirect_text);

        disclaimer.setText(Html.fromHtml(getString(R.string.disclaimer)));

        return view;
    }

}
