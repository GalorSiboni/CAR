package com.example.car;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.car.Model.Profile;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class EditProfile extends AppCompatActivity {
    private EditText firstNameEdit, lastNameEdit, phoneNumberEdit, addressEdit, driverNameEdit, idEdit, carNumberEdit, carModelEdit, carColorEdit,
            licenceNumberEdit, ownerAddressEdit, ownerPhoneNumberEdit, insuranceCompanyNameEdit, insurancePolicyNumberEdit, insuranceAgentNameEdit, insuranceAgentPhoneNumEdit;
    private Button save,edit;
    private String userName;
    //Firebase
    FirebaseDatabase db;
    DatabaseReference users;

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
        licenceNumberEdit = findViewById( R.id.licenceNumberEdit );
        ownerAddressEdit = findViewById( R.id.ownerAddressEdit );
        ownerPhoneNumberEdit = findViewById( R.id.ownerPhoneNumberEdit );
        insuranceCompanyNameEdit = findViewById( R.id.insuranceCompanyNameEdit );
        insurancePolicyNumberEdit = findViewById( R.id.insurancePolicyNumberEdit );
        insuranceAgentNameEdit = findViewById( R.id.insuranceAgentNameEdit );
        insuranceAgentPhoneNumEdit = findViewById( R.id.insuranceAgentPhoneNumEdit );
        save = findViewById( R.id.Save );
        save.setVisibility( View.GONE );
        edit = findViewById( R.id.edit );
        final EditText[] editTextsArr = {firstNameEdit, lastNameEdit, phoneNumberEdit, addressEdit, driverNameEdit, idEdit, carNumberEdit, carModelEdit, carColorEdit,
                licenceNumberEdit, ownerAddressEdit, ownerPhoneNumberEdit, insuranceCompanyNameEdit, insurancePolicyNumberEdit, insuranceAgentNameEdit, insuranceAgentPhoneNumEdit};
        editMode( editTextsArr,true );

        Intent intent = getIntent();
        userName = intent.getStringExtra( "userName" );

        //Firebase
        db = FirebaseDatabase.getInstance();
        users = db.getReference( "Profiles" );// TODO: 19/02/2020 need to change to const path!!!
        users.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Profile myProfile = dataSnapshot.child( userName ).getValue( Profile.class );
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

            save.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Profile user = new Profile( firstNameEdit.getText().toString(), lastNameEdit.getText().toString(), carNumberEdit.getText().toString(),
                        carModelEdit.getText().toString(), carColorEdit.getText().toString(), driverNameEdit.getText().toString(), idEdit.getText().toString(),
                        addressEdit.getText().toString(), licenceNumberEdit.getText().toString(), phoneNumberEdit.getText().toString(), ownerAddressEdit.getText().toString(),
                        ownerPhoneNumberEdit.getText().toString(), insurancePolicyNumberEdit.getText().toString(), insuranceCompanyNameEdit.getText().toString(),
                        insuranceAgentNameEdit.getText().toString(), insuranceAgentPhoneNumEdit.getText().toString() );
                users.child( userName ).setValue( user );
                save.setVisibility( View.GONE );
            }
        } );
            edit.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                editMode( editTextsArr,false );
                save.setVisibility(View.VISIBLE);
                }
                });
    }
    private void editMode(EditText[] arr,boolean visibilityFlag){
        if (visibilityFlag)
            for(int i = 0;i < arr.length -1 ;i++){
                arr[i].setFocusable( false );
                arr[i].setEnabled( false );
                arr[i].setCursorVisible( false );
        }
        else{
            for(int i = 0;i < arr.length;i++){
                arr[i].setFocusable( true );
                arr[i].setEnabled( true );
                arr[i].setCursorVisible( true );
            }
        }
    }
}
