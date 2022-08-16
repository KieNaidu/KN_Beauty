package com.varsitycollege.kn_beautyapp_2022_opsc_task2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;

public class ItemDetails extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    //Global variables
    Item item;
    EditText edt_IndividualItemTitle,edt_IndividualItemBrand,edt_IndividualDecription;
    TextView txt_IndividualItemDate,txt_HeadingCollection;
    ImageView img_IndividualImage,img_ReturnItemList,img_EditItem,img_Delete;
    TextView txt_ItemTitle,txt_ItemBrand,txt_ItemDecription,txt_ItemDate;
    Button btn_SaveEdit;
    ImageView img_DateEdit;
    String title,brand,description,dateAquired,img_ImageURL,itemId,collectionId;
    Collection collectionSelected;

    //getting the current user data
    User  user = ListUtils.usersList.get(0);

    //Firebase Realtime database Reference
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();

    //string values used in the database reference
    String userId = user.getUserID();
    String users ="Users";
    String itemRef ="Items";


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
        setContentView(R.layout.activity_item_details);

        //Create Intent to get item and collection object passed
        Intent i = getIntent();
        item = i.getParcelableExtra("Item");
        collectionSelected =i.getParcelableExtra("CollectionItem");

        //Get the value of the item that the user cannot edit
        img_ImageURL = item.getImgURL();
        collectionId = item.getCollectionID();
        itemId = item.getItemId();

        //find the component
        txt_HeadingCollection =findViewById(R.id.txt_IndividualCollectionItem);
        txt_ItemTitle = findViewById(R.id.txt_TitleDisplay);
        txt_ItemBrand = findViewById(R.id.txt_BrandDisplay);
        txt_ItemDecription =findViewById(R.id.txt_DescriptionDisplay);
        txt_ItemDate = findViewById(R.id.txt_DateDisplay);
        img_IndividualImage = findViewById(R.id.img_IndividualImage);
        img_ReturnItemList = findViewById(R.id.img_ReturnItemList);
        img_EditItem =findViewById(R.id.img_EditItem);
        img_Delete = findViewById(R.id.img_Delete);
        btn_SaveEdit =findViewById(R.id.btn_SaveEdit);
        //Edit components
        edt_IndividualItemTitle = findViewById(R.id.edt_IndividualItemTitle);
        edt_IndividualItemBrand = findViewById(R.id.edt_IndividualItemBrand);
        edt_IndividualDecription = findViewById(R.id.edt_IndividualDescription);
        img_DateEdit = findViewById(R.id.img_EditDate);
        txt_IndividualItemDate = findViewById(R.id.txt_DateEdit);

        //set the text for display components
        txt_HeadingCollection.setText(collectionSelected.getCollectionName());
        txt_ItemTitle.setText(item.getItemTitle());
        txt_ItemBrand.setText(item.getItemBrand());
        txt_ItemDecription.setText(item.getItemDescription());
        txt_ItemDate.setText(item.getItemDateAquired());

        //---------------------------------------Code Attribution------------------------------------------------
        //Author:CodingSTUFF
        //Uses:Display image fore firebase storage using url
        Glide.with(getApplicationContext()).load(item.getImgURL()).into(img_IndividualImage);
        //Link:https://www.youtube.com/watch?v=iEcokZOv5UY
        //-----------------------------------------------End------------------------------------------------------

        //when the user clicks the calender icon
        img_DateEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });


        //Onclick edit image button
        img_EditItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // make textview components invisible
                txt_ItemTitle.setVisibility(view.INVISIBLE);
                txt_ItemBrand.setVisibility(view.INVISIBLE);
                txt_ItemDecription.setVisibility(view.INVISIBLE);
                txt_ItemDate.setVisibility(view.INVISIBLE);

                // make editText and button components visible
                edt_IndividualItemTitle.setVisibility(view.VISIBLE);
                edt_IndividualItemBrand.setVisibility(view.VISIBLE);
                edt_IndividualDecription.setVisibility(view.VISIBLE);
                img_DateEdit.setVisibility(view.VISIBLE);
                btn_SaveEdit.setVisibility(View.VISIBLE);
            }
        });

        //Onclick for save
        //Update the item and updated the data in firebase realtime database
        btn_SaveEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Validation v = new Validation();

                try {
                    //Validate the title
                    //---------------------------------------Code Attribution------------------------------------------------
                    //Author:GeeksforGeeks
                    //Uses:validate if the input is empty and display field error, so the user know which component is an error
                    if (edt_IndividualItemTitle.length()==0){
                        edt_IndividualItemTitle.setError("Field is required!");
                        // Link:https://www.geeksforgeeks.org/implement-form-validation-error-to-edittext-in-android/
                        // -----------------------------------------------End------------------------------------------------------

                    }else{
                        try {
                            if(v.isNullOrEmpty(edt_IndividualItemTitle.getText().toString())==true || v.isAlphabet(edt_IndividualItemTitle.getText().toString())== false){
                                Toast.makeText(ItemDetails.this, "Enter in letter only!", Toast.LENGTH_SHORT).show();
                            }
                            title = edt_IndividualItemTitle.getText().toString();
                        }catch (Exception e){
                            Toast.makeText(ItemDetails.this,e.toString() , Toast.LENGTH_SHORT).show();
                        }
                    }


                    //validate the brand
                    //---------------------------------------Code Attribution------------------------------------------------
                    //Author:GeeksforGeeks
                    //Uses:validate if the input is empty and display field error, so the user know which component is an error
                    if (edt_IndividualItemBrand.length()==0){
                        edt_IndividualItemBrand.setError("Field is required!");
                        // Link:https://www.geeksforgeeks.org/implement-form-validation-error-to-edittext-in-android/
                        // -----------------------------------------------End------------------------------------------------------

                    }else{
                        try {
                            if(v.isNullOrEmpty(edt_IndividualItemBrand.getText().toString())==true || v.isAlphabet(edt_IndividualItemBrand.getText().toString())== false){
                                Toast.makeText(ItemDetails.this, "Enter in letter only!", Toast.LENGTH_SHORT).show();
                            }
                            brand = edt_IndividualItemBrand.getText().toString();
                        }catch (Exception e){
                            Toast.makeText(ItemDetails.this,e.toString() , Toast.LENGTH_SHORT).show();
                        }
                    }

                    //validate the description
                    //---------------------------------------Code Attribution------------------------------------------------
                    //Author:GeeksforGeeks
                    //Uses:validate if the input is empty and display field error, so the user know which component is an error
                    if (edt_IndividualDecription.length()==0){
                        edt_IndividualDecription.setError("Field is required!");
                        // Link:https://www.geeksforgeeks.org/implement-form-validation-error-to-edittext-in-android/
                        // -----------------------------------------------End------------------------------------------------------

                    }else{
                        try {
                            if(v.isNullOrEmpty(edt_IndividualDecription.getText().toString())==true || v.isAlphabet(edt_IndividualDecription.getText().toString())== false){
                                Toast.makeText(ItemDetails.this, "Enter in letter only!", Toast.LENGTH_SHORT).show();
                            }
                            description = edt_IndividualDecription.getText().toString();
                        }catch (Exception e){
                            Toast.makeText(ItemDetails.this,e.toString() , Toast.LENGTH_SHORT).show();
                        }
                    }

                    //validate dateAquired selected
                    try {
                        if(v.isNullOrEmpty(dateAquired)==true){
                            Toast.makeText(ItemDetails.this, "Select a date!", Toast.LENGTH_SHORT).show();
                        }
                    }catch (Exception e){
                        Toast.makeText(ItemDetails.this,e.toString() , Toast.LENGTH_SHORT).show();
                    }

                    //validate inputs before updating item
                    if (v.isNullOrEmpty(edt_IndividualItemTitle.getText().toString())==true ||v.isAlphabet(edt_IndividualItemTitle.getText().toString())== false
                            || v.isNullOrEmpty(edt_IndividualItemBrand.getText().toString())==true ||v.isAlphabet(edt_IndividualItemBrand.getText().toString())== false
                            || v.isNullOrEmpty(edt_IndividualDecription.getText().toString())==true  ||v.isAlphabet(edt_IndividualDecription.getText().toString())== false
                            || v.isNullOrEmpty(dateAquired)==true
                            || v.isNullOrEmpty(img_ImageURL)==true
                            || v.isNullOrEmpty(collectionId)==true
                            || v.isNullOrEmpty(itemId)==true
                    ){
                        Toast.makeText(ItemDetails.this, "Please check your inputs!", Toast.LENGTH_SHORT).show();
                    }else{
                        //---------------------------------------Code Attribution------------------------------------------------
                        //Author:Foxandroid
                        //Uses:Update data in Firebase realtime database
                        HashMap itemEdit = new HashMap();
                        itemEdit.put("collectionID",collectionId);
                        itemEdit.put("imgURL",img_ImageURL);
                        itemEdit.put("itemBrand",brand);
                        itemEdit.put("itemDateAquired",dateAquired);
                        itemEdit.put("itemDescription",description);
                        itemEdit.put("itemId",itemId);
                        itemEdit.put("itemTitle",title);

                        myRef.child(users).child(userId).child(itemRef).child(itemId).updateChildren(itemEdit).addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(ItemDetails.this, "Update Successful!", Toast.LENGTH_SHORT).show();
                                }else{

                                    txt_ItemTitle.setText("");
                                    txt_ItemBrand.setText("");
                                    txt_ItemDecription.setText("");
                                    txt_ItemDate.setText("");

                                    edt_IndividualItemBrand.setText("");
                                    edt_IndividualDecription.setText("");
                                    edt_IndividualItemTitle.setText("");
                                    txt_IndividualItemDate.setText("Select A Date");
                                }
                            }
                        });
                        //Link:https://www.youtube.com/watch?v=oNNHR-LeTnI
                        //-----------------------------------------------End------------------------------------------------------
                    }
                }catch(Exception e){

                }
            }
        });

        //Onclick for back image button,redirect user to the collectionItems.cs
        img_ReturnItemList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Clear arraylist
                ListUtils.itemsList.clear();
                ListUtils.collectionsItemList.clear();

                Intent returnItemIntent = new Intent(ItemDetails.this,CollectionItems.class); //Create intent
                returnItemIntent.putExtra("Collection",collectionSelected);//Pass collection object in intent
                startActivity(returnItemIntent);
            }
        });

        //---------------------------------------Code Attribution------------------------------------------------
        //Author:Foxandroid
        //Uses:Delete data from firebase realtime database
        img_Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myRef.child(users).child(userId).child(itemRef).child(itemId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(ItemDetails.this, "Item Successfully Deleted!", Toast.LENGTH_SHORT).show();
                            edt_IndividualItemTitle.setText("");
                            edt_IndividualItemBrand.setText("");
                            edt_IndividualDecription.setText("");
                            txt_IndividualItemDate.setText("");

                            txt_ItemTitle.setText("");
                            txt_ItemBrand.setText("");
                            txt_ItemDecription.setText("");
                            txt_ItemDate.setText("");

                        }else {
                            Toast.makeText(ItemDetails.this, "Delete Failed!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        //Link:https://www.youtube.com/watch?v=L3u6T8uzT58
        //-----------------------------------------------End------------------------------------------------------


    }

    //---------------------------------------Code Attribution------------------------------------------------
    //Author:CodingwithMitch,Stackoverflow(saulmm)
    //Uses:Datepicker Dialog, allow users to select date from datepicker dialog

    private void showDialog() {
        DatePickerDialog datePickerDialog= new DatePickerDialog(
                ItemDetails.this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }
    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        month = month+1;
        String date = day +"/"+month+"/"+year;
        txt_IndividualItemDate.setVisibility(View.VISIBLE);
        txt_IndividualItemDate.setText(date);
        dateAquired = date;
    }
    //Link:https://www.youtube.com/watch?v=hwe1abDO2Ag
    //Link:https://stackoverflow.com/questions/39916178/how-to-show-datepickerdialog-on-button-click
    //-----------------------------------------------End------------------------------------------------------
}