package com.example.car;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.car.Model.Profile;
import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class Menu extends AppCompatActivity {
    private ImageView qrCode;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        CardView scanQR, showAccidents, emergencyServices;


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        scanQR = findViewById(R.id.scanQRCode);
        showAccidents = findViewById(R.id.allAccidents);
        emergencyServices = findViewById(R.id.emergencyCall);
        qrCode = findViewById(R.id.qrCode);
        ImageView profile = findViewById(R.id.profileIcon);
        ImageView home = findViewById(R.id.homeIcon);
        ImageView logOut = findViewById(R.id.logOutIcon);
        TextView greeting = (TextView) findViewById(R.id.greeting);

        //SharedPreferences
        MySharedPreferences pref = new MySharedPreferences(this);
        String json = pref.getString(Constants.KEY_SHARED_PREF_PROFILE, "");
        Profile userProfile = new Gson().fromJson(json, Profile.class);
        userName = userProfile.getUsername();
        //   if (json.compareTo("") == 0) { // TODO: 12/03/2020  do we need to consider the case of json is empty?
//        }
//        else {
//            = new Gson().fromJson(json, PersonList.class);
//        }

        greeting.setText(String.format("%s%s", Constants.GREETING_STR, userProfile.getFullName()));

        //icons events
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Menu.this, EditProfile.class);
//                intent.putExtra(Constants.INTENT_USER_NAME, userName);
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
                Intent intent = new Intent(Menu.this, QrCodeScanner.class);
//                intent.putExtra(Constants.INTENT_USER_NAME, userName);
                startActivity(intent);
            }
        });

        showAccidents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Menu.this, AccidentReport.class);
//                intent.putExtra(Constants.INTENT_USER_NAME, userName);
                startActivity(intent);
            }
        });

        generateQrCode();
    }

    private void generateQrCode()
    {
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
            Log.d("Menu Exception QR", e.getMessage());
        }
    }
}
