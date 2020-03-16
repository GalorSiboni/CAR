package com.example.car;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.car.Model.Accident;
import com.example.car.Model.Profile;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;

public class AccidentHistoryScreen extends AppCompatActivity {
    private ArrayList<Accident> accidentArrayListForThisUser;
    private ImageView profileIcon, logOut;

    //Shared Pref and json
    private MySharedPreferences pref;
    private String json;

    private Profile userProfile;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accident_history_screen);

        profileIcon = findViewById(R.id.profileIcon);
        logOut = findViewById(R.id.logOutIcon);

        pref = new MySharedPreferences(this);

        //getting the user profile info
        json = pref.getString(Constants.KEY_SHARED_PREF_PROFILE, "");
        userProfile = new Gson().fromJson(json, Profile.class);

        MyFirebase.getAccidents(new CallBackAccidentsReady() {
            @Override
            public void accidentsReady(ArrayList<Accident> accidents) {
                createAccidentsRecycler(accidents);
            }

            @Override
            public void error() {
                Toast.makeText(AccidentHistoryScreen.this, "You are a careful driver! there is no accidents", Toast.LENGTH_SHORT).show();
//                ArrayList<Accident> accidents = new ArrayList<>();
//                createAccidentsRecycler(accidents);//for now is empty
            }
        });

        profileIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccidentHistoryScreen.this, EditProfile.class);
                startActivity(intent);
            }
        });

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AccidentHistoryScreen.this, MainActivity.class));
                finish();
            }
        });
    }

    private void createAccidentsRecycler(ArrayList<Accident> accidents) {
        accidentArrayListForThisUser = getAccidentArrayListForThisUser(accidents, userProfile);
        recyclerView = findViewById(R.id.rvAccidents);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewAdapter = new RecyclerViewAdapter(accidentArrayListForThisUser, this);
        recyclerViewAdapter.setClickListener(itemClickListener);
        recyclerView.setAdapter(recyclerViewAdapter);
    }

    public RecyclerViewAdapter.ItemClickListener itemClickListener = new RecyclerViewAdapter.ItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            saveSingleAccidentToJson(accidentArrayListForThisUser.get(position));
            Intent intent = new Intent(AccidentHistoryScreen.this, AccidentAfterScanning.class);
            intent.putExtra(Constants.INTENT_IS_NEW_ACCIDENT, false);//is new accident or previous accident -> required to know which accident to present
            startActivity(intent);
        }
    };

    private ArrayList<Accident> getAccidentArrayListForThisUser(ArrayList<Accident> accidents, Profile user)
    {
        ArrayList<Accident> accidentsForUser = new ArrayList<>();
        for(int i=0; i<accidents.size(); i++)
        {
            Log.d("AccidentHistoryxx1", accidents.get(i).getAccidentId());
            if(accidents.get(i).getAccidentId().contains(user.getUsername()))
            {
                Log.d("AccidentHistoryxx", accidents.get(i).getAccidentId());
                if(accidents.get(i) != null)
                    accidentsForUser.add(accidents.get(i));
                else
                    Log.d("AccidentHistoryxx", "Cannot resolve adding accident by user's accidents");
            }
        }
        return  accidentsForUser;
    }

    private void saveSingleAccidentToJson(Accident accident)//existing accident
    {
        String json1 = new Gson().toJson(accident);
        pref.putString(Constants.KEY_SHARED_FREF_EXIST_ACCIDENT, json1);
    }

}
