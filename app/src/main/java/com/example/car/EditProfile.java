package com.example.car;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.car.Model.Profile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;


public class EditProfile extends AppCompatActivity {
    private EditText firstNameEdit, lastNameEdit, phoneNumberEdit, addressEdit, driverNameEdit, idEdit, carNumberEdit, carModelEdit, carColorEdit,
            licenceNumberEdit, ownerAddressEdit, ownerPhoneNumberEdit, insuranceCompanyNameEdit, insurancePolicyNumberEdit, insuranceAgentNameEdit, insuranceAgentPhoneNumEdit;
    private ImageView profilePicture;
    private Button save,edit,choose;
    private String userName,password,mail;
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 71;
    //Firebase
    FirebaseDatabase db;
    FirebaseStorage storage;
    DatabaseReference users;
    StorageReference photos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_profile );
        firstNameEdit = findViewById( R.id.firstNameEdit );
        lastNameEdit = findViewById( R.id.lastNameEdit );
        phoneNumberEdit = findViewById( R.id.phoneNumberEdit );
        addressEdit = findViewById( R.id.addressEdit );
        driverNameEdit = findViewById( R.id.driverNameEdit );
        idEdit = findViewById( R.id.idEdit );
        carNumberEdit = findViewById( R.id.carNumberEdit );
        carModelEdit = findViewById( R.id.carModelEdit );
        carColorEdit = findViewById( R.id.carColorEdit );
        licenceNumberEdit = findViewById( R.id.licenceNumberEdit );
        ownerAddressEdit = findViewById( R.id.ownerAddressEdit );
        ownerPhoneNumberEdit = findViewById( R.id.ownerPhoneNumberEdit );
        insuranceCompanyNameEdit = findViewById( R.id.insuranceCompanyNameEdit );
        insurancePolicyNumberEdit = findViewById( R.id.insurancePolicyNumberEdit );
        insuranceAgentNameEdit = findViewById( R.id.insuranceAgentNameEdit );
        insuranceAgentPhoneNumEdit = findViewById( R.id.insuranceAgentPhoneNumEdit );
        profilePicture = findViewById( R.id.profilePicture );
        // buttons
        edit = findViewById( R.id.edit );
        save = findViewById( R.id.Save );
        save.setVisibility( View.GONE );
        choose = findViewById( R.id.photoChooser );
        choose.setVisibility( View.GONE );
        final EditText[] editTextsArr = {firstNameEdit, lastNameEdit, phoneNumberEdit, addressEdit, driverNameEdit, idEdit, carNumberEdit, carModelEdit, carColorEdit,
                licenceNumberEdit, ownerAddressEdit, ownerPhoneNumberEdit, insuranceCompanyNameEdit, insurancePolicyNumberEdit, insuranceAgentNameEdit, insuranceAgentPhoneNumEdit};
        editMode( editTextsArr,false );

        Intent intent = getIntent();
        userName = intent.getStringExtra( "userName" );

        //Firebase init
        db = FirebaseDatabase.getInstance();
        users = db.getReference( "Profiles" );// TODO: 19/02/2020 need to change to const path!!!
        storage = FirebaseStorage.getInstance();
        photos = storage.getReference();

        users.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Profile myProfile = dataSnapshot.child( userName ).getValue( Profile.class );
                password = myProfile.getPassword();
                mail = myProfile.getMail();
                firstNameEdit.setText( myProfile.getFirstName() );
                lastNameEdit.setText( myProfile.getLastName() );
                carNumberEdit.setText( myProfile.getCarNumber() );
                carModelEdit.setText( myProfile.getCarModel() );
                carColorEdit.setText( myProfile.getCarColor() );
                driverNameEdit.setText( myProfile.getDriverName() );
                idEdit.setText( myProfile.getId() );
                addressEdit.setText( myProfile.getAddress() );
                licenceNumberEdit.setText( myProfile.getLicenceNumber() );
                phoneNumberEdit.setText( myProfile.getPhoneNumber() );
                ownerAddressEdit.setText( myProfile.getOwnerAddress() );
                ownerPhoneNumberEdit.setText( myProfile.getOwnerPhoneNumber() );
                insurancePolicyNumberEdit.setText( myProfile.getInsurancePolicyNumber() );
                insuranceCompanyNameEdit.setText( myProfile.getInsuranceCompanyName() );
                insuranceAgentNameEdit.setText( myProfile.getInsuranceAgentName() );
                insuranceAgentPhoneNumEdit.setText( myProfile.getInsuranceAgentPhoneNum() );
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}});

            choose.setOnClickListener( new View.OnClickListener() {
               @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
               @Override
               public void onClick(View v) {
                   chooseImage();
               }
            });
            save.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    uploadPhoto();
                    final Profile user = new Profile( userName, password, mail, firstNameEdit.getText().toString(), lastNameEdit.getText().toString(), carNumberEdit.getText().toString(),
                            carModelEdit.getText().toString(), carColorEdit.getText().toString(), driverNameEdit.getText().toString(), idEdit.getText().toString(),
                            addressEdit.getText().toString(), licenceNumberEdit.getText().toString(), phoneNumberEdit.getText().toString(), ownerAddressEdit.getText().toString(),
                            ownerPhoneNumberEdit.getText().toString(), insurancePolicyNumberEdit.getText().toString(), insuranceCompanyNameEdit.getText().toString(),
                            insuranceAgentNameEdit.getText().toString(), insuranceAgentPhoneNumEdit.getText().toString() );
                    users.child( userName ).setValue( user );
                    Intent intent = new Intent (EditProfile.this, menu.class);
                    intent.putExtra("name", user.getFullName());
                }
            } );
            edit.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                editMode( editTextsArr,true );
                save.setVisibility(View.VISIBLE);
                choose.setVisibility(View.VISIBLE);
                edit.setVisibility(View.GONE);
                }
                });
    }
    private void editMode(EditText[] arr,boolean visibilityFlag){
            for(int i = 0;i < arr.length -1 ;i++){
                arr[i].setEnabled( visibilityFlag );
                arr[i].setCursorVisible( visibilityFlag );
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    private void chooseImage(){
        Intent intent = new Intent();
        intent.setType( "image/*" );
        intent.setAction( Intent.ACTION_GET_CONTENT );
        startActivityForResult( Intent.createChooser( intent,"SelectPicture"),PICK_IMAGE_REQUEST );
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult( requestCode,resultCode,data );
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null)
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap( getContentResolver(),filePath );
                profilePicture.setImageBitmap( bitmap );
            }
             catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void uploadPhoto(){
        if(filePath != null){
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle( "Uploading..." );
            progressDialog.show();

            final StorageReference ref = photos.child("images/" + UUID.randomUUID().toString() );
            ref.putFile(filePath).addOnSuccessListener( new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                    Toast.makeText( EditProfile.this, "Upload Success", Toast.LENGTH_SHORT ).show();
//                    StorageMetadata snapshotMetadata = taskSnapshot.getMetadata();
//                    Task<Uri> downloadUrl  = ref.getDownloadUrl();
//                    downloadUrl.addOnSuccessListener( new OnSuccessListener() {
//                        @Override
//                        public void onSuccess(Object uri) {
//                            String imageReference = uri.toString();
//                            users.child( userName ).child( "imageUrl" ).setValue( imageReference );
//                        }
//                    } );
                }
            } )
            .addOnFailureListener( new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText( EditProfile.this, "Failed "+e.getMessage() , Toast.LENGTH_SHORT).show();
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

}
