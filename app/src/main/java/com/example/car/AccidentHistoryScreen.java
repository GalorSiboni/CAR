package com.example.car;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.car.Model.Accident;
import com.example.car.Model.Profile;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

import java.util.ArrayList;

public class AccidentHistoryScreen extends Fragment {
    private CallBackList callBackList;
    private ArrayList<Accident> accidentArrayList = new ArrayList<>();
    private ArrayList<Accident> accidentArrayListForThisUser;
    private Boolean isNewAccident = false;
    private ArrayAdapter<Accident> adapter;

    //Firebase
    FirebaseDatabase db;
    DatabaseReference accidents;

    //Shared Pref and json
    private MySharedPreferences pref;
    private String json;

    private Accident accident;
    private View view = null;
    //need to get by intent user profile including accident history

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.activity_accident_history_screen, container, false);
        }

        // TODO: 14/03/2020 load accidents from firebase
        db = FirebaseDatabase.getInstance();
        accidents = db.getReference(Constants.FIRE_BASE_ACCIDENT_PATH);
        pref = new MySharedPreferences(view.getContext());

        //getting the user profile info
        json = pref.getString(Constants.KEY_SHARED_PREF_PROFILE, "");
        Profile userProfile = new Gson().fromJson(json, Profile.class);


        accidents.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren())
                {
                    accident = dataSnapshot.getValue(Accident.class);
                    accidentArrayList.add(accident);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        accidents.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                accident = dataSnapshot.getValue(Accident.class);
                accidentArrayList.add(accident);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        accidentArrayListForThisUser = getAccidentArrayListForThisUser(accidentArrayList,userProfile);

        RecyclerView recyclerView = view.findViewById(R.id.rvAccidents);
        recyclerView.setLayoutManager(new LinearLayoutManager(inflater.getContext()));
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(accidentArrayListForThisUser, inflater.getContext());
        adapter.setClickListener(itemClickListener);
        recyclerView.setAdapter(adapter);

        return view;
    }

    public RecyclerViewAdapter.ItemClickListener itemClickListener = new RecyclerViewAdapter.ItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            // TODO: 14/03/2020 need to open accident after scanning with NO new accident
            //need somehow pass the specific accident id or something
//            double lat = accidentArrayList.get(position).getLocation().latitude;
//            double longi = accidentArrayList.get(position).getLocation().longitude;
//            setLocation(new LatLng(lat, longi));
        }
    };

    public void setCallback(CallBackList callback) {
        this.callBackList = callback;
    }

    public void setLocation(LatLng location) {
        callBackList.setMapLocation(location);
    }

    private ArrayList<Accident> getAccidentArrayListForThisUser(ArrayList<Accident> accidents, Profile user)
    {
        ArrayList<Accident> accidentsForUser = new ArrayList<>();
        for(int i=0; i<accidents.size(); i++)
        {
            if(accidents.get(i).getAccidentId().contains(user.getUsername()))
            {
                if(accidents.get(i) != null)
                    accidentsForUser.add(accidents.get(i));
                else
                    Log.d("AccidentHistoryxx", "Cannot resolve adding accident by user's accidents");
            }
        }
        return  accidentsForUser;
    }

}
