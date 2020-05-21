package com.rhdigital.rhclient.common.loader;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

import android.view.View;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.HashMap;

public class CustomLoaderFactory extends FrameLayout {

  private ArrayList<CustomLoader> children = new ArrayList<>();
  private ArrayList<Path> animationPathList = new ArrayList<>();
  private HashMap<Path, Path> animationMap = new HashMap<>();
  private Paint paint = new Paint();
  private Context context;
  private float width;
  private float height;
  private float padding;
  private float circleDiameter;
  private float offcut;

  public CustomLoaderFactory(Context context, float width, float height, int numCircles, float circleStake, float spaceBetween) {
    super(context);
    this.context = context;
    this.width = width;
    this.height = height;
    calculateCircleSize(numCircles, circleStake, spaceBetween);
    init(numCircles);
  }

  private void init(int numCircles) {
    for (int i = 0; i < numCircles; i++) {
      Path path = new Path();
      float left = offcut + (padding * (i + 1)) + (circleDiameter * (i + 1));
      float top = (height / 2) - (circleDiameter / 2);
      float right = offcut + (circleDiameter * (i + 1)) + (padding * i) ;
      float bottom = top + circleDiameter;

      path.addArc(0,0, circleDiameter, circleDiameter, 0, 360);
      paint.setColor(Color.WHITE);
      CustomLoader customLoader = new CustomLoader(context, path, paint, (int) circleDiameter, (int) circleDiameter, left, top);

      this.children.add(customLoader);

      //Create animation Arc
      Path anim = new Path();
      anim.arcTo(left - circleDiameter - padding, top - (circleDiameter), right - padding, bottom, 0, -180, true);
      this.animationPathList.add(anim);
    }
  }

  public ArrayList<AnimatorSet> createAnimations() {
    ArrayList<AnimatorSet> animatorSets = new ArrayList<>();
    for (int i = 0; i < getChildren().size(); i++) {
      ObjectAnimator o = ObjectAnimator.ofFloat(
        this.children.get(i),
        "translationX",
        "translationY",
        this.animationPathList.get(i));
      ObjectAnimator f = ObjectAnimator.ofFloat(getChildren().get(i),
        View.ALPHA,
        0,
        1
      );

      o.setRepeatMode(ValueAnimator.REVERSE);
      o.setRepeatCount(ValueAnimator.INFINITE);
      o.setStartDelay(500 * i);
      o.setDuration(2000);

      f.setDuration(3000 * i);

      AnimatorSet animatorSet = new AnimatorSet();
      animatorSet.playTogether(o, f);
      animatorSets.add(animatorSet);
    }
    return animatorSets;
  }

  private void calculateCircleSize(int numCircles, float circleStake, float spaceBetween) {
    this.padding = (width / 100 * spaceBetween) / (numCircles - 1);
    this.circleDiameter = (width / 100 * circleStake) / numCircles;
    this.offcut = (width - (padding * (numCircles - 1)) - (circleDiameter * numCircles)) / 2;
  }

  public ArrayList<CustomLoader> getChildren() {
    return this.children;
  }

  public ArrayList<Path> getAnimationPathList() {
    return this.animationPathList;
  }
}
