package com.varsitycollege.kn_beautyapp_2022_opsc_task2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.view.ViewCompat;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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
import java.util.Date;

public class CreateCollection extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    //Static variables
    public static final int CAMERA_REQUEST_CODE = 102; //Request code for camera
    public static final int CAMERA_PERM_CODE = 101; //Request code for camera permissions
    public static final int GALLERY_REQUEST_CODE = 105;//Request code for gallery

    //Global variables
    Spinner spinner;
    EditText edt_Name, edt_NumItem;
    TextView txt_NumItem, txt_Collection_Name, txt_Goal;
    Button btn_Create;
    Boolean goal;
    String name, goalInput,currentPhotoPath,num;
    Integer numGoal;
    ImageView img_Camera, img_Gallery,img_Collection,img_ReturnCollectionList;

    //Firebase realtime database reference
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();

   User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //---------------------------------------Code Attribution------------------------------------------------
        //Author:geeksforgeeks
        //Uses:Hides the action bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        //Link:https://www.geeksforgeeks.org/different-ways-to-hide-action-bar-in-android-with-examples/#:~:text=If%20you%20want%20to%20hide,AppCompat
        //-----------------------------------------------End------------------------------------------------------
        setContentView(R.layout.activity_create_collection);

        //getting the current user data
         user = ListUtils.usersList.get(0);

        ListUtils.collectionImageList.clear();

        //find components
        spinner = findViewById(R.id.spn_CollectionGoal);
        txt_NumItem = findViewById(R.id.txt_AddItems);
        txt_Collection_Name = findViewById(R.id.txt_collectionName);
        txt_Goal = findViewById(R.id.txt_addGoal);
        edt_Name = findViewById(R.id.edt_collectionName);
        edt_NumItem = findViewById(R.id.edt_CollectionGoalNoItems);
        btn_Create = findViewById(R.id.btn_CreateCollection);
        img_Collection = findViewById(R.id.img_CollectionImg);
        img_Camera = findViewById(R.id.img_CollectionCamera);
        img_Gallery = findViewById(R.id.img_CollectionGallery);
        img_ReturnCollectionList =findViewById(R.id.img_ReturnCollectionList);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); //create a simple layout resource file for each spinner component
        spinner.setAdapter(adapter);

        //Apply OnItemSelectedListener to the Spinner instance to determine which item of the spinner is clicked.
        spinner.setOnItemSelectedListener(this);

        //when the <- button selected, the user redirected to the home page
        img_ReturnCollectionList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ListUtils.collectionList.clear();
                Intent returnCollectionList = new Intent(CreateCollection.this,Home.class);
                startActivity(returnCollectionList);

            }
        });

        //onclick listener on the camera button to allow user to open the camera,
        //let the user take a picture and display it in the imageview
        img_Camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                askCameraPermissions(); //Calling method that asks user for camera permission
                img_Collection.setVisibility(View.VISIBLE);
            }
        });

        //when the gallery icon is clicked,allow user to open the gallery, and enables user to select an image and display it in the imageview
        img_Gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(gallery, GALLERY_REQUEST_CODE);
                img_Collection.setVisibility(View.VISIBLE);
            }
        });

        //when create button clicked, a collection is created and written to firebase realtime database
        btn_Create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Validation val = new Validation();
                try {

                    //Random Collection id
                    String id = KeyGenerator.getRandomString(8);

                    //validating the name of collection
                    //---------------------------------------Code Attribution------------------------------------------------
                    //Author:GeeksforGeeks
                    //Uses:validate if the input is empty and display field error, so the user know which component is an error
                    if (edt_Name.length()==0){
                        edt_Name.setError("This field is required!");
                        // Link:https://www.geeksforgeeks.org/implement-form-validation-error-to-edittext-in-android/
                        // -----------------------------------------------End------------------------------------------------------
                    }else{
                        try {
                            if(val.isNullOrEmpty(edt_Name.getText().toString())==true || val.isAlphabet(edt_Name.getText().toString())== false){
                                Toast.makeText(CreateCollection.this, "Enter in letter only!", Toast.LENGTH_SHORT).show();
                            }
                        }catch (Exception e){
                        }
                        //obtaining the user input entered to create a new collection
                        name = edt_Name.getText().toString();
                    }

                    //Check if goal is yes/no
                    if (goalInput.equals("Yes")) {
                        //validating the goal number of items for the collection
                        //---------------------------------------Code Attribution------------------------------------------------
                        //Author:GeeksforGeeks
                        //Uses:validate if the input is empty and display field error, so the user know which component is an error
                        if (edt_NumItem.length()==0){
                            edt_NumItem.setError("This field is required!");
                            // Link:https://www.geeksforgeeks.org/implement-form-validation-error-to-edittext-in-android/
                            // -----------------------------------------------End------------------------------------------------------
                        }else{
                            try {
                                if (val.isNumeric(edt_NumItem.getText().toString())==false || val.isNullOrEmpty(edt_NumItem.getText().toString())==true){
                                    Toast.makeText(CreateCollection.this, "Enter in number only!", Toast.LENGTH_SHORT).show();
                                }
                                if((numGoal<0) && (numGoal>30)){
                                    Toast.makeText(CreateCollection.this, "Enter in valid goal number of items!", Toast.LENGTH_SHORT).show();
                                }
                            }catch (Exception e){
                            }
                            //obtaining the number entered for the goal number items
                            num = edt_NumItem.getText().toString();
                            numGoal = Integer.parseInt(num);
                        }
                    } else {
                        numGoal = 0;
                    }

                    //validation for image
                    try{
                        if (ListUtils.collectionImageList.size()<=0) {
                            Toast.makeText(CreateCollection.this, "Please attach image!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    catch(Exception e) {
                    }

                    //validated the inputs, if they are valid then collection is created and written to firebase realtime database
                    if (val.isNullOrEmpty(edt_Name.getText().toString())==true || val.isAlphabet(edt_Name.getText().toString())== false
                            || ListUtils.collectionImageList.size()<=0 ||
                            (numGoal<0) && (numGoal>30)){
                        Toast.makeText(CreateCollection.this, "Please check your inputs!", Toast.LENGTH_SHORT).show();
                    }else{

                        //create instance of collection class
                        Collection  c = new Collection(id,name, numGoal, goal,ListUtils.collectionImageList.get(0));

                        //---------------------------------------Code Attribution------------------------------------------------
                        //Author:Sarina Till
                        //Uses:Write data to firebase realtime database
                        myRef.child("Users").child(user.getUserID()).child("Collections").child(id).setValue(c)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(CreateCollection.this, "Success", Toast.LENGTH_SHORT).show();

                                        //clear the collection list with all collection instances
                                        ListUtils.collectionList.clear();

                                        //redirect user to the Home screen to display all their collections
                                        Intent i = new Intent(CreateCollection.this, Home.class);
                                        startActivity(i);
                                        finish();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(CreateCollection.this, "oops", Toast.LENGTH_SHORT).show();
                            }
                        });
                        //Link:https://www.youtube.com/watch?v=ej5dGlP3kdQ&list=PL480DYS-b_kdor_f0IFgS7iiEsOwxdx6w&index=24
                        //-----------------------------------------------End------------------------------------------------------
                    }
                }catch(Exception e){
                    Toast.makeText(CreateCollection.this, "Create Collection Failed!, Check inputs", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    //OnItemSelect for spinner
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        try {
            //If the user selects "yes" to input a goal an editText is displayed, else no editText is displayed.
            goalInput = String.valueOf(parent.getItemAtPosition(position));

            if (goalInput.equals("Yes")) {
                txt_NumItem.setVisibility(View.VISIBLE);
                edt_NumItem.setVisibility(View.VISIBLE);
                goal = true;
            }else{
                txt_NumItem.setVisibility(View.INVISIBLE);
                edt_NumItem.setVisibility(View.INVISIBLE);
                goal = false;
                numGoal = 0;
            }
        }catch (Exception e){

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        txt_NumItem.setVisibility(View.INVISIBLE);
        edt_NumItem.setVisibility(View.INVISIBLE);
        goal = false;
        numGoal = 0;
    }

    //---------------------------------------Code Attribution------------------------------------------------
    //Author:SmallAcademy
    //Uses:Select an image from Camera(ask permissions) or Gallery,upload the image to firebase storage and display image in imageView

    //Method ask the camera permissions
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
    public void onRequestPermissionResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 101) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent();
            } else {
                Toast.makeText(this, "Camera permission is required to use camera", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //Opening camera
    private void openCamera() {
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camera, CAMERA_REQUEST_CODE);
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
                Uri contentUri = data.getData(); //Creating content URI from the data
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()); //Creating filename
                String imageFileName = "JPEG_" + timeStamp + "." + getFileExt(contentUri); //Specifying the file type
                Log.d("tag", "OnActivityResult: Gallery Image Uri:     " + imageFileName); //Displaying absolute Uri through ImageView

                //Method call to upload the data to firebase storage
                UploadImageToFirebase(imageFileName, contentUri);

            }
        }
    }

    //---------------------------------------Code Attribution------------------------------------------------
    //Author:SmallAcademy
    //Uses:Upload image to firebase storage
    private void UploadImageToFirebase(String name, Uri contentUri) {

        //Firebase storage reference
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();
        StorageReference image = storageReference.child("images/" +user.getUserID()+"/" +name);

      image.putFile(contentUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
          //Called when image upload is successful
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> downloadUri = taskSnapshot.getStorage().getDownloadUrl();
                downloadUri.addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(img_Collection); //Getting the Uri from firebase and using the picasso class to display the image
                        ListUtils.collectionImageList.clear();
                        // get the url for image in firebase storage and add it to an Arraylist
                        String genFilePath = downloadUri.getResult().toString();
                        //Toast.makeText(CreateCollection.this, genFilePath, Toast.LENGTH_SHORT).show();
                        ListUtils.collectionImageList.add(genFilePath);
                    }
                });
                //When upload is successful the following message appears to the user
                Toast.makeText(CreateCollection.this, "Image is uploaded", Toast.LENGTH_SHORT).show();
            }
          //Called if uploading image has failed
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CreateCollection.this, "Upload failed", Toast.LENGTH_SHORT).show();
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

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()); //Simple date format created to get timestamp
        String imageFileName = "JPEG_" + timeStamp + "_"; //Creating the image file , stored with JPEG name and the timestamp created above
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile( //Creating an image file and passing parameters
                imageFileName,".jpg",storageDir);

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath(); //Getting absolute path where image is saved
        return image;
    }


    private void dispatchTakePictureIntent() {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); //Creating a new camera

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
                        "com.varsitycollege.kn_beautyapp_2022_opsc_task2.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
            }
        }
    }
    //Link:https://www.youtube.com/watch?v=s1aOlr3vbbk&list=PLlGT4GXi8_8eopz0Gjkh40GG6O5KhL1V1&index=2
    //Link:https://www.youtube.com/watch?v=KaDwSvOpU5E&list=PLlGT4GXi8_8eopz0Gjkh40GG6O5KhL1V1&index=3
    //Link:https://www.youtube.com/watch?v=q5pqnT1n-4s&list=PLlGT4GXi8_8eopz0Gjkh40GG6O5KhL1V1&index=4
    //-----------------------------------------------End------------------------------------------------------
}


