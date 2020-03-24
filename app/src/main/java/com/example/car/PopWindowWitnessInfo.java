package com.example.car;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.car.Model.Accident;
import com.example.car.Model.Profile;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class PopWindowWitnessInfo extends Activity {

//    private TextView phoneNumberEdit, addressEdit, driverNameEdit, idEdit, carNumberEdit, carModelEdit, carColorEdit,
//            licenceNumberEdit, ownerAddressEdit, ownerPhoneNumberEdit, insuranceCompanyNameEdit, insurancePolicyNumberEdit, insuranceAgentNameEdit, insuranceAgentPhoneNumEdit;
//    private CircleImageView profilePicture;
//    private ImageButton btn_close;
//    private TextView profileLbl;
//    private String mail;
//
//    //Shared Preferences
//    MySharedPreferences pref;
//    boolean isNewAccident;
//    String json;
//    Profile otherDriverProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_window_user_info);

//        setDisplayAsPop();
//        setViews();
//
//        isNewAccident =  getIntent().getBooleanExtra(Constants.INTENT_IS_NEW_ACCIDENT, true);
//        pref = new MySharedPreferences(this);
//
//        if(isNewAccident){//if it is a new accident
//            json = pref.getString(Constants.KEY_SHARED_PREF_NEW_ACCIDENT, "");
//        }
//        else {
//            json = pref.getString(Constants.KEY_SHARED_FREF_EXIST_ACCIDENT, "");
//        }
//
//        Accident accident = new Gson().fromJson(json, Accident.class);
//        String jsonProfile = pref.getString(Constants.KEY_SHARED_PREF_PROFILE, "");
//        Profile userProfile = new Gson().fromJson(jsonProfile, Profile.class);
//
//        //checks which one of the profile is the userName profile
//        if(accident.getDriverWhoGotScanned().getUsername().equals(userProfile.getUsername())) {
//            otherDriverProfile = new Gson().fromJson(json, Accident.class).getDriverThatScan();
//        }
//        else if(accident.getDriverThatScan().getUsername().equals(userProfile.getUsername())) {
//            otherDriverProfile = new Gson().fromJson(json, Accident.class).getDriverWhoGotScanned();
//        }
//
//        profileLbl.setText(String.format("%s %s", otherDriverProfile.getFirstName(), otherDriverProfile.getLastName()));
//
//        getTextFromFields();
//
//        Picasso.get().load(Uri.parse(otherDriverProfile.getImageUrl())).into(profilePicture);
//
//        btn_close.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });

    }

    private void setDisplayAsPop()
    {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width *.8), (int)(height *.6));

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = -20;

        getWindow().setAttributes(params);
    }

//    private void setViews()
//    {
//        profileLbl = findViewById(R.id.profileLabel);
//        btn_close = findViewById(R.id.closePop);
//        phoneNumberEdit = findViewById(R.id.phoneNumberEdit);
//        addressEdit = findViewById(R.id.addressEdit);
//        driverNameEdit = findViewById(R.id.driverNameEdit);
//        idEdit = findViewById(R.id.idEdit);
//        carNumberEdit = findViewById(R.id.carNumberEdit);
//        carModelEdit = findViewById(R.id.carModelEdit);
//        carColorEdit = findViewById(R.id.carColorEdit);
//        licenceNumberEdit = findViewById(R.id.licenceNumberEdit);
//        ownerAddressEdit = findViewById(R.id.ownerAddressEdit);
//        ownerPhoneNumberEdit = findViewById(R.id.ownerPhoneNumberEdit);
//        insuranceCompanyNameEdit = findViewById(R.id.insuranceCompanyNameEdit);
//        insurancePolicyNumberEdit = findViewById(R.id.insurancePolicyNumberEdit);
//        insuranceAgentNameEdit = findViewById(R.id.insuranceAgentNameEdit);
//        insuranceAgentPhoneNumEdit = findViewById(R.id.insuranceAgentPhoneNumEdit);
//        profilePicture = findViewById(R.id.profile_image);
//    }

//    private void getTextFromFields()
//    {
//        assert otherDriverProfile != null;
//        mail = otherDriverProfile.getMail();
//        carNumberEdit.setText(otherDriverProfile.getCarNumber());
//        carModelEdit.setText(otherDriverProfile.getCarModel());
//        carColorEdit.setText(otherDriverProfile.getCarColor());
//        driverNameEdit.setText(otherDriverProfile.getDriverName());
//        idEdit.setText(otherDriverProfile.getId());
//        addressEdit.setText(otherDriverProfile.getAddress());
//        licenceNumberEdit.setText(otherDriverProfile.getLicenceNumber());
//        phoneNumberEdit.setText(otherDriverProfile.getPhoneNumber());
//        ownerAddressEdit.setText(otherDriverProfile.getOwnerAddress());
//        ownerPhoneNumberEdit.setText(otherDriverProfile.getOwnerPhoneNumber());
//        insurancePolicyNumberEdit.setText(otherDriverProfile.getInsurancePolicyNumber());
//        insuranceCompanyNameEdit.setText(otherDriverProfile.getInsuranceCompanyName());
//        insuranceAgentNameEdit.setText(otherDriverProfile.getInsuranceAgentName());
//        insuranceAgentPhoneNumEdit.setText(otherDriverProfile.getInsuranceAgentPhoneNum());
//    }
}
