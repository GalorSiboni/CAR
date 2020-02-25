package com.example.car;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.car.Model.Profile;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private EditText editUser,editPass;
    private Button regBTN,logBTN;
    private double latitude, longitude;
    private BroadcastReceiver broadcastReceiver;


    //Firebase
    FirebaseDatabase db;
    DatabaseReference users;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        regBTN = findViewById(R.id.regBTN);
        logBTN = findViewById(R.id.logBTN);
        editUser = findViewById(R.id.editText);
        editPass = findViewById(R.id.editText2);

        //Firebase
        db = FirebaseDatabase.getInstance();
        users = db.getReference("Profiles");// TODO: 19/02/2020 need to change to const path!!!

        startGpsService();
        if (broadcastReceiver == null) {
            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    latitude = (double) intent.getExtras().get("latitude");
                    longitude = (double) intent.getExtras().get("longitude");
                }
            };
        }
        registerReceiver(broadcastReceiver, new IntentFilter("location_update"));

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
                                Profile login = dataSnapshot.child(editUser.getText().toString()).getValue(Profile.class);
                                if (login.getPassword().equals(editPass.getText().toString())) {
                                    Toast.makeText(MainActivity.this, "Login Success!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent (MainActivity.this, Menu.class);
                                    Intent intentLocation = new Intent (MainActivity.this, QrCodeScanner.class);
                                    intent.putExtra("name", login.getFullName());
                                    intent.putExtra("userName", login.getUsername());// TODO
                                    intentLocation.putExtra("latitude", latitude);
                                    intentLocation.putExtra("longitude", longitude);

                                    startActivity(intent);
                                    finish();
                                     }
                                else
                                    Toast.makeText(MainActivity.this, "Password Is Wrong", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                            Toast.makeText(MainActivity.this,"Username Is Not Registered",Toast.LENGTH_SHORT).show();
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
        if (broadcastReceiver != null) {
            unregisterReceiver(broadcastReceiver);
        }
    }
    private void startGpsService() {
        if (!runtime_permissions()) {
            Intent i = new Intent(getApplicationContext(), GPS_service.class);
            startService(i);
        }

    }

    private boolean runtime_permissions() {
        if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 100);

            return true;
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult( requestCode, permissions, grantResults );
        if (requestCode == 100) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                startGpsService();
            } else {
                runtime_permissions();
            }
        }
    }
}