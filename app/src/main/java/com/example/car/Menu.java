package com.example.car;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.car.Model.Accident;
import com.example.car.Model.Profile;
import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.ArrayList;

public class Menu extends AppCompatActivity implements NewAccidentObserver{
    private String userName, json;
    private TextView greeting;
    private  ImageView profile, logOut, qrCode;
    private  CardView scanQR, showAccidents, emergencyServices;
    private AccidentHistoryScreen accidentHistoryScreen;
    private MySharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        accidentHistoryScreen = new AccidentHistoryScreen();

        getViews();

        //SharedPreferences
        pref = new MySharedPreferences(this);
        json = pref.getString(Constants.KEY_SHARED_PREF_PROFILE, "");
        Profile userProfile = new Gson().fromJson(json, Profile.class);
        userName = userProfile.getUsername();
        Log.d("Mainxxx", json);
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
//                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//                transaction.add(R.id.LAYOUT_TO_INFLATE, accidentHistoryScreen);
//                transaction.show(accidentHistoryScreen);
//                transaction.commit();
                Intent intent = new Intent(Menu.this, AccidentHistoryScreen.class);
//                intent.putExtra(Constants.INTENT_IS_NEW_ACCIDENT, false);
                startActivity(intent);
            }
        });

        generateQrCode();
    }

    private void generateQrCode()
    {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = qrCodeWriter.encode(json, BarcodeFormat.QR_CODE, 200, 200, null);
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

    private void getViews()
    {
        scanQR = findViewById(R.id.scanQRCode);
        showAccidents = findViewById(R.id.allAccidents);
        emergencyServices = findViewById(R.id.emergencyCall);
        qrCode = findViewById(R.id.qrCode);
        profile = findViewById(R.id.profileIcon);
        logOut = findViewById(R.id.logOutIcon);
        greeting = findViewById(R.id.greeting);
    }

    @Override
    public void update() {
        MyFirebase.getAccidents(new CallBackAccidentsReady() {
            @Override
            public void accidentsReady(ArrayList<Accident> accidents) {
                for(int i=0; i< accidents.size(); i++){
                    if(accidents.get(i).getDriverWhoGotScanned().getUsername().equals(userName)) {
                        if(!accidents.get(i).isScannedUserOpened()) {
                            showAlertDialogNewAccident(accidents.get(i));
                            saveAccidentData(accidents.get(i));
                        }
                    }
                }
            }

            @Override
            public void error() {
                Toast.makeText(accidentHistoryScreen, "There is error loading the accidents", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void showAlertDialogNewAccident(final Accident accident)
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("New accident alert");
        alert.setMessage("There is a new accident involving you, do you want to see more details about it?");
        alert.setPositiveButton("Take me there", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                accident.setScannedUserOpened(true);//the user has opened the new accident
                Intent intent = new Intent(Menu.this, AccidentAfterScanning.class);
                intent.putExtra(Constants.INTENT_IS_NEW_ACCIDENT, true);//is new accident or previous accident -> required to know which accident to present
                startActivity(intent);
            }
        });
        alert.setNegativeButton("It's okay, i'll stay here", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                accident.setScannedUserOpened(true);//the user has not opened the accident but has not interest in it so we will change the status to true
                Toast.makeText(accidentHistoryScreen, "You can see your new accident later in the accidents history", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void saveAccidentData(Accident accident) {
        String json1 = new Gson().toJson(accident);
        pref.putString(Constants.KEY_SHARED_PREF_NEW_ACCIDENT, json1);
    }

}
