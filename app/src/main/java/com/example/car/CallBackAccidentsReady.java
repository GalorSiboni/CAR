package com.example.car;

import com.example.car.Model.Accident;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public interface CallBackAccidentsReady {
    void accidentsReady(ArrayList<Accident> accidents);
}
