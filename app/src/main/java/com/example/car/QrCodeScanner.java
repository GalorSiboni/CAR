package com.example.car;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.car.Model.Accident;
import com.example.car.Model.Profile;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.zxing.Result;

import java.util.logging.Logger;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class QrCodeScanner extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;
    //Firebase
    FirebaseDatabase db;
    DatabaseReference users;
    DatabaseReference accidents;
    @Override
    public void onCreate(Bundle state) {
        //Firebase init
        db = FirebaseDatabase.getInstance();
        users = db.getReference( "Profiles" );// TODO: 19/02/2020 need to change to const path!!!
        accidents = db.getReference( "Accidents" );// TODO: 19/02/2020 need to change to const path!!!

        super.onCreate( state );
        // Programmatically initialize the scanner view
        mScannerView = new ZXingScannerView( this );
        // Set the scanner view as the content view
        setContentView( mScannerView );
    }

    @Override
    public void onResume() {
        super.onResume();
        // Register ourselves as a handler for scan results.
        mScannerView.setResultHandler( this );
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
            Intent intentLocation = getIntent();
            double latitude = intentLocation.getDoubleExtra("latitude", 0.0);
            double longitude = intentLocation.getDoubleExtra("longitude", 0.0);
            final String userName = intentLocation.getStringExtra("userName");
            final Accident newAccident = new Accident(latitude, longitude);
            users.addListenerForSingleValueEvent( new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Profile myProfile = dataSnapshot.child( userName ).getValue( Profile.class );
                    Profile driver1Profile = dataSnapshot.child( rawResult.getText() ).getValue( Profile.class );
                    newAccident.addToProfilesList( driver1Profile);
                    newAccident.addToProfilesList( myProfile);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            String key = accidents.push().getKey();
            accidents.child( key ).setValue( newAccident );
            Intent intent = new Intent(QrCodeScanner.this, AccidentReport.class);
            intent.putExtra("driver1", rawResult.getText());// TODO change name to const!
            startActivity(intent);
            finish();
        }

    }
}