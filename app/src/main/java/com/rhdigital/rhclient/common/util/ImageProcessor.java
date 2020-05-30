package com.rhdigital.rhclient.common.util;

import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.provider.OpenableColumns;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class ImageProcessor {

  private static ImageProcessor INSTANCE;

  private ImageProcessor() { }

  public static ImageProcessor getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new ImageProcessor();
    }
    return INSTANCE;
  }

  public String getFileName(ContentResolver contentResolver, Uri uri) {
    String name = "";
    Cursor cursor;
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      cursor = contentResolver.query(uri, null, null, null);
    } else {
      cursor = contentResolver.query(uri, null, null, null, null);
    }

    if (cursor != null) {
      cursor.moveToFirst();
      name = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
      cursor.close();
    }
    return name;
  }


  public Bitmap processImageForUpload(final File file, String type) {
    try {
      Log.d("IMAGE", "PROCESSING IMAGE...");
      Bitmap bitmap = BitmapFactory.decodeFile(file.getPath());
      int imageRotation = getImageRotation(file);
      if (imageRotation != 0) {
        bitmap = getBitmapRotatedByDegree(bitmap, imageRotation);
      }
      return compressImage(bitmap, type);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public Bitmap compressImage(Bitmap original, String type) {
    Log.d("IMAGE", "COMPRESSING IMAGE...");
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    if (type.equalsIgnoreCase("image/jpeg")) {
      original.compress(Bitmap.CompressFormat.JPEG, 25, out);
    } else if (type.equalsIgnoreCase("image/png")) {
      original.compress(Bitmap.CompressFormat.PNG, 25, out);
    } else {
      return null;
    }
    return BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));
  }

  private static int getImageRotation(final File file) {
    Log.d("IMAGE", "CALCULATING IMAGE ROTATIONS...");
    ExifInterface exif = null;
    int exifRotation = 0;

    try {
      exif = new ExifInterface(file.getPath());
      exifRotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
    } catch (IOException e) {
      e.printStackTrace();
    }

    if (exif == null)
      return 0;
    else
      return exifToDegrees(exifRotation);
  }

  private static int exifToDegrees(int rotation) {
    if (rotation == ExifInterface.ORIENTATION_ROTATE_90)
      return 90;
    else if (rotation == ExifInterface.ORIENTATION_ROTATE_180)
      return 180;
    else if (rotation == ExifInterface.ORIENTATION_ROTATE_270)
      return 270;

    return 0;
  }

  private static Bitmap getBitmapRotatedByDegree(Bitmap bitmap, int rotationDegree) {
    Log.d("IMAGE", "CORRECTING IMAGE ROTATION...");
    Matrix matrix = new Matrix();
    matrix.preRotate(rotationDegree);

    return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
  }
}
