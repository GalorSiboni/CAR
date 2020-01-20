package com.example.car;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class menu extends AppCompatActivity {

    private Button showProfile, showAccidents, insuranceAgent, emergencyServices;
    private String kindOfUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);


        Intent intent = getIntent();
        kindOfUser = intent.getStringExtra("kindOfUser");

        showProfile = findViewById(R.id.showProfile);
        showAccidents = findViewById(R.id.showAccident);
        insuranceAgent = findViewById(R.id.insuranceAgent);
        emergencyServices = findViewById(R.id.emergency);
    }
}
