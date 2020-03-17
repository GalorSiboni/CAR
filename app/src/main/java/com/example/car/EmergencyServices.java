package com.example.car;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

public class EmergencyServices extends AppCompatActivity {

    private ImageView profile, logOut;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergancy_services);
        CardView police = findViewById(R.id.police);
        CardView mada = findViewById(R.id.mada);
        CardView firefighters = findViewById(R.id.fireFighters);
        profile = findViewById(R.id.profileIcon);
        logOut = findViewById(R.id.logOutIcon);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EmergencyServices.this, EditProfile.class);
                startActivity(intent);
            }
        });

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EmergencyServices.this, MainActivity.class));
                finish();
            }
        });

        police.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_DIAL);
                String p = Constants.CALL_POLICE;
                i.setData(Uri.parse(p));
                startActivity(i);
            }
        });
        mada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_DIAL);
                String p = Constants.CALL_MADA;
                i.setData(Uri.parse(p));
                startActivity(i);
            }
        });
        firefighters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_DIAL);
                String p = Constants.CALL_FIRE;
                i.setData(Uri.parse(p));
                startActivity(i);
            }
        });


        }
}
