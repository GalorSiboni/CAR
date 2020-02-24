package com.example.car;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.zxing.Result;

import java.util.logging.Logger;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class QrCodeScanner extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;
    @Override
    public void onCreate(Bundle state) {
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
    public void handleResult(Result rawResult) {
        // Do something with the result here
        // Prints scan results
        //onBackPressed();
        if (rawResult != null){

            Intent intent = new Intent(QrCodeScanner.this, QrCodeScanner.class);
            intent.putExtra("accidentOpenerProfile", rawResult.getText());// TODO change name to const!
            intent.putExtra("whoScan", true);// TODO change name to const!
            startActivity(intent);
        }

    }
}