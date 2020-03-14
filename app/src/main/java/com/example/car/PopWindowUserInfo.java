package com.example.car;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.car.Model.Profile;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class PopWindowUserInfo extends Activity {

    private EditText firstNameEdit, lastNameEdit, phoneNumberEdit, addressEdit, driverNameEdit, idEdit, carNumberEdit, carModelEdit, carColorEdit,
            licenceNumberEdit, ownerAddressEdit, ownerPhoneNumberEdit, insuranceCompanyNameEdit, insurancePolicyNumberEdit, insuranceAgentNameEdit, insuranceAgentPhoneNumEdit;
    private CircleImageView profilePicture;
    private ImageView choose;
    private Button save, edit;
    private String userName, mail, imageUrl = "";

    private Uri filePath;
    private StorageTask uploadTask;
    private final int PICK_IMAGE_REQUEST = 71;
    private boolean editPhotoFlag = false;

    //Firebase
    FirebaseDatabase db;
    StorageReference storage;
    DatabaseReference users;

    //Shared Preferences
    MySharedPreferences pref;
    String json;
    Profile otherDriverProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_window_user_info);

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
}
