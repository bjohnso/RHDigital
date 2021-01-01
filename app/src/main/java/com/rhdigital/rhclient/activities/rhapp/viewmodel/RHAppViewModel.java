package com.rhdigital.rhclient.activities.rhapp.viewmodel;

import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;


import androidx.appcompat.widget.Toolbar;
import androidx.databinding.BindingAdapter;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RHAppViewModel extends ViewModel {

    public MutableLiveData<String> title = new MutableLiveData("Programs");
    public MutableLiveData<Boolean> isTitleCenter = new MutableLiveData(true);
    public MutableLiveData<Boolean> isBackButtonActive = new MutableLiveData(true);

    @BindingAdapter("app:center")
    public static void setCenter(View view, Boolean isCenter) {
        Toolbar.LayoutParams params =
                new Toolbar.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = isCenter ? Gravity.CENTER : Gravity.START;
        view.setLayoutParams(params);
    }
}
