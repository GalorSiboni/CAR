package com.example.car;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AccidentAfterScanning extends AppCompatActivity {

    private Button btnOtherDriverInfo;
    private TextView date, time, location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accident_after_scanning);

        btnOtherDriverInfo = findViewById(R.id.otherDriverInfoBtn);
        date = findViewById(R.id.dateTextView);
        location = findViewById(R.id.locationTextView);
        time = findViewById(R.id.timeTextView);

        btnOtherDriverInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
}
