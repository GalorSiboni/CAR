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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterPage extends AppCompatActivity {
    private Button submit;
    private EditText fName,sName,mail,pass,cPass,userName;
    private TextView login;
    private String s0,s1,s2,s3,s4,s5;
    //Firebase
    FirebaseDatabase db;
    DatabaseReference users;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);


        //Firebase
        db = FirebaseDatabase.getInstance();
        users = db.getReference("Users");

        userName = findViewById(R.id.regUserName);
        fName = findViewById(R.id.FirstName);
        sName = findViewById(R.id.SecondName);
        mail = findViewById(R.id.Email);
        pass = findViewById(R.id.Password);
        cPass = findViewById(R.id.PasswordConfrim);
        submit = findViewById(R.id.regOnPageBTN);
        login = findViewById(R.id.login);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                s0 = userName.getText().toString().trim();
                s1 = pass.getText().toString().trim();
                s2 = fName.getText().toString().trim();
                s3 = sName.getText().toString().trim();
                s4 = mail.getText().toString().trim();
                s5 = cPass.getText().toString().trim();
                if(s1.equals(s5)){
                    final User user = new User(s0,s1,s2,s3,s4);

                    users.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.child(user.getUsername()).exists())
                                Toast.makeText(RegisterPage.this,"The Username Is Already Exist!",Toast.LENGTH_SHORT).show();
                            else if(dataSnapshot.child(user.getMail()).exists())
                                Toast.makeText(RegisterPage.this,"This Mail Is Already Exist!",Toast.LENGTH_SHORT).show();
                            else{
                                users.child(user.getUsername()).setValue(user);
                                Toast.makeText(RegisterPage.this,"Register Success!",Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                else {
                    Toast.makeText(RegisterPage.this,"Password Confirmation Failed!",Toast.LENGTH_SHORT).show();
                }
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLoginPage();
            }
        } );
    }


    public void openLoginPage() {
        startActivity(new Intent(RegisterPage.this,MainActivity.class));
        finish();
    }
}