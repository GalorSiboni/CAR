package com.example.car;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

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
    private CallBackAccidentsReady callBackList;
    private ArrayList<Accident> accidentArrayList = new ArrayList<>();
    private ArrayList<Accident> accidentArrayListForThisUser;
    private Boolean isNewAccident = false;

    //Firebase
    FirebaseDatabase db;
    DatabaseReference accidents;

    //Shared Pref and json
    private MySharedPreferences pref;
    private String json;

    private Accident accident;

    private Profile userProfile;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accident_history_screen);

        pref = new MySharedPreferences(this);

        //getting the user profile info
        json = pref.getString(Constants.KEY_SHARED_PREF_PROFILE, "");
        userProfile = new Gson().fromJson(json, Profile.class);
        // TODO: 15/03/2020 test, delete later
//        ArrayList<Accident> testAccidents = new ArrayList<>();
//        testAccidents.add(new Accident(userProfile,userProfile));

//        accidents.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for(DataSnapshot ds: dataSnapshot.getChildren())
//                {
//                    accident = dataSnapshot.getValue(Accident.class);
////                    Log.d("AccidentHistoryxx", accident.getAccidentId());
//                    accidentArrayList.add(accident);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

        MyFirebase.getAccidents(new CallBackAccidentsReady() {
            @Override
            public void accidentsReady(ArrayList<Accident> accidents) {
                createAccidentsRecycler(accidents);
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

//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        if (view == null) {
//            view = inflater.inflate(R.layout.activity_accident_history_screen, container, false);
//        }
//
//        // TODO: 14/03/2020 load accidents from firebase
//        db = FirebaseDatabase.getInstance();
//        accidents = db.getReference(Constants.FIRE_BASE_ACCIDENT_PATH);
//        pref = new MySharedPreferences(view.getContext());
//
//        //getting the user profile info
//        json = pref.getString(Constants.KEY_SHARED_PREF_PROFILE, "");
//        Profile userProfile = new Gson().fromJson(json, Profile.class);
//
//        accidentArrayListForThisUser = getAccidentArrayListForThisUser(accidentArrayList,userProfile);
//
//        RecyclerView recyclerView = view.findViewById(R.id.rvAccidents);
//        recyclerView.setLayoutManager(new LinearLayoutManager(inflater.getContext()));
//        RecyclerViewAdapter adapter = new RecyclerViewAdapter(accidentArrayListForThisUser, inflater.getContext());
//        adapter.setClickListener(itemClickListener);
//        recyclerView.setAdapter(adapter);
//
//        return view;
//    }

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

    public void setCallback(CallBackAccidentsReady callback) {
        this.callBackList = callback;
    }

//    public void setLocation(LatLng location) {
//        callBackList.setMapLocation(location);
//    }

    private ArrayList<Accident> getAccidentArrayListForThisUser(ArrayList<Accident> accidents, Profile user)
    {
        ArrayList<Accident> accidentsForUser = new ArrayList<>();
        for(int i=0; i<accidents.size(); i++)
        {
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

}
