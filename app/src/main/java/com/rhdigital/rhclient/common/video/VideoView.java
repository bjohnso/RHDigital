package com.rhdigital.rhclient.common.video;

import android.net.Uri;

public interface VideoView {

  public void initVideo(Uri uri);

  public void enableVideo();

  public void disableVideo();

  public void destroyVideo();

}
