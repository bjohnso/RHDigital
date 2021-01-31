package com.rhdigital.rhclient.activities.rhapp.dialogs;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.rhdigital.rhclient.R;
import com.rhdigital.rhclient.activities.rhapp.adapters.EditProfileArrayAdapter;
import com.rhdigital.rhclient.activities.rhapp.delegates.EditProfileDialogDelegate;
import com.rhdigital.rhclient.common.dto.UserFieldDto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class EditProfileDialog extends DialogFragment {

    private EditProfileDialogDelegate delegate;
    private UserFieldDto userField;
    private TextView tvProperty;
    private EditText etValue;
    private ListView list;
    private Button buttonCancel;
    private Button buttonOk;

    public EditProfileDialog(EditProfileDialogDelegate delegate, UserFieldDto userField) {
        this.delegate = delegate;
        this.userField = userField;
        setCancelable(false);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_edit_profile, container, false);
        tvProperty = view.findViewById(R.id.tvField);
        etValue = view.findViewById(R.id.etProperty);
        list = view.findViewById(R.id.list);
        buttonOk = view.findViewById(R.id.buttonOk);
        buttonCancel = view.findViewById(R.id.buttonCancel);
        bindUI();
        return view;
    }

    private void bindUI() {
        tvProperty.setText(userField.getField());

        if (userField.getType() == UserFieldDto.COUNTRY) {
            list.setVisibility(View.VISIBLE);
            etValue.setVisibility(View.GONE);

            Locale[] locales = Locale.getAvailableLocales();
            ArrayList<String> countries = new ArrayList<>();

            for (Locale l: locales) {
              String country = l.getDisplayCountry();
              if (country.trim().length() > 0 && !countries.contains(country))
                countries.add(l.getDisplayCountry());
            }
            Collections.sort(countries);

            list.setAdapter(
                    new EditProfileArrayAdapter(
                            getContext(),
                            countries,
                            countries.indexOf(userField.getValue()),
                            userField
                    )
            );

        } else {
            list.setVisibility(View.GONE);
            etValue.setVisibility(View.VISIBLE);
            etValue.setText(userField.getValue());
        }

        buttonCancel.setOnClickListener(view -> {
            delegate.onComplete(userField);
            dismiss();
        });

        buttonOk.setOnClickListener(view -> {
            if (etValue.getVisibility() == View.VISIBLE) {
                userField.setValue(etValue.getText().toString());
            }
            delegate.onComplete(
                    new UserFieldDto(
                            userField.getField(),
                            userField.getValue(),
                            userField.getType()
                    )
            );
            dismiss();
        });
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
    }
}
