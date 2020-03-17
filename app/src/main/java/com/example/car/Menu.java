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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.ArrayList;

public class Menu extends AppCompatActivity{
    private String userName, json;
    private TextView greeting;
    private  ImageView profile, logOut, qrCode;
    private  CardView scanQR, showAccidents, emergencyServices;
    private MySharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        getViews();

        //SharedPreferences
        pref = new MySharedPreferences(this);
        json = pref.getString(Constants.KEY_SHARED_PREF_PROFILE, "");
        Profile userProfile = new Gson().fromJson(json, Profile.class);
        userName = userProfile.getUsername();

        greeting.setText(String.format("%s%s", Constants.GREETING_STR, userProfile.getFullName()));

        //icons events
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Menu.this, EditProfile.class);
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
                startActivity(intent);
            }
        });

        showAccidents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Menu.this, AccidentHistoryScreen.class);
                startActivity(intent);
            }
        });

        generateQrCode();
        update();
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


    public void update() {
        MyFirebase.getAccidents(new CallBackAccidentsReady() {
            @Override
            public void accidentsReady(ArrayList<Accident> accidents) {
                for(int i=0; i< accidents.size(); i++){
                    if(accidents.get(i).getDriverWhoGotScanned().getUsername().equals(userName)) {
                        if(!accidents.get(i).isScannedUserOpened()) {
                            showAlertDialogNewAccident(accidents.get(i));
                        }
                    }
                }
            }

            @Override
            public void error() {
                Toast.makeText(Menu.this, "There is error loading the accidents", Toast.LENGTH_SHORT).show();
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
                saveNewStateForAccidentForScannedUser(true, accident);
                saveAccidentData(accident);
                Intent intent = new Intent(Menu.this, AccidentAfterScanning.class);
                intent.putExtra(Constants.INTENT_IS_NEW_ACCIDENT, true);//is new accident or previous accident -> required to know which accident to present
                startActivity(intent);
            }
        });
        alert.setNegativeButton("It's okay, i'll stay here", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                accident.setScannedUserOpened(false);
                saveNewStateForAccidentForScannedUser(false, accident);
                saveAccidentData(accident);
                Toast.makeText(Menu.this, "You can see your new accident later in the accidents history", Toast.LENGTH_SHORT).show();
            }
        });
        alert.show();
    }
    private void saveAccidentData(Accident accident) {
        String json1 = new Gson().toJson(accident);
        pref.putString(Constants.KEY_SHARED_PREF_NEW_ACCIDENT, json1);
    }

    private void saveNewStateForAccidentForScannedUser(Boolean isUserOpened, Accident accident) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference myRef = db.getReference(Constants.FIRE_BASE_ACCIDENT_PATH);
        myRef.child(accident.getAccidentId()).child(Constants.IS_SCANNED_USER_OPENED).setValue(isUserOpened);

    }

}
