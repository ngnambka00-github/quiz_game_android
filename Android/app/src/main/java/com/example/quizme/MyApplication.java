package com.example.quizme;

import android.app.Application;

import com.example.quizme.models.User;

public class MyApplication extends Application {
    private static User userLogin = null;

    public MyApplication() { }

    public static User getUserLogin() {
        return userLogin;
    }

    public static void setUserLogin(User userLogin) {
        MyApplication.userLogin = userLogin;
    }
}
