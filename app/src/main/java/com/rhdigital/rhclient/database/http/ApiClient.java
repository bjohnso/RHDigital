package com.rhdigital.rhclient.database.http;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

  private static final String BASE_URL = "https://firebasestorage.googleapis.com/v0/b/rh-digital-f9ec9.appspot.com/";
  private static Retrofit retrofit;

  public static Retrofit getRetrofit() {
    if (retrofit == null) {
      retrofit = new Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build();
    }
    return retrofit;
  }
}
