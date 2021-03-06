package com.justinwei.auijustin;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private ImageView capturedPhoto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        capturedPhoto = (ImageView) findViewById(R.id.capturedPhoto);



        final Button btnTakeAPicture = (Button) findViewById(R.id.btnTakeAPicture);
        btnTakeAPicture.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });

        final Button btnChooseFromGallery = (Button) findViewById(R.id.btnChooseFromGallery);
        btnChooseFromGallery.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dispatchGalleryIntent();
            }
        });
        if (ContextCompat.checkSelfPermission(this,
                "android.permission.CAMERA")
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{"android.permission.CAMERA"},
                    0);
        }

        if (ContextCompat.checkSelfPermission(this,
                "android.permission.WRITE_EXTERNAL_STORAGE")
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"},
                    0);
        }

        if (ContextCompat.checkSelfPermission(this,
                "android.permission.READ_EXTERNAL_STORAGE")
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{"android.permission.READ_EXTERNAL_STORAGE"},
                    0);
        }
    }


    static final int REQUEST_TAKE_PHOTO = 1;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
        }
    }


    static final int REQUEST_GALLERY = 2;
    private void dispatchGalleryIntent() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, REQUEST_GALLERY);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(MainActivity.this, "Image saved to gallery.", Toast.LENGTH_SHORT).show();


                // Image captured and saved to fileUri specified in the Intent
                //This displays the picture on the screen
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(MainActivity.this, "Image capture canceled.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Image capture failed.", Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == REQUEST_GALLERY) {
            if (resultCode == RESULT_OK) {



                // Get the Image from data
                Uri imageURI = data.getData();


                ConnectivityManager connMgr = (ConnectivityManager)
                        getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()) {
                    Toast.makeText(MainActivity.this, "Image selected.", Toast.LENGTH_SHORT).show();

                    Intent startImageActivity = new Intent(MainActivity.this, ImageActivity.class);
                    startImageActivity.putExtra("imageUri", imageURI);
                    startActivity(startImageActivity);
                } else {
                    Toast.makeText(MainActivity.this, "No internet, selection canceled", Toast.LENGTH_LONG).show();
                }



            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(MainActivity.this, "Image selection canceled.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Image selection failed.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

