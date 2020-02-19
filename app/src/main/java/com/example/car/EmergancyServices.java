package com.example.car;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class EmergancyServices extends AppCompatActivity {
    private ImageButton police,mada,firefighters;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergancy_services);
        police = findViewById(R.id.police);
        mada = findViewById(R.id.mada);
        firefighters = findViewById(R.id.firefighters);
        police.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_DIAL);
                String p = "tel:100";
                i.setData(Uri.parse(p));
                startActivity(i);
            }
        });
        mada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_DIAL);
                String p = "tel:101";
                i.setData(Uri.parse(p));
                startActivity(i);
            }
        });
        firefighters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_DIAL);
                String p = "tel:102";
                i.setData(Uri.parse(p));
                startActivity(i);
            }
        });
        }
}
