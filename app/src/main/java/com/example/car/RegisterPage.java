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

import com.example.car.Model.Profile;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterPage extends AppCompatActivity {
    private EditText fName,sName,mail,pass,cPass,userName;
    private String s0 = "",s1 = "",s2 = "",s3 = "",s4 = "",s5 = "", fullName = "";// TODO: 12/03/2020 fix
    //Firebase
    FirebaseDatabase db;
    DatabaseReference users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        //Firebase
        db = FirebaseDatabase.getInstance();
        users = db.getReference(Constants.FIRE_BASE_DB_PROFILES_PATH);

        userName = findViewById(R.id.regUserName);
        fName = findViewById(R.id.FirstName);
        sName = findViewById(R.id.SecondName);
        mail = findViewById(R.id.Email);
        pass = findViewById(R.id.Password);
        cPass = findViewById(R.id.PasswordConfrim);
        Button submit = findViewById(R.id.regOnPageBTN);
        TextView login = findViewById(R.id.login);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                s0 += userName.getText().toString().trim();
                s1 += pass.getText().toString();
                s2 += fName.getText().toString().trim();
                s3 += sName.getText().toString().trim();

                if(!mail.getText().toString().trim().equals("") && mail.getText().toString().trim().contains(Constants.AT_SIGN)) {
                    s4 += mail.getText().toString().trim();
                    String[] str = s4.split(Constants.AT_SIGN);
                    s4 = str[0] + Constants.AT_SIGN + str[1].replace( ".", "_DOT_" );//email -> replace . to _dot_ because of firebase settings
                }

                s5 += cPass.getText().toString();//password check

                if(!s0.equals("") && !s1.equals("") && !s2.equals("") && !s3.equals("")){
                if(s1.equals(s5)){
                    final Profile user = new Profile(s0,s1,s2,s3,s4);

                    users.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.child(user.getUsername()).exists())
                                Toast.makeText(RegisterPage.this,"The Username Is Already Exist!",Toast.LENGTH_SHORT).show();// TODO: 11/03/2020
                            else if(dataSnapshot.child(user.getMail()).exists())
                                Toast.makeText(RegisterPage.this,"This Mail Is Already Exist!",Toast.LENGTH_SHORT).show();// TODO: 11/03/2020
                            else if(dataSnapshot.child(user.getFullName()).exists())
                                Toast.makeText(RegisterPage.this,"This Full Name Is Already Exist!",Toast.LENGTH_SHORT).show();// TODO: 11/03/2020
                            else{
                                users.child(user.getUsername()).setValue(user);
                                Toast.makeText(RegisterPage.this,"Register Success!",Toast.LENGTH_SHORT).show();// TODO: 11/03/2020
                                Intent intent = new Intent (RegisterPage.this, Menu.class);
                                intent.putExtra(Constants.INTENT_FULL_NAME, user.getFullName());
                                intent.putExtra(Constants.INTENT_FULL_NAME, user.getUsername());
                                startActivity(intent);
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                else {
                    Toast.makeText(RegisterPage.this,"Password Confirmation Failed!",Toast.LENGTH_SHORT).show();// TODO: 11/03/2020 change to const 
                }
            }
            else{
                    Toast.makeText(RegisterPage.this,"Please fill all the slots",Toast.LENGTH_SHORT).show();// TODO: 11/03/2020 change to const
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