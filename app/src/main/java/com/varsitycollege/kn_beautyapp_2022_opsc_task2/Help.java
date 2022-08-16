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

public class Help extends AppCompatActivity {

    private WebView Helpwebview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide(); //Hide the action bar
        setContentView(R.layout.activity_help);

        //---------------------------------------Code Attribution------------------------------------------------
        //Author:Coding in Flow
        //Uses:Opening Help webpage in the application

        WebView myWebView = (WebView) findViewById(R.id.Helpwebview);
        myWebView.setWebViewClient(new WebViewClient()); //Loads in the application
        myWebView.loadUrl("https://knbeauty.azurewebsites.net/"); //Url of the loaded website

        myWebView = new WebView(Help.this);
        setContentView(myWebView);

        myWebView.loadUrl("https://knbeauty.azurewebsites.net/");


    }

    @Override
    public void onBackPressed() {
        if (Helpwebview.canGoBack()) {
            Helpwebview.goBack();
        } else {
        super.onBackPressed();
        {

        }
    }}}
        //Link:https://www.youtube.com/watch?v=TUXui5ItBkM
        //-----------------------------------------------End------------------------------------------------------
