package com.varsitycollege.kn_beautyapp_2022_opsc_task2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Home extends AppCompatActivity {

    //Global variables
    ImageView img_Logo;
    RecyclerView rcyCollection;
    ImageView imageMenu;

    //Instance of user and collection class
    User user;//current user
    Collection collect;

    //Firebase Realtime Database reference
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;


    ImageView img_Graph;

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
        setContentView(R.layout.activity_home);


        //redirect the user to the graph screen
        img_Graph= findViewById(R.id.img_Graph);
        img_Graph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent graph = new Intent(Home.this,Graph.class);
                startActivity(graph);

            }
        });


        //Navigation components
        DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);
        NavigationView navigationView = findViewById(R.id.navigationView);
        View v = navigationView.getHeaderView(0);

        //find the text components
        img_Logo = findViewById(R.id.img_Logo);
        rcyCollection = findViewById(R.id.rcy_Collections);
        imageMenu = findViewById(R.id.imageMenu);
        TextView txtName = v.findViewById(R.id.txt_Name);
        TextView txtEmail = v.findViewById(R.id.txt_Email);

        //Display users name and email in navigation drawer
        txtName.setText(CurrentUser.user.getUserName());
        txtEmail.setText(CurrentUser.user.getUserEmail());

        //call method to display the users collection
        setUserInfo();

        //when the menu icon is clicked the navigation drawer opens
        imageMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);

            }
        });

        //Set tint to menu icons on navigation view
        NavigationView navigationView1 = findViewById(R.id.navigationView);
        navigationView1.setItemIconTintList(null);

        //navigation drawer, when the user clicks on on of the item on the navigation ,they will be redirected to the corresponding link
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                drawerLayout.closeDrawer(GravityCompat.START);
                switch (id) {

                    case R.id.signout:
                        FirebaseAuth.getInstance().signOut();
                        Intent returnLogin = new Intent(Home.this,Login.class);
                        startActivity(returnLogin);
                        ListUtils.usersList.clear();
                        ListUtils.collectionList.clear();
                        ListUtils.itemsList.clear();
                        ListUtils.collectList.clear();
                        ListUtils.collectionImageList.clear();
                        ListUtils.itemImageList.clear();
                        break;
                    case R.id.privacyPolicy:
                        Intent privacy = new Intent(Home.this, PrivacyPolicy.class);
                        startActivity(privacy);
                        finish();
                        break;
                    case R.id.help:
                        Intent helppage = new Intent(Home.this, Help.class);
                        startActivity(helppage);
                        finish();
                        break;
                    case R.id.feedback:
                        Intent feedbackpage = new Intent(Home.this, Feedback.class);
                        startActivity(feedbackpage);
                        finish();
                        break;
                    default:
                        return true;
                }
                return true;
            }

        });

        //when the user clicks the + from the home page they will be redirected to the createcollection page
        img_Logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, CreateCollection.class);
                startActivity(intent);
            }
        });
    }


    //method to pull current user data from firebase and display in a recyclerview
    private void setUserInfo() {

        user = ListUtils.usersList.get(0);

        //---------------------------------------Code Attribution------------------------------------------------
        //Author:Sarina Till
        //Uses:Read data from firebase realtime database
        // reference for data in firebase
        myRef = database.getReference().child("Users").child(user.getUserID()).child("Collections");

        //get data from firebase whilst using reference
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                // instance of collection class
                collect = new Collection();

                //pulling data from realtime firebase
                for (DataSnapshot collectFirebase: snapshot.getChildren())
                {
                    // snapshot is assigned to the collection instance
                    collect = collectFirebase.getValue(Collection.class);
                    //Add instance to arraylist collectionList
                    ListUtils.collectionList.add(collect);
                }
        //Link:https://www.youtube.com/watch?v=Ydn5cXn1j-0&list=PL480DYS-b_kdor_f0IFgS7iiEsOwxdx6w&index=26
        //-----------------------------------------------End------------------------------------------------------

                //---------------------------------------Code Attribution------------------------------------------------
                //Author:Ben O'Brien
                //Uses:set the recycleCollectionAdapter and display users data in the recyclerview
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                rcyCollection.setLayoutManager(layoutManager);
                rcyCollection.setItemAnimator(new DefaultItemAnimator());
                recyclerCollectionAdapter adapter = new recyclerCollectionAdapter(ListUtils.collectionList,getApplicationContext());
                rcyCollection.setAdapter(adapter);
                //Link:https://www.youtube.com/watch?v=__OMnFR-wZU
                //-----------------------------------------------End------------------------------------------------------

                //---------------------------------------Code Attribution------------------------------------------------
                //Author:Coding in Flow
                //Uses:When a specfic item in recyclerview is clicked on,redirect user to their list of items in collection
                adapter.setOnCollectionClickListerner(new recyclerCollectionAdapter.OnCollectionClickListerner() {
                    @Override
                    public void onCollectionClick(int position) {
                        Intent i = new Intent(Home.this,CollectionItems.class);

                        //---------------------------------------Code Attribution------------------------------------------------
                        //Author:Coding in Flow
                        //Uses:Passing a collection object to the CollectionItem class using an intent
                        i.putExtra("Collection",ListUtils.collectionList.get(position));
                        //Link:https://www.youtube.com/watch?v=WBbsvqSu0is
                        //-----------------------------------------------End------------------------------------------------------

                        startActivity(i);
                    }
                });
                //Link:https://www.youtube.com/watch?v=bhhs4bwYyhc&list=PLrnPJCHvNZuBtTYUuc5Pyo4V7xZ2HNtf4&index=4
                //-----------------------------------------------End------------------------------------------------------
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}