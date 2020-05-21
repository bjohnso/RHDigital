package com.rhdigital.rhclient.common.loader;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

public class CustomViewPager extends ViewPager {
    private boolean swipeable;

    public CustomViewPager(@NonNull Context context) {
        super(context);
    }

    public CustomViewPager(@NonNull Context context, AttributeSet attributeSet){
        super(context, attributeSet);
        this.swipeable = true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (this.swipeable) {
            return super.onTouchEvent(ev);
        }
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (this.swipeable){
            return super.onInterceptTouchEvent(ev);
        }
        return false;
    }

    @Override
    public int getCurrentItem() {
        return super.getCurrentItem();
    }

    public void setSwipeable(boolean swipeable){
        this.swipeable = swipeable;
    }
}
