package com.varsitycollege.kn_beautyapp_2022_opsc_task2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

public class PrivacyPolicy extends AppCompatActivity {

    private WebView PrivacyPolicywebview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide(); //Hide the action bar
        setContentView(R.layout.activity_privacy_policy);

        //---------------------------------------Code Attribution------------------------------------------------
        //Author:Coding in Flow
        //Uses:Opening privacy policy webpage in the application
        WebView myWebView = (WebView) findViewById(R.id.PrivacyPolicywebview);
        myWebView.setWebViewClient(new WebViewClient()); //Loads in the application
        myWebView.loadUrl("https://www.termsfeed.com/live/0dac0478-4856-4a54-b1c1-f13afa879e13"); //Url of the loaded website

        myWebView = new WebView(PrivacyPolicy.this);
        setContentView(myWebView);

        myWebView.loadUrl("https://www.termsfeed.com/live/0dac0478-4856-4a54-b1c1-f13afa879e13");
    }

    @Override
    public void onBackPressed() {
        if (PrivacyPolicywebview.canGoBack()) {
            PrivacyPolicywebview.goBack();
        } else {
            super.onBackPressed();
            {

            }
        }}}
        //Link:https://www.youtube.com/watch?v=TUXui5ItBkM
        //-----------------------------------------------End------------------------------------------------------
