package com.example.car;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class Menu extends AppCompatActivity {
    private String userName, fullName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        CardView scanQR, showAccidents, emergencyServices;
        ImageView qrCode, profile, home, logOut;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        scanQR = findViewById(R.id.scanQRCode);
        showAccidents = findViewById(R.id.allAccidents);
        emergencyServices = findViewById(R.id.emergencyCall);
        qrCode = findViewById(R.id.qrCode);
        profile = findViewById(R.id.profileIcon);
        home = findViewById(R.id.homeIcon);
        logOut = findViewById(R.id.logOutIcon);


        Intent intent = getIntent();
        fullName = intent.getStringExtra("name");//Todo change name to const!!!
        userName = intent.getStringExtra("userName");//Todo change name to const!!!
        TextView greeting = (TextView) findViewById(R.id.greeting);
        greeting.setText("welcome :" + fullName);

//
        //icons events
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Menu.this, EditProfile.class);
                intent.putExtra("userName", userName);// TODO change name to const!
                startActivity(intent);
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Menu.this, Menu.class));
            }
        });
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Menu.this, MainActivity.class));
                finish();
            }
        });

        //buttons events
        emergencyServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Menu.this, EmergencyServices.class));
            }
        });

        scanQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (Menu.this, QrCodeScanner.class);
                intent.putExtra("userName", userName);// TODO
                startActivity(intent);}
        });

        showAccidents.setOnClickListener( new View.OnClickListener() {
                        @Override
            public void onClick(View v) {
                Intent intent = new Intent (Menu.this, AccidentReport.class);
                intent.putExtra("userName", userName);// TODO
                startActivity(intent);
            }
        } );


        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = qrCodeWriter.encode(userName, BarcodeFormat.QR_CODE, 200, 200, null);
            Bitmap bitmap = Bitmap.createBitmap(200, 200, Bitmap.Config.RGB_565);

            for (int x = 0; x < 200; x++) {
                for (int y = 0; y < 200; y++) {
                    bitmap.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
            qrCode.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
