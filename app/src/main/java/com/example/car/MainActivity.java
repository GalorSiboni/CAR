package com.example.car;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.car.Model.Profile;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity {
    private EditText editUser,editPass;

    //Firebase
    FirebaseDatabase db;
    DatabaseReference users;

    //Shared Pref and json
    private MySharedPreferences pref;
    private String json;
    private Profile profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView regBTN = findViewById(R.id.regBTN);
        Button logBTN = findViewById(R.id.logBTN);
        editUser = findViewById(R.id.editUser);
        editPass = findViewById(R.id.editPassword);

        //Firebase
        db = FirebaseDatabase.getInstance();
        users = db.getReference(Constants.FIRE_BASE_DB_PROFILES_PATH);

        pref = new MySharedPreferences(this);
        //load data
//        profile = new Gson().fromJson(json, Profile.class);//converting profile info to gson


        regBTN.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,RegisterPage.class));
                finish();
            }
        });
        logBTN.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                users.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.child(editUser.getText().toString()).exists()) {
                            if (!editUser.getText().toString().isEmpty()) {
                                profile = dataSnapshot.child(editUser.getText().toString()).getValue(Profile.class);
                                assert profile != null;
                                if (profile.getPassword().equals(editPass.getText().toString())) {
                                    Toast.makeText(MainActivity.this, "Login Success!", Toast.LENGTH_SHORT).show();// TODO: 11/03/2020 change txt to const
                                    Intent intent = new Intent (MainActivity.this, Menu.class);
                                    saveData();
                                    Log.d("Mainxxx", json);
                                    startActivity(intent);
                                    finish();
                                }
                                else
                                    Toast.makeText(MainActivity.this, "Password Is Wrong", Toast.LENGTH_SHORT).show();// TODO: 11/03/2020 change txt to const
                            }
                        }
                        else
                            Toast.makeText(MainActivity.this,"Username Is Not Registered",Toast.LENGTH_SHORT).show();// TODO: 11/03/2020  change text to const
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void saveData()
    {
        json = new Gson().toJson(profile);
        pref.putString(Constants.KEY_SHARED_PREF_PROFILE, json);
    }
}