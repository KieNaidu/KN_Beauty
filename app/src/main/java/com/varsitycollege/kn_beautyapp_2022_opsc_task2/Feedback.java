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

public class Feedback extends AppCompatActivity {

    private WebView Feedbackwebview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide(); //Hide the action bar
        setContentView(R.layout.activity_feedback);

        //---------------------------------------Code Attribution------------------------------------------------
        //Author:Coding in Flow
        //Uses:Opening feeback google form in the application

        WebView myWebView = (WebView) findViewById(R.id.Feedbackwebview);
        myWebView.setWebViewClient(new WebViewClient()); //Loads in the application
        myWebView.loadUrl("https://forms.gle/kmxWg66fgqd3xMXc7"); //Url of the loaded website

        myWebView = new WebView(Feedback.this);
        setContentView(myWebView);

       myWebView.loadUrl("https://forms.gle/kmxWg66fgqd3xMXc7");


    }

    @Override
    public void onBackPressed() {
        if (Feedbackwebview.canGoBack()) {
            Feedbackwebview.goBack();
        } else {
            super.onBackPressed();
            {

            }
        }}}

        //Link:https://www.youtube.com/watch?v=TUXui5ItBkM
        //-----------------------------------------------End------------------------------------------------------
