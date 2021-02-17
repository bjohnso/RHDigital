package com.rhdigital.rhclient.activities.rhpayment;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.rhdigital.rhclient.R;

public class RHPaymentActivity extends AppCompatActivity {
    private WebView webView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.paymentwebview);

        webView = (WebView) findViewById(R.id.paymentwebview);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("192.168.0.105:3001/");

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
    }

    @Override
    public void onBackPressed() {
        if(webView.canGoBack()){
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
