package com.example.car;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.car.Model.Profile;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity {
    private EditText editUser,editPass;

    //Firebase
    FirebaseDatabase db;
    DatabaseReference users;

    //Shared Pref and json
    private MySharedPreferences pref;
    private String json;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestPermission();//location permission

        TextView regBTN = findViewById(R.id.regBTN);
        Button logBTN = findViewById(R.id.logBTN);
        editUser = findViewById(R.id.editUser);
        editPass = findViewById(R.id.editPassword);

        //Firebase
        db = FirebaseDatabase.getInstance();
        users = db.getReference(Constants.FIRE_BASE_DB_PROFILES_PATH);

        pref = new MySharedPreferences(this);

        regBTN.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,RegisterPage.class));
                finish();
            }
        });
        logBTN.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!editUser.getText().toString().isEmpty()) {
                    MyFirebase.getUser(new CallBackUsersReady() {
                        @Override
                        public void userReady(Profile user) {
                            assert user != null;
                            if (user.getPassword().equals(editPass.getText().toString())) {
                                Toast.makeText(MainActivity.this, "Login Successfully!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(MainActivity.this, Menu.class);
                                saveData(user);
                                startActivity(intent);
                                finish();
                            }
                                else
                                 Toast.makeText(MainActivity.this, "Password Is Wrong", Toast.LENGTH_SHORT).show();
                            }

                        @Override
                        public void error() {
                            Toast.makeText(MainActivity.this, "Having trouble finding your account..", Toast.LENGTH_SHORT).show();
                        }
                    }, editUser.getText().toString());
                }
                else
                    Toast.makeText(MainActivity.this,"This user is not registered",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void saveData(Profile user) {
        json = new Gson().toJson(user);
        pref.putString(Constants.KEY_SHARED_PREF_PROFILE, json);
    }

    public void requestPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        Constants.MY_PERMISSIONS_REQUEST_FINE_LOCATION);
            }
        }
    }

}