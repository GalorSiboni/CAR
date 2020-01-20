package com.example.car;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

public class menu extends AppCompatActivity {

    private Button showProfile;
    private Button showAccidents;
    private Button insuranceAgent;
    private Button emergencyServices;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        showProfile = findViewById(R.id.showProfile);
        showAccidents = findViewById(R.id.showAccident);
        insuranceAgent = findViewById(R.id.insuranceAgent);
        emergencyServices = findViewById(R.id.emergency);
    }
}
