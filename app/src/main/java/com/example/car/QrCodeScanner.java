package com.example.car;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

import com.example.car.Model.Accident;
import com.example.car.Model.Profile;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class QrCodeScanner extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;

    //Firebase
    FirebaseDatabase db;
    DatabaseReference users;
    DatabaseReference accidents;
    private Profile myProfile;//the driver who scan the barcode
    private Accident newAccident = new Accident();
    private Profile otherDriverProfile;
    private double latitude, longitude;
    private String userName;

    //Location
    private FusedLocationProviderClient fusedLocationProviderClient;
    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);//
        setContentView(mScannerView);

        requestPermission();

        //Firebase init
        db = FirebaseDatabase.getInstance();
        users = db.getReference(Constants.FIRE_BASE_DB_PROFILES_PATH);
        accidents = db.getReference(Constants.FIRE_BASE_ACCIDENT_PATH);

        // Programmatically initialize the scanner view
        mScannerView = new ZXingScannerView(this);
        // Set the scanner view as the content view

        //location
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getLastKnownLocation();

        Intent intentLocation = getIntent();//TODO location
//        latitude = intentLocation.getDoubleExtra("latitude", 0.0);//TODO location
//        longitude = intentLocation.getDoubleExtra("longitude", 0.0);//TODO location
        userName = intentLocation.getStringExtra(Constants.INTENT_USER_NAME);

        users.addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                myProfile = dataSnapshot.child(userName).getValue(Profile.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        // Register ourselves as a handler for scan results.
        mScannerView.setResultHandler(this);
        // Start camera on resume
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        // Stop camera on pause
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(final Result rawResult) {
        //onBackPressed();
        if (rawResult.getText() != null){
            newAccident.setDriver1(myProfile);
//            String key = accidents.push().getKey();
//            accidents.child(key).setValue( newAccident );
            users.child(userName).child(Constants.ACCIDENT_KEY_INTENT).setValue(newAccident.getAccidentId());
            Intent intent = new Intent(QrCodeScanner.this, AccidentReport.class);
//            intent.putExtra("driver1", rawResult.getText());// TODO: 12/03/2020 change name to const!
//            intent.putExtra("accidentKey", key);// TODO: 12/03/2020 change name to const!
            intent.putExtra(Constants.INTENT_USER_NAME, userName);
            startActivity(intent);
            finish();
        }

    }

    private void getLastKnownLocation() {
        //permissions check:
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    if (task.isSuccessful()) {
                        Location location = task.getResult();
                        assert location != null;
                        longitude = location.getLongitude();
                        latitude = location.getLatitude();

                        newAccident.setLocation(new LatLng(latitude,longitude));
                    }
                }
            });
        }
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
        } else {
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case Constants.MY_PERMISSIONS_REQUEST_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                longitude = location.getLongitude();
                                latitude = location.getLatitude();
                            }
                        }
                    });
                } else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }

}