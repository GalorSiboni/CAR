package com.example.car;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
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
import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class AccidentAfterScanning extends AppCompatActivity {

    private TextView date, location;
    private ImageView profileIcon, logOut, image1, image2, image3, qrCode;
    private ImageButton newImage, saveImage, leftPic, rightPic;
    private int width, hight, counter = 0;
    private Uri filePath;
    private StorageTask uploadTask;
    private final int PICK_IMAGE_REQUEST = 71;
    private boolean isNewAccident;

    //Shared Preferences
    MySharedPreferences pref;
    String json;
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

        findViews();
        image1.getLayoutParams().width = image2.getLayoutParams().width = image3.getLayoutParams().width = width/3;
        saveImage.setVisibility(View.GONE);

        //Firebase init
        db = FirebaseDatabase.getInstance();
        accidentDB = db.getReference(Constants.FIRE_BASE_DB_ACCIDENTS_PATH);
        storage = FirebaseStorage.getInstance().getReference().child(Constants.FIRE_BASE_STORAGE_ACCIDENT_GALLERY);

        Button btnOtherDriverInfo = findViewById(R.id.otherDriverInfoBtn);
        Button btnWitnessInfo = findViewById(R.id.witnessInfoBtn);
        date = findViewById(R.id.dateTextView);
        location = findViewById(R.id.locationTextView);

        isNewAccident =  getIntent().getBooleanExtra(Constants.INTENT_IS_NEW_ACCIDENT, true);
        pref = new MySharedPreferences(this);
        if(isNewAccident){
            //if it s a new accident that just got scanned
            json = pref.getString(Constants.KEY_SHARED_PREF_NEW_ACCIDENT, "");
        }
        else {
            json = pref.getString(Constants.KEY_SHARED_FREF_EXIST_ACCIDENT, "");
        }
        accident = new Gson().fromJson(json, Accident.class);

        date.setText(String.format("%s%s", date.getText(), accident.getOpenDate()));
        location.setText(String.format("%s%s", location.getText(), accident.getLocationStr()));
        generateQrCode();

        if (accident.getGallery().size() > 3)
            setAllImages(0);
        else
            updateImageViews();

        profileIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccidentAfterScanning.this, EditProfile.class);
                startActivity(intent);
            }
        });

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AccidentAfterScanning.this, MainActivity.class));
                finish();
            }
        });

        leftPic.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (counter > 0 && accident.getGallery().size() > 3){
                    counter--;
                    setAllImages(counter);
                }
            }
        });

        rightPic.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (accident.getGallery() != null && counter + 3 < accident.getGallery().size()) {
                    counter++;
                    setAllImages(counter);
                    }
            }
        });

        btnOtherDriverInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccidentAfterScanning.this, PopWindowUserInfo.class);
                intent.putExtra(Constants.INTENT_IS_NEW_ACCIDENT, isNewAccident);//is new accident or previous accident -> required to know which accident to present
                startActivity(intent);
            }
        });
        btnWitnessInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccidentAfterScanning.this, PopWindowUserInfo.class);
                intent.putExtra(Constants.INTENT_IS_NEW_ACCIDENT, isNewAccident);//is new accident or previous accident -> required to know which accident to present
                startActivity(intent);
            }
        });

        newImage.setOnClickListener( new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
            @Override
            public void onClick(View v) {
                chooseImage();
                saveImage.setVisibility( View.VISIBLE );
                newImage.setVisibility( View.GONE );
            }
        });
        saveImage.setOnClickListener( new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
            @Override
            public void onClick(View v) {
                uploadPhoto();
                newImage.setVisibility( View.VISIBLE );
                saveImage.setVisibility( View.GONE );
            }
        });
    }

    private void findViews() {
        profileIcon = findViewById(R.id.profileIcon);
        logOut = findViewById(R.id.logOutIcon);
        image1 = findViewById(R.id.image1);
        image2 = findViewById(R.id.image2);
        image3 = findViewById(R.id.image3);
        newImage = findViewById(R.id.new_images);
        saveImage = findViewById(R.id.saveImage);
        leftPic = findViewById(R.id.leftPic);
        rightPic = findViewById(R.id.rightPic);
        qrCode = findViewById( R.id.witnessQrCode );
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
            else if (image2 == null) {
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap( getContentResolver(), filePath );
                    image2.setImageBitmap( bitmap );
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else if (image3 == null) {
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
                            saveData();
                            accidentDB.child(accident.getAccidentId()).setValue( accident );
                            if(accident.getGallery().size() > 3)
                                setAllImages(accident.getGallery().size() - 3);
                            else updateImageViews();
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
        intent.setType("image/*"); //evry image type
        intent.setAction( Intent.ACTION_GET_CONTENT );
        startActivityForResult( Intent.createChooser( intent,"SelectPicture"),PICK_IMAGE_REQUEST );
    }
    private void saveData() {
        json = new Gson().toJson(accident);
        pref.putString(Constants.KEY_SHARED_PREF_NEW_ACCIDENT, json);
    }
    private void setAllImages(int num){
        Picasso.get().load( Uri.parse( accident.getGallery().get( num ) ) ).into( image1 );
        Picasso.get().load( Uri.parse( accident.getGallery().get( num + 1 ) ) ).into( image2 );
        Picasso.get().load( Uri.parse( accident.getGallery().get( num + 2 ) ) ).into( image3 );
    }
    private void updateImageViews() {
        if (accident.getGallery() != null) {
            switch (accident.getGallery().size()) {
                case 1: Picasso.get().load( Uri.parse( accident.getGallery().get( 0 ) ) ).into( image1 );
                    break;
                case 2: Picasso.get().load( Uri.parse( accident.getGallery().get( 0 ) ) ).into( image1 );
                    Picasso.get().load( Uri.parse( accident.getGallery().get( 1 ) ) ).into( image2 );
                    break;
                case 3: setAllImages(0);
                    break;
            }
        }
    }
    private void generateQrCode()
    {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = qrCodeWriter.encode(accident.getAccidentId(), BarcodeFormat.QR_CODE, 150, 150, null);
            Bitmap bitmap = Bitmap.createBitmap(150, 150, Bitmap.Config.RGB_565);

            for (int x = 0; x < 150; x++) {
                for (int y = 0; y < 150; y++) {
                    bitmap.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
            qrCode.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
