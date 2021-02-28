package com.rhdigital.rhclient.activities.rhapp.dialogs;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.rhdigital.rhclient.R;

public class DownloadingDialog extends DialogFragment {

    private TextView tvTitle;

    public DownloadingDialog() {
        setCancelable(false);
    }

    @Override
    public int getTheme() {
        return R.style.FullScreenDialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_downloading, container, false);
        tvTitle = view.findViewById(R.id.tvTitle);
        return view;
    }

    public void onFailure() {
        Handler handler = new Handler(Looper.getMainLooper());
        Runnable runnableCode = () -> {
            tvTitle.setText(R.string.content_downloading_failed);
            delayedDismiss();
        };
        handler.postDelayed(runnableCode, 2000);
    }

    public void onSuccess() {
        Handler handler = new Handler(Looper.getMainLooper());
        Runnable runnableCode = () -> {
            tvTitle.setText(R.string.content_downloading_success);
            delayedDismiss();
        };
        handler.postDelayed(runnableCode, 2000);
    }

    private void delayedDismiss() {
        Handler handler = new Handler(Looper.getMainLooper());
        Runnable runnableCode = this::dismiss;
        handler.postDelayed(runnableCode, 3000);
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
    }

}
