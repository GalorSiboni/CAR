package com.example.car;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.car.Model.Accident;
import com.example.car.Model.Profile;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.zxing.Result;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class QrCodeScanner extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private ZXingScannerView mScannerView;

    //Firebase
    FirebaseDatabase db;
    DatabaseReference users;
    DatabaseReference accidents;
    private Profile myProfile;//the driver who scan the barcode
    private Accident newAccident;
    private Profile otherDriverProfile;
    private double latitude, longitude;
    private String userName, locationStr;
    private boolean isNewAccident = true;

    //SharedPreferences
    private MySharedPreferences pref;
    private String json;

    //Location
    private FusedLocationProviderClient fusedLocationProviderClient;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        // Programmatically initialize the scanner view
        mScannerView = new ZXingScannerView(this);
        // Set the scanner view as the content view
        setContentView(mScannerView);

        this.requestPermissions(new String[]{Manifest.permission.CAMERA}, Constants.MY_PERMISSIONS_REQUEST_CAMERA);
        this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},Constants.MY_PERMISSIONS_REQUEST_FINE_LOCATION);

        //Firebase init
        db = FirebaseDatabase.getInstance();
        accidents = db.getReference(Constants.FIRE_BASE_ACCIDENT_PATH);

        pref = new MySharedPreferences(this);
        json = pref.getString(Constants.KEY_SHARED_PREF_PROFILE, "");
        myProfile = new Gson().fromJson(json, Profile.class);//my profile
        userName = myProfile.getUsername();

        //location
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getLastKnownLocation();
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
        onBackPressed();
        if (rawResult.getText() != null) {
            final String otherDriverResult = rawResult.getText();//other driver info in json
            otherDriverProfile = new Gson().fromJson(otherDriverResult, Profile.class);//converting profile info to gson
            newAccident = new Accident(myProfile, otherDriverProfile);
            newAccident.setLocationStr(saveLocationAsString(latitude, longitude));

            saveAccidentData();
            accidents.child(newAccident.getAccidentId()).setValue(newAccident);//adding to the fireBase
            Intent intent = new Intent(QrCodeScanner.this, AccidentAfterScanning.class);
            intent.putExtra(Constants.INTENT_IS_NEW_ACCIDENT, isNewAccident);//is new accident or previous accident -> required to know which accident to present
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
                    }
                }
            });
        }
    }

    private void saveAccidentData() {
        String json1 = new Gson().toJson(newAccident);
        pref.putString(Constants.KEY_SHARED_PREF_NEW_ACCIDENT, json1);
    }

    private String saveLocationAsString(double latitude, double longitude) {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(mScannerView.getContext(), Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            if (addresses != null) {
                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String knownName = addresses.get(0).getFeatureName();

                if (address != null)
                    locationStr = address;
                else if(knownName != null)
                    locationStr = knownName + ", " + city +", " + country;
                else
                    locationStr = state + ", " + city +", " + country;
                return locationStr;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "No location is found";//if not found
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case Constants.MY_PERMISSIONS_REQUEST_FINE_LOCATION: {// If request is cancelled, the result arrays are empty.
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
                    if (grantResults.length > 0)
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
            }
            case Constants.MY_PERMISSIONS_REQUEST_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // Here user granted the permission
                } else {
                    if(grantResults.length > 0)
                    // permission denied, boo! Disable the functionality that depends on this permission.
                    Toast.makeText(QrCodeScanner.this, "Permission denied to read your Camera", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }
}