package com.example.car;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.car.Model.Accident;
import com.example.car.Model.Profile;
import com.google.gson.Gson;

public class AccidentAfterScanning extends AppCompatActivity {

    private Button btnOtherDriverInfo;
    private TextView date, time, location;
    private boolean isNewAccident;
    private MySharedPreferences pref;
    private String json;
    private Accident accident;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accident_after_scanning);

        btnOtherDriverInfo = findViewById(R.id.otherDriverInfoBtn);
        date = findViewById(R.id.dateTextView);
        location = findViewById(R.id.locationTextView);
        time = findViewById(R.id.timeTextView);

        isNewAccident =  getIntent().getBooleanExtra(Constants.INTENT_IS_NEW_ACCIDENT, true);
        pref = new MySharedPreferences(this);
        if(isNewAccident)//if it is a new accident
        {
            json = pref.getString(Constants.KEY_SHARED_PREF_NEW_ACCIDENT, "");
        }
        else
        {
            json = pref.getString(Constants.KEY_SHARED_FREF_EXIST_ACCIDENT, "");
        }
        accident = new Gson().fromJson(json, Accident.class);


                btnOtherDriverInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccidentAfterScanning.this, PopWindowUserInfo.class);
                intent.putExtra(Constants.INTENT_IS_NEW_ACCIDENT, isNewAccident);//is new accident or previous accident -> required to know which accident to present
                startActivity(intent);
                //finish();
            }
        });

    }
}
