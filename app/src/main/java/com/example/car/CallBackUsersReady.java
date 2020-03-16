package com.example.car;

import com.example.car.Model.Profile;
import com.example.car.Model.User;
import java.util.ArrayList;

public interface CallBackUsersReady {
    void userReady(Profile user);//when user details are ready from fire base
    void error();
}
