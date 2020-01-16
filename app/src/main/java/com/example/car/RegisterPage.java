package com.example.car;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.car.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class RegisterPage extends AppCompatActivity {
    private Button submit;
    private EditText fName,sName,mail,pass,cPass,userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        userName = findViewById(R.id.regUserName);
        fName = findViewById(R.id.FirstName);
        sName = findViewById(R.id.SecondName);
        mail = findViewById(R.id.Email);
        pass = findViewById(R.id.Password);
        cPass = findViewById(R.id.PasswordConfrim);
        submit = findViewById(R.id.regOnPageBTN);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final User user = new User(userName.getText().toString(),pass.getText().toString(),fName.getText().toString(),sName.getText().toString(),mail.getText().toString());

                users.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.child(user.getUsername()).exists())
                            Toast.makeText(MainActivity.this,"The Username Is Already Exist!",Toast.LENGTH_SHORT).show();
                        else{
                            users.child(user.getUsername()).setValue(user);
                            Toast.makeText(MainActivity.this,"Register Success!",Toast.LENGTH_SHORT).show();
                            editUser.setText("");
                            editPass.setText("");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });
        openLoginPage();

    }

    public void openLoginPage() {
        startActivity(new Intent(this,MainActivity.class));
        finish();
    }
}
