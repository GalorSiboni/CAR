package com.example.car;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
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

    //SharedPreferences
    private MySharedPreferences pref;
    private String json;

    //Location
    private FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    public void onCreate(Bundle state) {
        // Programmatically initialize the scanner view
        mScannerView = new ZXingScannerView(this);
        // Set the scanner view as the content view

        super.onCreate(state);
        setContentView(mScannerView);

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
            Log.d("QrCodexxx", otherDriverResult);
            otherDriverProfile = new Gson().fromJson(otherDriverResult, Profile.class);//converting profile info to gson

            newAccident = new Accident(myProfile, otherDriverProfile);
            newAccident.setLocation(new LatLng(latitude, longitude));

            newAccident.setLocationStr(saveLocationAsString(latitude, longitude));

            saveAccidentData();
            accidents.child(newAccident.getAccidentId()).setValue(newAccident);//adding to the fireBase
            Intent intent = new Intent(QrCodeScanner.this, AccidentReport.class);
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
                        Log.d("QrScannerLong", " " + longitude);
                        Log.d("QrScannerLat", " " + latitude);
//                        newAccident.setLocation(new LatLng(latitude,longitude));
//                        saveLocationAsString(latitude, longitude);
                    }
                }
            });
        }
    }



    private void saveAccidentData() {
        String json1 = new Gson().toJson(newAccident);
        pref.putString(Constants.KEY_SHARED_PREF_ACCIDENT, json1);
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
//                            String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName();

                if (knownName != null) {
                    locationStr = knownName + " ," + city + ", " + country;
                } else {
                    if (address != null)
                        locationStr = address + " ," + city + ", " + country;
                }
                Log.d("QrScanner", locationStr);
                return locationStr;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";//if not found
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constants.MY_PERMISSIONS_REQUEST_FINE_LOCATION) {// If request is cancelled, the result arrays are empty.
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
        }
    }
}