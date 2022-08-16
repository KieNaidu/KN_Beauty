package com.varsitycollege.kn_beautyapp_2022_opsc_task2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CreateItem extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    //Static variables
    public static final int CAMERA_REQUEST_CODE = 102; //Request code for camera
    public static final int CAMERA_PERM_CODE = 101; //Request code for camera permissions
    public static final int GALLERY_REQUEST_CODE = 105;

    //Global variables
    EditText edt_Title, edt_Brand, edt_Description,edt_DateAquired;
    Button btn_CreateItem;
    TextView  txt_CreatedCollectionName;
    String title, description, brand, dateAquired, currentPhotoPath;
    ImageView img_ImgAttach, img_GalleryItem, img_CameraItem, img_BackToCollection,img_SelectDate;
    Collection collectionSelected;
    User user;

    //Firebase realtime database Reference
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();

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
        setContentView(R.layout.activity_create_item);

        //getting the current user data
        user = ListUtils.usersList.get(0);

        //Create intent to get the collection object passed in the intent
        Intent i = getIntent();
        collectionSelected = i.getParcelableExtra("CollectionInfo");

        //find components
        edt_Title = findViewById(R.id.edt_Title);
        edt_Description = findViewById(R.id.edt_Description);
        edt_Brand = findViewById(R.id.edt_Brand);
        btn_CreateItem = findViewById(R.id.btn_itemCreate);
        img_SelectDate = findViewById(R.id.img_SelectDate);
        img_GalleryItem = findViewById(R.id.img_GalleryItem);
        img_CameraItem = findViewById(R.id.img_CameraItem);
        img_ImgAttach = findViewById(R.id.img_ItemImg);
        img_BackToCollection = findViewById(R.id.img_BackToCollection);
        txt_CreatedCollectionName = findViewById(R.id.txt_CreatedCollectionName);
        edt_DateAquired = findViewById(R.id.edt_DateAquired);

        //Display Heading
        txt_CreatedCollectionName.setText(collectionSelected.getCollectionName());

        // when <- clicked the user will be redirected to the collection item page
        img_BackToCollection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ListUtils.itemsList.clear();
                Intent returnToCollection = new Intent(CreateItem.this, CollectionItems.class);
                returnToCollection.putExtra("Collection", collectionSelected);
                startActivity(returnToCollection);
            }
        });

        // onclick listener on the gallery button to allow user to open the gallery,
        // let the user select an image and display it in the imageview
        img_GalleryItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(gallery, GALLERY_REQUEST_CODE);
                img_ImgAttach.setVisibility(view.VISIBLE);
            }
        });

        //onclick listener on the camera button to allow user to open the camera,
        //let the user take a picture and display it in the imageview
        img_CameraItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                askCameraPermissions(); //Calling method that asks user for camera permission
                img_ImgAttach.setVisibility(view.VISIBLE);
            }
        });

        //when the user clicks on the calender icon, to add a date.
        img_SelectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //method call to show datepicker dialog
                showDialog();
            }
        });

        //button onclick,when create button is clicked an Item is created and Written to firebase realtime database
        btn_CreateItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Validation val = new Validation();
                //Adding To Firebase
                //string values used in the database reference
                String userId = user.getUserID();
                String users = "Users";
                String item = "Items";

                try {
                    // random item id
                    String itemId = KeyGenerator.getRandomString(5);

                    //collection id
                    String collectionId = collectionSelected.getCollectionId();

                    //validate the item title
                    //---------------------------------------Code Attribution------------------------------------------------
                    //Author:GeeksforGeeks
                    //Uses:validate if the input is empty and display field error, so the user know which component is an error
                    if (edt_Title.length() == 0) {
                        edt_Title.setError("This field is required!");
                        // Link:https://www.geeksforgeeks.org/implement-form-validation-error-to-edittext-in-android/
                        // -----------------------------------------------End------------------------------------------------------

                    } else {
                        try {
                            if (val.isNullOrEmpty(edt_Title.getText().toString()) == true || val.isAlphabet(edt_Title.getText().toString()) == false) {
                                Toast.makeText(CreateItem.this, "Enter in letter only!", Toast.LENGTH_SHORT).show();
                            }
                            title = edt_Title.getText().toString();
                        } catch (Exception e) {
                            Toast.makeText(CreateItem.this, e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    //Validate description input
                    //---------------------------------------Code Attribution------------------------------------------------
                    //Author:GeeksforGeeks
                    //Uses:validate if the input is empty and display field error, so the user know which component is an error
                    if (edt_Description.length() == 0) {
                        edt_Description.setError("This field is required!");
                        // Link:https://www.geeksforgeeks.org/implement-form-validation-error-to-edittext-in-android/
                        // -----------------------------------------------End------------------------------------------------------

                    } else {
                        try {
                            if (val.isNullOrEmpty(edt_Description.getText().toString()) == true || val.isAlphabet(edt_Description.getText().toString()) == false) {
                                Toast.makeText(CreateItem.this, "Enter in letter only!", Toast.LENGTH_SHORT).show();
                            }
                            description = edt_Description.getText().toString();
                        } catch (Exception e) {
                            Toast.makeText(CreateItem.this, e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    //Validate brand input
                    //---------------------------------------Code Attribution------------------------------------------------
                    //Author:GeeksforGeeks
                    //Uses:validate if the input is empty and display field error, so the user know which component is an error
                    if (edt_Brand.length() == 0) {
                        edt_Brand.setError("This field is required!");
                        // Link:https://www.geeksforgeeks.org/implement-form-validation-error-to-edittext-in-android/
                        // -----------------------------------------------End------------------------------------------------------

                    } else {
                        try {
                            if (val.isNullOrEmpty(edt_Brand.getText().toString()) == true || val.isAlphabet(edt_Brand.getText().toString()) == false) {
                                Toast.makeText(CreateItem.this, "Enter in letter only!", Toast.LENGTH_SHORT).show();
                            }
                            brand = edt_Brand.getText().toString();
                        } catch (Exception e) {
                            Toast.makeText(CreateItem.this, e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    //validate dateAquired selected
                    try {
                        if (val.isNullOrEmpty(dateAquired) == true) {
                            Toast.makeText(CreateItem.this, "Select a date!", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(CreateItem.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }

                    //validate image
                    try {
                        if (ListUtils.itemImageList.size() <= 0) {
                            Toast.makeText(CreateItem.this, "Please attach image!", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(CreateItem.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }

                    //Checking if the input is valid before pushing it to firebase
                    if (val.isNullOrEmpty(edt_Title.getText().toString()) == true || val.isAlphabet(edt_Title.getText().toString()) == false
                            || val.isNullOrEmpty(edt_Brand.getText().toString()) == true || val.isAlphabet(edt_Description.getText().toString()) == false
                            || val.isNullOrEmpty(edt_Description.getText().toString()) == true || val.isAlphabet(edt_Brand.getText().toString()) == false
                            || val.isNullOrEmpty(dateAquired) == true
                            || ListUtils.itemImageList.size() <= 0) {
                        Toast.makeText(CreateItem.this, "Please check your inputs!", Toast.LENGTH_SHORT).show();
                    } else {
                        //Create item instance
                        Item i = new Item(itemId, collectionId, title, brand, description, dateAquired, ListUtils.itemImageList.get(0));

                        //---------------------------------------Code Attribution------------------------------------------------
                        //Author:Sarina Till
                        //Uses:Write data to firebase realtime database
                        // push data to firebase for specfic user
                        myRef.child(users).child(userId).child(item).child(itemId).setValue(i)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(CreateItem.this, "Success", Toast.LENGTH_SHORT).show();

                                        //clear the collection list with all collection instances
                                        ListUtils.itemsList.clear();

                                        //redirect user to the Home screen to display all their collections
                                        Intent i = new Intent(CreateItem.this, CollectionItems.class);
                                        i.putExtra("Collection", collectionSelected);
                                        startActivity(i);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(CreateItem.this, "oops", Toast.LENGTH_SHORT).show();
                                    }
                                });
                        //Link:https://www.youtube.com/watch?v=ej5dGlP3kdQ&list=PL480DYS-b_kdor_f0IFgS7iiEsOwxdx6w&index=24
                        //-----------------------------------------------End------------------------------------------------------

                    }
                }catch(Exception e){
                    Toast.makeText(CreateItem.this, "Creating Item Failed, check inputs!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //---------------------------------------Code Attribution------------------------------------------------
    //Author:CodingwithMitch,Stackoverflow(saulmm)
    //Uses:Datepicker Dialog, allow users to select date from datepicker dialog

    @SuppressLint("ResourceAsColor")
    private  void showDialog(){
        DatePickerDialog datePickerDialog= new DatePickerDialog(
                CreateItem.this,
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
        edt_DateAquired.setVisibility(View.VISIBLE);
        edt_DateAquired.setText(date);
        dateAquired = date;
    }

    //Link:https://www.youtube.com/watch?v=hwe1abDO2Ag
    //Link:https://stackoverflow.com/questions/39916178/how-to-show-datepickerdialog-on-button-click
    //-----------------------------------------------End------------------------------------------------------

    //---------------------------------------Code Attribution------------------------------------------------
    //Author:SmallAcademy
    //Uses:Select an image from Camera(ask permissions) or Gallery,upload the image to firebase storage and display image in imageView

    // Method ask the camera permissions
    //If camera permission is not granted, requesting the permission on runtime.
    private void askCameraPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERM_CODE);
        } else {
            dispatchTakePictureIntent();
        }
    }

    //Method checks if permission is granted for camera
    //If this condition is true - the user has given permission to use the camera
    public void onRequestPermissionResult(int requestCode, @NonNull String[] permissions,
                                          @NonNull int[] grantResults) {
        if (requestCode == 101) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent();
            } else {
                Toast.makeText(this, "Camera permission is required to use camera", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //Opening Camera
    private void openCamera() {
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camera, CAMERA_REQUEST_CODE);
        finish();
    }


    //Checking if the request is a camera or gallery request
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //If camera request
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                File f = new File(currentPhotoPath);
                Log.d("tag", "Absolute URI of image is " + Uri.fromFile(f));

                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri contentUri = Uri.fromFile(f);
                mediaScanIntent.setData(contentUri);
                this.sendBroadcast(mediaScanIntent);

                //Method call to upload the data to firebase storage
                UploadImageToFirebase(f.getName(), contentUri);
            }
        }

        //if gallery request
        if (requestCode == GALLERY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {

                Uri contentUri = data.getData();//Creating content URI from the data
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());//Creating filename
                String imageFileName = "JPEG_" + timeStamp + "." + getFileExt(contentUri);// Specifying the file type
                Log.d("tag", "OnActivityResult: Gallery Image Uri:     " + imageFileName);//Displaying absolute Uri through ImageView

                //Method call to upload image to Firebase storage
                UploadImageToFirebase(imageFileName, contentUri);
            }
        }
    }

    //---------------------------------------Code Attribution------------------------------------------------
    //Author:SmallAcademy
    //Uses:Upload image to firebase storage

    //Method to push image to Firebase storage
    private void UploadImageToFirebase(String name, Uri contentUri) {

        //Firebase storage reference
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();
        StorageReference image = storageReference.child("images/" + user.getUserID() + "/" + name);

        image.putFile(contentUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            //Called when image upload is successful
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> downloadUri = taskSnapshot.getStorage().getDownloadUrl();
                downloadUri.addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        Picasso.get().load(uri).into(img_ImgAttach); //Getting the Uri from firebase and using the picasso class to display the image
                        ListUtils.itemImageList.clear();

                        // get the url for image in firebase storage and add it to an Arraylist
                        String genFilePath = downloadUri.getResult().toString();
                        ListUtils.itemImageList.add(genFilePath);
                    }
                });

                //When upload is successful the following message appears to the user
                Toast.makeText(CreateItem.this, "Image is uploaded", Toast.LENGTH_SHORT).show();
            }

            //Called if uploading image has failed
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CreateItem.this, "Upload failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
    //Link:https://www.youtube.com/watch?v=dKX2V992pWI&list=PLlGT4GXi8_8eopz0Gjkh40GG6O5KhL1V1&index=5
    //-----------------------------------------------End------------------------------------------------------


    private String getFileExt(Uri contentUri) {
        ContentResolver c = getContentResolver(); //Get extension of image that user has selected
        MimeTypeMap mime = MimeTypeMap.getSingleton(); //Lists out all the supported types of images
        return mime.getExtensionFromMimeType(c.getType(contentUri)); //Gets the extension of the mimetype from the Url, selected from gallery
    }

    //Method creates an image file name
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()); //Simple date format created to get timestamp
        String imageFileName = "JPEG_" + timeStamp + "_"; //Creating the image file , stored with JPEG name and the timestamp created above
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);//Getting the storage directory where the file is being stored
        File image = File.createTempFile( //Creating an image file and passing parameters
                imageFileName,
                ".jpg", //Extension used for the image
                storageDir   //Directory to save the image
        );
        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath(); //Getting absolute path where image is saved
        return image;
    }

    //Method handles picture with camera
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); //Creating a new camera intent

        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) { //Checks if the camera is present in the device or not
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile(); //Returns image with the file name
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this, //Creating a photo URI to get the URI for the file
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
                finish();
            }
        }
    }
    //Link:https://www.youtube.com/watch?v=s1aOlr3vbbk&list=PLlGT4GXi8_8eopz0Gjkh40GG6O5KhL1V1&index=2
    //Link:https://www.youtube.com/watch?v=KaDwSvOpU5E&list=PLlGT4GXi8_8eopz0Gjkh40GG6O5KhL1V1&index=3
    //Link:https://www.youtube.com/watch?v=q5pqnT1n-4s&list=PLlGT4GXi8_8eopz0Gjkh40GG6O5KhL1V1&index=4
    //-----------------------------------------------End------------------------------------------------------
}
