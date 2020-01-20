package com.example.car;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class menu extends AppCompatActivity {

    private Button showProfile, showAccidents, insuranceAgent, emergencyServices, talkWithClient;
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
        talkWithClient = findViewById(R.id.talkWithClient);

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
//                        finish();
                    }
                });
                showAccidents.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(menu.this, AccidentReport.class));
                        finish();
                    }
                });
                insuranceAgent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        startActivity(new Intent(menu.this,insuranceAgent.class));
//                        finish();
                    }
                });
                emergencyServices.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        startActivity(new Intent(menu.this,emergencyServices.class));
//                        finish();
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
//                        finish();
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
//                        finish();
                    }
                });
                break;
            }
        }
    }
}
