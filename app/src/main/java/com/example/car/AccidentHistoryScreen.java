package com.example.car;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.car.Model.Accident;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import java.util.ArrayList;

public class AccidentHistoryScreen extends Fragment {
    private CallBackList callBackList;
    private ArrayList<Accident> accidentArrayList;

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

        RecyclerView recyclerView = view.findViewById(R.id.rvPersons);
        recyclerView.setLayoutManager(new LinearLayoutManager(inflater.getContext()));
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(accidentArrayList, inflater.getContext());
        adapter.setClickListener(itemClickListener);
        recyclerView.setAdapter(adapter);

        return view;
    }

    public RecyclerViewAdapter.ItemClickListener itemClickListener = new RecyclerViewAdapter.ItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            double lat = accidentArrayList.get(position).getLocation().latitude;
            double longi = accidentArrayList.get(position).getLocation().longitude;
            setLocation(new LatLng(lat, longi));
        }
    };

    public void setCallback(CallBackList callback) {
        this.callBackList = callback;
    }

    public void setLocation(LatLng location) {
        callBackList.setMapLocation(location);
    }
}
