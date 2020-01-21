package com.example.car;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.car.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private EditText editUser,editPass;
    private Button regBTN,logBTN;
    private String kindOfUser, fullName;

    //Firebase
    FirebaseDatabase db;
    DatabaseReference users;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        regBTN = findViewById(R.id.regBTN);
        logBTN = findViewById(R.id.logBTN);
        editUser = findViewById(R.id.editText);
        editPass = findViewById(R.id.editText2);

        //Firebase
        db = FirebaseDatabase.getInstance();
        users = db.getReference("Users");


        regBTN.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,RegisterPage.class));
                finish();
            }
        });
        logBTN.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                users.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.child(editUser.getText().toString()).exists()) {
                            if (!editUser.getText().toString().isEmpty()) {
                                User login = dataSnapshot.child(editUser.getText().toString()).getValue(User.class);
                                if (login.getPassword().equals(editPass.getText().toString())) {
                                    Toast.makeText(MainActivity.this, "Login Success!", Toast.LENGTH_SHORT).show();
                                    kindOfUser =  switchCase(editUser.getText().toString());
                                    Intent intent = new Intent (MainActivity.this, menu.class);
                                    intent.putExtra("kindOfUser", kindOfUser);
                                    intent.putExtra("name", fullName);
                                    startActivity(intent);
                                    finish();
                                     }
                                else
                                    Toast.makeText(MainActivity.this, "Password Is Wrong", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                            Toast.makeText(MainActivity.this,"Username Is Not Registered",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }

private String switchCase(String user) {
    String kind ="user";
    switch(user){
        case "LielT": {
            fullName = "Liel Titelbaum";
            break;
        }
        case "GalorSiboni": {
            fullName = "Galor Siboni";
            break;
        }
        case "Agent": {
            fullName = "Yossi Cohen";
            kind = "agent";
            break;
        }
        case "Witness": {
            fullName = "Dani Din";
            kind = "witness";
            break;
        }
        default:{
            fullName = " New User";
        }
    }
    return kind;
}
}