package com.example.car;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class menu extends AppCompatActivity {

    private Button showProfile, showAccidents, insuranceAgent, emergencyServices, talkWithClient;
    private String kindOfUser,fullName;
    private TextView greeting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        showProfile = findViewById(R.id.showProfile);
        showAccidents = findViewById(R.id.showAccident);
        insuranceAgent = findViewById(R.id.insuranceAgent);
        emergencyServices = findViewById(R.id.emergency);
        talkWithClient = findViewById(R.id.talkWithClient);
        greeting = findViewById(R.id.greeting);

        Intent intent = getIntent();
        kindOfUser = intent.getStringExtra("kindOfUser");
        fullName = intent.getStringExtra("name");
        greeting.setText("welcome "+ fullName);

        visabilitySwitchCase(kindOfUser);
    }

    private void visabilitySwitchCase(String kind) {
        switch (kind) {
            case "user": {
                talkWithClient.setVisibility(View.GONE);
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
                insuranceAgent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        startActivity(new Intent(menu.this,insuranceAgent.class));
                    }
                });
                emergencyServices.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        startActivity(new Intent(menu.this,emergencyServices.class));
                    }
                });
                break;
            }
            case "agent": {
                showProfile.setVisibility(View.GONE);
                showAccidents.setVisibility(View.GONE);
                emergencyServices.setVisibility(View.GONE);
                insuranceAgent.setVisibility(View.GONE);
                talkWithClient.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        startActivity(new Intent(menu.this,talkWithClient.class));
                    }
                });
                break;
            }
            case "witness": {
                showProfile.setVisibility(View.GONE);
                showAccidents.setVisibility(View.GONE);
                insuranceAgent.setVisibility(View.GONE);
                talkWithClient.setVisibility(View.GONE);
                emergencyServices.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        startActivity(new Intent(menu.this,emergencyServices.class));
                    }
                });
                break;
            }
        }
    }
}
