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

import java.util.Date;

public class EditProfile extends AppCompatActivity {
    private EditText firstNameEdit, lastNameEdit, emailEdit, phoneNumberEdit, addressEdit, driverNameEdit, idEdit, carNumberEdit, carModelEdit, carColorEdit,
            licenceNumberEdit, ownerAddressEdit, ownerPhoneNumberEdit, insuranceCompanyNameEdit, insurancePolicyNumberEdit, insuranceAgentNameEdit, insuranceAgentPhoneNumEdit;
    private Button button2;
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
        emailEdit = findViewById( R.id.emailEdit );
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
        button2 = findViewById( R.id.button2 );
//        firstNameEdit.setFocusable( false );
//        firstNameEdit.setEnabled( false );
//        firstNameEdit.setCursorVisible( false );

        Intent intent = getIntent();
        userName = intent.getStringExtra( "userName" );
        //Firebase
        db = FirebaseDatabase.getInstance();
        users = db.getReference( "Profiles" );// TODO: 19/02/2020 need to change to const path!!!
        button2.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Profile user = new Profile( firstNameEdit.getText().toString(), lastNameEdit.getText().toString(), carNumberEdit.getText().toString(),
                        carModelEdit.getText().toString(), carModelEdit.getText().toString(), driverNameEdit.getText().toString(), idEdit.getText().toString(),
                        addressEdit.getText().toString(), licenceNumberEdit.getText().toString(), phoneNumberEdit.getText().toString(), ownerAddressEdit.getText().toString(),
                        ownerPhoneNumberEdit.getText().toString(), insurancePolicyNumberEdit.getText().toString(), insurancePolicyNumberEdit.getText().toString(),
                        insuranceAgentNameEdit.getText().toString(), insuranceAgentPhoneNumEdit.getText().toString() );
                users.child( userName ).setValue( user );
            }
        } );
    }
}
