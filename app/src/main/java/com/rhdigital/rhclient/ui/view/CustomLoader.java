package com.rhdigital.rhclient.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

public class CustomLoader extends View {
  private Paint paint;
  private Path path;
  private float centerX;
  private float centerY;
  private int width;
  private int height;

  public CustomLoader(Context context, Path path, Paint paint, int width, int height, float x, float y) {
    super(context);
    this.paint = paint;
    this.path = path;
    this.width = width;
    this.height = height;
    setLayoutParams(new FrameLayout.LayoutParams(width, height));
    setX(x);
    setY(y);
  }

  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    canvas.drawPath(path, paint);
  }

  @Override
  public float getX() {
    return super.getX();
  }

  @Override
  public float getY() {
    return super.getY();
  }

  @Override
  public void setY(float y) {
    super.setY(y);
  }

  @Override
  public void setX(float x) {
    super.setX(x);
  }

  public Path getPath() {
    return path;
  }

  public float getCenterX() {
    return centerX;
  }

  public float getCenterY() {
    return centerY;
  }

  public void setCenterX(float centerX) {
    this.centerX = centerX;
  }

  public void setCenterY(float centerY) {
    this.centerY = centerY;
  }
}
