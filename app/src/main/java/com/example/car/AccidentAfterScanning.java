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

    private TextView date, location;
    private boolean isNewAccident;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accident_after_scanning);

        Button btnOtherDriverInfo = findViewById(R.id.otherDriverInfoBtn);
        date = findViewById(R.id.dateTextView);
        location = findViewById(R.id.locationTextView);

        isNewAccident =  getIntent().getBooleanExtra(Constants.INTENT_IS_NEW_ACCIDENT, true);
        MySharedPreferences pref = new MySharedPreferences(this);
        String json;
        if(isNewAccident)//if it is a new accident
        {
            json = pref.getString(Constants.KEY_SHARED_PREF_NEW_ACCIDENT, "");
        }
        else
        {
            json = pref.getString(Constants.KEY_SHARED_FREF_EXIST_ACCIDENT, "");
        }
        Accident accident = new Gson().fromJson(json, Accident.class);

        date.setText(accident.getOpenDate());
        location.setText(accident.getLocationStr());

        // TODO: 14/03/2020 add new images and load current images to a gallery like form

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
