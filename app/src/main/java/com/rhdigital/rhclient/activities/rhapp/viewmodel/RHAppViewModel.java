package com.rhdigital.rhclient.activities.rhapp.viewmodel;

import android.app.Application;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.BindingAdapter;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class RHAppViewModel extends AndroidViewModel {

    public MutableLiveData<String> title = new MutableLiveData("");
    public MutableLiveData<Boolean> isTitleCenter = new MutableLiveData(false);
    public MutableLiveData<Boolean> isBackButtonActive = new MutableLiveData(false);
    public MutableLiveData<Boolean> isActionButtonActive = new MutableLiveData(false);
    public MutableLiveData<Boolean> isEnrollState = new MutableLiveData(false);
    public MutableLiveData<Boolean> isFullscreenMode = new MutableLiveData(false);

    public RHAppViewModel(@NonNull Application application) {
        super(application);
    }

    @BindingAdapter("app:center")
    public static void setCenter(View view, Boolean isCenter) {
        Toolbar.LayoutParams params =
                new Toolbar.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = isCenter ? Gravity.CENTER : Gravity.START;
        view.setLayoutParams(params);
    }
}
