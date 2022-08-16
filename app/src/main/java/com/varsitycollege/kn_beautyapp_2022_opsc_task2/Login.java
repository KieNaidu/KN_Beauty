package com.varsitycollege.kn_beautyapp_2022_opsc_task2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentSender;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.BeginSignInResult;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
public class Login extends AppCompatActivity {

    //---------------------------------------Code Attribution------------------------------------------------
    //Author:Firebase,Emil Namaan Reuben Murray and Yadav Jivan
    //Uses:Firebase Google Login

    public static final String DEFUALT_IMAGE = "https://firebasestorage.googleapis.com/v0/b/vinyl-warehouse.appspot.com/o/default%2Ficon.png?alt=media&token=6495156e-9304-4be2-b2aa-06110afe1bee";
    public static final String SERVER_CLIENT_ID = "325279032255-8r3lbmc4198cu8s5ut12k86hjtfuka2u.apps.googleusercontent.com";
    //Declaring variables for Google Sign in
    private SignInClient oneTapClient;
    private FirebaseAuth mAuth;
    ImageView imgIntroWelcome_GoogleSignIn;
    private static final int REQ_ONE_TAP = 2;
    private BeginSignInRequest signInRequest;
    ImageView btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //---------------------------------------Code Attribution------------------------------------------------
        //Author:geeksforgeeks
        //Uses:Hides the action bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide(); //Hide the action bar
        //Link:https://www.geeksforgeeks.org/different-ways-to-hide-action-bar-in-android-with-examples/#:~:text=If%20you%20want%20to%20hide,AppCompat
        //-----------------------------------------------End------------------------------------------------------
        setContentView(R.layout.activity_login);

        //find component
        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
        mAuth = FirebaseAuth.getInstance();
        //get client
        oneTapClient = Identity.getSignInClient(this);

        signInRequest = BeginSignInRequest.builder()
                .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        // Your server's client ID, not your Android client ID.
                        .setServerClientId(SERVER_CLIENT_ID)
                        // Only show accounts previously used to sign in.
                        .setFilterByAuthorizedAccounts(false)
                        .build())
                .build();

        }


    private void signIn() {

        oneTapClient.beginSignIn(signInRequest).addOnSuccessListener(this, new OnSuccessListener<BeginSignInResult>() {
            @Override
            public void onSuccess(BeginSignInResult beginSignInResult) {

                try {
                    startIntentSenderForResult(beginSignInResult.getPendingIntent().getIntentSender(), REQ_ONE_TAP,
                            null, 0, 0, 0);
                } catch (IntentSender.SendIntentException e) {
                    Log.e("Google", "Couldn't start One Tap UI: " + e.getLocalizedMessage());
                }
            }
        }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // No saved credentials found. Launch the One Tap sign-up flow, or
                // do nothing and continue presenting the signed-out UI.
                Log.d("Google", "sigInw" + e.getLocalizedMessage());
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            String googleCredential = oneTapClient.getSignInCredentialFromIntent(data).getGoogleIdToken();
            AuthCredential credential = GoogleAuthProvider.getCredential(googleCredential, null);
            //here we are checking the Authentication Credential and checking the task is successful or not and display the message
            //based on that.
            mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        //Toast.makeText(Login.this, "successful", Toast.LENGTH_LONG).show();
                        //get signed in user
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        //update details and go to main activity
                        UpdateUI(firebaseUser);
                    } else {
                        Toast.makeText(Login.this, "Failed!", Toast.LENGTH_LONG).show();
                    }
                }
            });

        } catch (ApiException e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        //Get login user
        FirebaseUser currentUser = mAuth.getCurrentUser();

        //if null then no user loged in
        //else login the user and go to main activity
        if (currentUser != null) {
            UpdateUI(currentUser);
        }

    }
    private void UpdateUI(FirebaseUser firebaseUser) {

        // TODO: 2022/04/30 Convert to id base not google https://medium.com/@juliomacr/10-firebase-realtime-database-rule-templates-d4894a118a98

        if (firebaseUser != null) {
            //userID
            String id = firebaseUser.getUid();
            //Username
            String personName = firebaseUser.getDisplayName();
            //Email address
            String personEmail = firebaseUser.getEmail();
            //profile picture
            Uri personPhoto = firebaseUser.getPhotoUrl();
            //check if user has profile picture
            if (personPhoto == null) {
                personPhoto = Uri.parse(DEFUALT_IMAGE);
            }
            //Update user details
            User user = new User(personName,  personEmail, id);
            //Add user to arraylist
            ListUtils.usersList.add(user);
            //Store user basic details in runtime
            CurrentUser.user = user;
            // TODO: 2022/05/25 Take out later

            Toast.makeText(this, "Welcome " +user.getUserName(), Toast.LENGTH_SHORT).show();
            //Go to main activity
            Intent i = new Intent(Login.this, Home.class);
            startActivity(i);
            finish();
        }
    }

    //Link:https://www.youtube.com/watch?v=SXlidHy-Tb8&t=1s
    //Source:Vinyl Warehouse
    //-----------------------------------------------End------------------------------------------------------
}