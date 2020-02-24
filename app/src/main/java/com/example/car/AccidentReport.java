package com.example.car;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class AccidentReport extends AppCompatActivity {
    private String myUserName,accidentOpenerUserName;
    private boolean iScan;

    //Firebase
    FirebaseDatabase db;
    StorageReference storage;
    StorageReference accidentStorage;
    DatabaseReference users;
    DatabaseReference accidents;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accident_report);


        //Firebase init
        db = FirebaseDatabase.getInstance();
        users = db.getReference( "Profiles" );// TODO: 19/02/2020 need to change to const path!!!
        accidents = db.getReference( "Accidents" );// TODO: 19/02/2020 need to change to const path!!!
        storage = FirebaseStorage.getInstance().getReference().child( "ImageFolder" );
//        accidentStorage =  storage.child("accidents/" + accidentID);


        Intent intent = getIntent();
        myUserName = intent.getStringExtra("userName");//Todo change name to const!!!
        accidentOpenerUserName = intent.getStringExtra("accidentOpenerProfile");//Todo change name to const!!!
        iScan = intent.getBooleanExtra("whoScan",false);//Todo change name to const!!!
    }

}
