package com.example.car;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.car.Model.Accident;
import com.example.car.Model.Profile;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

import java.util.ArrayList;

public class MyFirebase {

    public static void getAccidents(final CallBackAccidentsReady callBackAccidentsReady) {
        final ArrayList<Accident> accidents = new ArrayList<>();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(Constants.FIRE_BASE_ACCIDENT_PATH);

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot == null)
                    callBackAccidentsReady.error();

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

    public static void getUser(final CallBackUsersReady callBackUsersReady, final String userName){
        final Profile[] user = new Profile[1];
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(Constants.FIRE_BASE_DB_PROFILES_PATH);

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot == null)
                    callBackUsersReady.error();
                else {
                    if (dataSnapshot.child(userName).exists()) {
                        Log.d("MyFirebaseXXX", dataSnapshot.child(userName).getKey());
                        user[0] = dataSnapshot.child(userName).getValue(Profile.class);
                    }
                    callBackUsersReady.userReady(user[0]);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public static String updateUserDetails(final Profile profile, final String userName, String firstName, String lastName, String mail, String carNumber, String carModel, String carColor, String driverName, String id, String address, String licenceNumber, String phoneNumber, String ownerAddress, String ownerPhoneNumber, String insuranceCompanyName, String insurancePolicyNumber, String insuranceAgentName, String insuranceAgentPhoneNum, String imageUrl) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference myRef = db.getReference( Constants.FIRE_BASE_DB_PROFILES_PATH);
        final DatabaseReference accidentsRef = db.getReference(Constants.FIRE_BASE_ACCIDENT_PATH);
        StorageReference storage = FirebaseStorage.getInstance().getReference().child(Constants.FIRE_BASE_STORAGE_PROFILE_IMAGE);

        profile.updateProfile(firstName, lastName, mail, carNumber, carModel, carColor, driverName, id, address, licenceNumber, phoneNumber, ownerAddress, ownerPhoneNumber, insuranceCompanyName, insurancePolicyNumber, insuranceAgentName, insuranceAgentPhoneNum, imageUrl);
        myRef.child(userName).setValue(profile);

        //update the profiles in the accidents as well
        MyFirebase.getAccidents(new CallBackAccidentsReady() {
            @Override
            public void accidentsReady(ArrayList<Accident> accidents) {
               for(int i=0; i<accidents.size(); i++) {
                    if(accidents.get(i).getAccidentId().contains(userName))
                    {
                        if(accidents.get(i).getDriverThatScan().getUsername().equals(userName))
                        {
                            accidentsRef.child(accidents.get(i).getAccidentId()).child("driverThatScan").setValue(profile);
                        }
                        else if(accidents.get(i).getDriverWhoGotScanned().getUsername().equals(userName))
                        {
                            accidentsRef.child(accidents.get(i).getAccidentId()).child("driverWhoGotScanned").setValue(profile);
                        }
                    }
               }
            }

            @Override
            public void error() {
            }
        });
        return new Gson().toJson(profile);
    }

}