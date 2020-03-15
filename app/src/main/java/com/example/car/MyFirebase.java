package com.example.car;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.car.Model.Accident;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyFirebase {

    public static void getAccidents(final CallBackAccidentsReady callBackAccidentsReady) {
        final ArrayList<Accident> accidents = new ArrayList<>();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(Constants.FIRE_BASE_ACCIDENT_PATH);

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                if (dataSnapshot == null)
//                    callBack_usersReady.error();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Accident accident = ds.getValue(Accident.class);
                    accidents.add(accident);
                }
                callBackAccidentsReady.accidentsReady(accidents);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}