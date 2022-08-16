package com.varsitycollege.kn_beautyapp_2022_opsc_task2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CollectionItems extends AppCompatActivity {

    // Global Variables
    TextView txt_collectionHeading, txt_Message;
    String heading;
    User user;
    Item item;
    RecyclerView rcy_Item;
    ImageView img_AddLogo, img_ReturnHome;
    Collection collectionSelected;

    //Firebase realtime database references
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;

    TextView txt_item,txt_goalNo;
    ProgressBar bar;

    Integer sum;
    Integer counter;

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
        setContentView(R.layout.activity_collection_items);

        txt_item= (TextView) findViewById(R.id.txt_itemsCurrently);
        txt_goalNo= (TextView)findViewById(R.id.txt_GoalNumber);
        bar = (ProgressBar) findViewById(R.id.collect_progress);

        user = ListUtils.usersList.get(0);

        //Intent to get collection object passed in from the Home.cs
        Intent i = getIntent();

        //Instantiate the object with the object passed from the Home.cs
        collectionSelected = i.getParcelableExtra("Collection");
        heading = collectionSelected.getCollectionName();

        //Clear ArrayLists
        ListUtils.collectionsItemList.clear();
        ListUtils.collectionList.clear();
        ListUtils.collectList.add(collectionSelected);

        //find component
        txt_collectionHeading =findViewById(R.id.txt_CreatedCollectionName);
        img_ReturnHome = findViewById(R.id.img_ReturnHome);
        img_AddLogo = findViewById(R.id.img_AddLogo);
        txt_collectionHeading = findViewById(R.id.txt_CreatedCollectionName);
        rcy_Item = findViewById(R.id.rcy_item);
        txt_Message = findViewById(R.id.txt_AddItemsToCollection);

        //Display the collection name
        txt_collectionHeading.setText(heading);

        //set user data in component
        setUserInfo();

        //Onclick to return back to home screen
        //when user clicks <- they will be redirected to the home page
        img_ReturnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ListUtils.itemsList.clear();
                ListUtils.collectionsItemList.clear();
                Intent returnHome = new Intent(CollectionItems.this, Home.class);
                startActivity(returnHome);
            }
        });

        //OnClick to redirect user to the CreateItem.cs(To create an item)
        //when the user clicks the + they will be redirected to create item page
        img_AddLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addItem = new Intent(CollectionItems.this, CreateItem.class);
                addItem.putExtra("CollectionInfo", collectionSelected);
                startActivity(addItem);
            }
        });
    }


    //method to pull current user data from firebase and display in a recyclerview
    private void setUserInfo() {
        //---------------------------------------Code Attribution------------------------------------------------
        //Author:Sarina Till
        //Uses:Read data from firebase realtime database

        // reference for data in firebase
        myRef = database.getReference().child("Users").child(user.getUserID()).child("Items");

        //get data from firebase whilst using reference
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //Item instance
                item = new Item();

                //pull data from firebase realtime database
                for (DataSnapshot itemFirebase : snapshot.getChildren()) {
                    //store the snapshot in item instance
                    item = itemFirebase.getValue(Item.class);
                    //Add the item to itemList
                    ListUtils.itemsList.add(item);
                }

        //Link:https://www.youtube.com/watch?v=Ydn5cXn1j-0&list=PL480DYS-b_kdor_f0IFgS7iiEsOwxdx6w&index=26
        //-----------------------------------------------End------------------------------------------------------

                counter=0;
                sum=0;
                //Check if the user has any items in their collections
                if (ListUtils.itemsList != null) {
                    //if the user has items
                    //Check which item belongs in the collection that the user selected
                    for (Item itemCollection : ListUtils.itemsList) {
                        if (itemCollection.getCollectionID().equals(collectionSelected.getCollectionId())) {
                            //Add the items that belong to collection into an arraylist
                            ListUtils.collectionsItemList.add(itemCollection);

                            if (collectionSelected.getGoal()== true){
                                sum++;
                            }
                        }
                    }

                    bar.setMax(collectionSelected.getCollectionGoalItem());
                    bar.setProgress(sum);
                    txt_item.setText(String.valueOf(sum));
                    txt_goalNo.setText(String.valueOf(collectionSelected.getCollectionGoalItem()));

                    //if the user has no items that belong in the collection selected
                    // display a message of how to create item
                    if (ListUtils.collectionsItemList.isEmpty()) {
                        txt_Message.setVisibility(View.VISIBLE);
                        sum = 0;
                    }

                } else {
                    //message on how to create item
                    txt_Message.setVisibility(View.VISIBLE);
                }


                //---------------------------------------Code Attribution------------------------------------------------
                //Author:Ben O'Brien
                //Uses:set the recyclerItemAdapter and display users data in the recyclerview
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                rcy_Item.setLayoutManager(layoutManager);
                rcy_Item.setItemAnimator(new DefaultItemAnimator());
                recyclerItemAdapter adapter = new recyclerItemAdapter(ListUtils.collectionsItemList, getApplicationContext());
                rcy_Item.setAdapter(adapter);
                //Link:https://www.youtube.com/watch?v=__OMnFR-wZU
                //-----------------------------------------------End------------------------------------------------------

                //---------------------------------------Code Attribution------------------------------------------------
                //Author:Coding in flow
                //Uses:when a specfic collection is clicked, the user is redirected to Items_Details with dtaa for the item clicked
                adapter.setOnItemClickListerner(new recyclerItemAdapter.OnItemClickListerner() {
                    @Override
                    public void onItemClick(int position) {
                        //Intent to redirect user to ItemDetails screen
                        Intent i = new Intent(CollectionItems.this, ItemDetails.class);

                        //---------------------------------------Code Attribution------------------------------------------------
                        //Author:Coding in Flow
                        //Uses:pass in a collection and Item object to the ItemDetails class
                        i.putExtra("CollectionItem", collectionSelected);
                        i.putExtra("Item", ListUtils.collectionsItemList.get(position));
                        //Link:https://www.youtube.com/watch?v=WBbsvqSu0is
                        //-----------------------------------------------End------------------------------------------------------
                        startActivity(i);
                    }
                //Link:https://www.youtube.com/watch?v=bhhs4bwYyhc&list=PLrnPJCHvNZuBtTYUuc5Pyo4V7xZ2HNtf4&index=4
                //-----------------------------------------------End------------------------------------------------------

                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
