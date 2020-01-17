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
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pass.getText().toString() == cPass.getText().toString()){
                final User user = new User(userName.getText().toString(),pass.getText().toString(),fName.getText().toString(),sName.getText().toString(),mail.getText().toString());

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
                }}
        });
        openLoginPage();

    }

    public void openLoginPage() {
        startActivity(new Intent(this,MainActivity.class));
        finish();
    }
}
