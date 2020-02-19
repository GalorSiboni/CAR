package com.example.car;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class menu extends AppCompatActivity {

    private Button showProfile, showAccidents, emergencyServices;
    private String fullName;
    private TextView greeting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        showProfile = findViewById(R.id.showProfile);
        showAccidents = findViewById(R.id.showAccident);
        emergencyServices = findViewById(R.id.emergency);
        greeting = findViewById(R.id.greeting);

        Intent intent = getIntent();
        fullName = intent.getStringExtra("name");
        greeting.setText("welcome :"+ fullName);
        showProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                        startActivity(new Intent(menu.this,showProfile.class));
            }
        });
        showAccidents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(menu.this, AccidentReport.class));
            }
        });
        emergencyServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                        startActivity(new Intent(menu.this,emergencyServices.class));
            }
        });
    }
}
