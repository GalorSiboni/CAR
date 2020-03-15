package com.example.car;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.car.Model.Accident;
import com.example.car.Model.Profile;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class AccidentAfterScanning extends AppCompatActivity {

    private TextView date, location;
    private ImageView image1,image2,image3;
    private ImageButton newImage, leftPic, rightPic;
    private int width, hight, counter = 0;
    private Uri filePath;
    private StorageTask uploadTask;
    private final int PICK_IMAGE_REQUEST = 71;
    private boolean isNewAccident;
    private Accident accident;

    //firebase
    FirebaseDatabase db;
    StorageReference storage;
    DatabaseReference accidentDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accident_after_scanning);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        hight = size.y;
        image1 = findViewById(R.id.image1);
        image2 = findViewById(R.id.image2);
        image3 = findViewById(R.id.image3);
        newImage = findViewById(R.id.new_images);
        leftPic = findViewById(R.id.leftPic);
        rightPic = findViewById(R.id.rightPic);
        image1.getLayoutParams().width = image2.getLayoutParams().width = image3.getLayoutParams().width = width/3;

        //Firebase init
        db = FirebaseDatabase.getInstance();
        accidentDB = db.getReference( Constants.FIRE_BASE_DB_ACCIDENTS_PATH);
        storage = FirebaseStorage.getInstance().getReference().child(Constants.FIRE_BASE_STORAGE_ACCIDENT_GALLERY);

        Button btnOtherDriverInfo = findViewById(R.id.otherDriverInfoBtn);
        date = findViewById(R.id.dateTextView);
        location = findViewById(R.id.locationTextView);

        isNewAccident =  getIntent().getBooleanExtra(Constants.INTENT_IS_NEW_ACCIDENT, true);
        MySharedPreferences pref = new MySharedPreferences(this);
        String json;
        if(isNewAccident)//if it is a new accident
        {
            json = pref.getString(Constants.KEY_SHARED_PREF_NEW_ACCIDENT, "");
        }
        else
        {
            json = pref.getString(Constants.KEY_SHARED_FREF_EXIST_ACCIDENT, "");
        }
        accident = new Gson().fromJson(json, Accident.class);

        date.setText(accident.getOpenDate());
        location.setText(accident.getLocationStr());
        leftPic.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (counter !=0)
                counter--;
            }
        });
        rightPic.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (counter < accident.getGallery().size())
                counter++;
            }
        });
        if (accident.getGallery() != null) {
            for (int i = counter; i < accident.getGallery().size(); i++) {
                if (!accident.getGallery().get( i ).trim().isEmpty() || accident.getGallery().get( i ) != null) {
                    Picasso.get().load( Uri.parse( accident.getGallery().get( i ) ) ).into( image1 );
                }
                if (!accident.getGallery().get( i + 1 ).trim().isEmpty() || accident.getGallery().get( i + 1 ) != null) {
                    Picasso.get().load( Uri.parse( accident.getGallery().get( i ) ) ).into( image2 );
                }
                if (!accident.getGallery().get( i + 2 ).trim().isEmpty() || accident.getGallery().get( i + 2 ) != null) {
                    Picasso.get().load( Uri.parse( accident.getGallery().get( i ) ) ).into( image3 );
                }
            }
        }
        // TODO: 14/03/2020 add new images and load current images to a gallery like form

                btnOtherDriverInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccidentAfterScanning.this, PopWindowUserInfo.class);
                intent.putExtra(Constants.INTENT_IS_NEW_ACCIDENT, isNewAccident);//is new accident or previous accident -> required to know which accident to present
                startActivity(intent);
                //finish();
            }
        });
        newImage.setOnClickListener( new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult( requestCode,resultCode,data );
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null)
        {
            filePath = data.getData();
            if (image1 == null) {
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap( getContentResolver(), filePath );
                    image1.setImageBitmap( bitmap );
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (image2 == null) {
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap( getContentResolver(), filePath );
                    image2.setImageBitmap( bitmap );
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (image3 == null) {
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap( getContentResolver(), filePath );
                    image3.setImageBitmap( bitmap );
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private void uploadPhoto(){
        if(filePath != null){
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle( "Uploading..." );
            progressDialog.show();

            final StorageReference ref = storage.child(accident.getAccidentId() + "/" + accident.getGallery().size() );
            uploadTask = ref.putFile(filePath).addOnSuccessListener( new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                    Toast.makeText( AccidentAfterScanning.this, "Upload Success", Toast.LENGTH_SHORT ).show();
                    ref.getDownloadUrl().addOnSuccessListener( new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String url = uri.toString();
                            //add new pic
                            accident.addToGallery( url );
                            //end new part
                            accidentDB.child(accident.getAccidentId()).child("" + accident.getGallery().size()).setValue(url);
                        }
                    } );
                }})
                    .addOnFailureListener( new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText( AccidentAfterScanning.this, "Failed "+e.getMessage() , Toast.LENGTH_SHORT).show();
                        }
                    } )
                    .addOnProgressListener( new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                            progressDialog.setMessage("Uploading " +(int)progress + "%");
                        }
                    } );
        }
    }
    private void chooseImage(){
        Intent intent = new Intent();
        intent.setType("image/*");// TODO: 12/03/2020 change
        intent.setAction( Intent.ACTION_GET_CONTENT );
        startActivityForResult( Intent.createChooser( intent,"SelectPicture"),PICK_IMAGE_REQUEST ); // TODO: 12/03/2020 change
    }
}
