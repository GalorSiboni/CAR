package com.example.car;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class menu extends AppCompatActivity {

    private Button scanQR, showAccidents, emergencyServices;
    private String fullName,userName;
    private TextView greeting;
    private ImageView qrCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_menu );

        scanQR = findViewById( R.id.scanqr );
        showAccidents = findViewById( R.id.showAccident );
        emergencyServices = findViewById( R.id.emergency );
        greeting = findViewById( R.id.greeting );
        qrCode = findViewById( R.id.qrCode );


        Intent intent = getIntent();
        fullName = intent.getStringExtra( "name" );
        userName = intent.getStringExtra( "userName" );
        greeting.setText( "welcome :" + fullName );
        scanQR.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(menu.this,QrCodeScanner.class));
            }
        } );
        showAccidents.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (menu.this, EditProfile.class);
                intent.putExtra("userName", userName);// TODO
                startActivity(intent);
            }
        } );
        emergencyServices.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent( menu.this, EmergencyServices.class ) );
            }
        } );
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = qrCodeWriter.encode( userName, BarcodeFormat.QR_CODE, 200, 200, null );
            Bitmap bitmap = Bitmap.createBitmap( 200, 200, Bitmap.Config.RGB_565 );

            for (int x = 0; x < 200; x++) {
                for (int y = 0; y < 200; y++) {
                    bitmap.setPixel( x, y, bitMatrix.get( x, y ) ? Color.BLACK : Color.WHITE );
                }
            }
            qrCode.setImageBitmap( bitmap );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
