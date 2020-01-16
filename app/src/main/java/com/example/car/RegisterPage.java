package com.example.car;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class RegisterPage extends AppCompatActivity {
    private TextView registerBtn;
    private EditText fName,sName,mail,pass,cPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        fName = findViewById(R.id.FirstName);
        sName = findViewById(R.id.SecondName);
        mail = findViewById(R.id.Email);
        pass = findViewById(R.id.Password);
        cPass = findViewById(R.id.PasswordConfrim);
        registerBtn = findViewById(R.id.regOnPageBTN);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        openLoginPage();

    }

    public void openLoginPage() {
        startActivity(new Intent(this,MainActivity.class));
        finish();
    }
}
