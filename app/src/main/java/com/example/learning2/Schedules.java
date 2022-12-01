package com.example.learning2;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;

import android.provider.MediaStore;
import android.view.View;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.learning2.Utils.SubjDatabaseHandler;

import com.example.learning2.Utils.UriDatabaseHandler;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import java.util.Objects;

public class Schedules extends AppCompatActivity {

    // One Button
    FloatingActionButton BSelectImage;

    Button btn;

    // Image load into
    ImageView IVPreviewImage;


    // constant to compare
    // the activity result code
    int SELECT_PICTURE = 200;
    int PICK_IMAGE = 100;

    String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.schedule);

        Objects.requireNonNull(getSupportActionBar()).hide();

        // Register the View items
        BSelectImage = findViewById(R.id.addImage);

        IVPreviewImage = findViewById(R.id.IVPreviewImage);

        btn = findViewById(R.id.load_image);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String filename = "img.jpeg";
                if(IVPreviewImage.getDrawable() !=null){

                        Bitmap bm = ((BitmapDrawable) IVPreviewImage.getDrawable()).getBitmap();

                    //saveToInternalStorage(bm);
                    // create thee directory and file in the internal memory
                        createDirectoryAndSaveFile(bm,filename);
                }

                // loadImageFromStorage("/sdcard/",filename);
                // load the image from the saved images
                loadImageFromStorage(path,filename);


            }
        });


        // handle the Choose Image button to trigger
        // the image chooser function

        BSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                imageChooser();

            }
        });

    }


    // this function is triggered when
    // the Select Image Button is clicked
    void imageChooser() {

        // create an instance of the
        // intent of the type image
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        // pass the constant to compare it
        // with the returned requestCode
        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
    }

    // this function is triggered when user
    // selects the image from the imageChooser
    // called from the imageChooser
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


            if (resultCode == RESULT_OK) {

                // compare the resultCode with the
                // SELECT_PICTURE constant
                if (requestCode == SELECT_PICTURE) {
                    // Get the url of the image from data
                    Uri selectedImageUri = data.getData();

                    if (null != selectedImageUri) {
                        // update the preview image in the layout
                        IVPreviewImage.setImageURI(selectedImageUri);

                        UriDatabaseHandler db = new UriDatabaseHandler(getApplicationContext());
                        db.insertUri(selectedImageUri.toString());
                    }
                }
            }


    }



    /*
     * get the image from the image view and save
     *
     */
    private void createDirectoryAndSaveFile(Bitmap imageToSave, String fileName) {

        File direct = new File(Environment.getExternalStorageDirectory() + "/DirName");

        if (!direct.exists()) {

            File wallpaperDirectory = new File(Environment.getExternalStorageDirectory() + "/DirName");

            wallpaperDirectory.mkdirs();
            direct = wallpaperDirectory;


            //Toast.makeText(this, "Directory created in internal" , Toast.LENGTH_SHORT).show();
        }

        File file = new File(Environment.getExternalStorageDirectory() + "/DirName/", fileName);

        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (file.exists()) {
            file.delete();

        }
        try {

            FileOutputStream out = new FileOutputStream(Environment.getExternalStorageDirectory()+"/DirName/"+fileName);
            //FileOutputStream out = new FileOutputStream(file);

            imageToSave.compress(Bitmap.CompressFormat.JPEG, 100, out);

            out.flush();
            out.close();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    /**
     * load the image into the image viewer
     * @param path
     * @param filename
     */
    private void loadImageFromStorage(String path,String filename)
    {

        try {
            File f=new File(path, filename);

            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));

            ImageView img=(ImageView)findViewById(R.id.IVPreviewImage);
            img.setImageBitmap(b);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

    }
}
