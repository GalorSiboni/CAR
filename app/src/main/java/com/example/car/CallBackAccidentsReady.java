package com.example.car;

import com.example.car.Model.Accident;
import java.util.ArrayList;

public interface CallBackAccidentsReady {
    void accidentsReady(ArrayList<Accident> accidents);
    void error();
}
