package com.example.car;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;

import com.example.car.Model.Accident;
import com.example.car.Model.Profile;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class AccidentReport extends AppCompatActivity {
    private String myUserName,driver1, key;
    private Profile driver1Profile;
    private Accident accidentReport;

    //Firebase
    FirebaseDatabase db;
    StorageReference storage;
//    StorageReference accidentStorage;
    DatabaseReference users;
    DatabaseReference accidents;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accident_report);


        //Firebase init
        db = FirebaseDatabase.getInstance();
        users = db.getReference(Constants.FIRE_BASE_DB_PROFILES_PATH);
        accidents = db.getReference(Constants.FIRE_BASE_ACCIDENT_PATH);
        storage = FirebaseStorage.getInstance().getReference().child(Constants.FIRE_BASE_STORAGE_PROFILE_IMAGE);
//        accidentStorage =  storage.child("accidents/" + accidentID);

        Intent intent = getIntent();
        driver1 = intent.getStringExtra(Constants.DRIVER1_INTENT_DETAILS);
        myUserName = intent.getStringExtra(Constants.INTENT_USER_NAME);
        key = intent.getStringExtra(Constants.ACCIDENT_KEY_INTENT);
//        users.addListenerForSingleValueEvent( new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                driver1Profile = dataSnapshot.child( driver1 ).getValue( Profile.class );
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//        accidents.addListenerForSingleValueEvent( new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                accidentReport = dataSnapshot.child( key ).getValue( Accident.class );
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//        accidentReport.addToProfilesList( driver1Profile);
//        accidents.child( key ).setValue( accidentReport );
    }


}
