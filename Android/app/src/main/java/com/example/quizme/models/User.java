package com.example.quizme.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("fullname")
    @Expose
    private String name;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("password")
    @Expose
    private String pass;

    private String profile, referCode;

    @SerializedName("coin")
    @Expose
    private long coins = 25;

    public User() {
    }

    public User(String name, String email, String pass, String referCode) {
        this.name = name;
        this.email = email;
        this.pass = pass;
        this.referCode = referCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getReferCode() {
        return referCode;
    }

    public void setReferCode(String referCode) {
        this.referCode = referCode;
    }

    public long getCoins() {
        return coins;
    }

    public void setCoins(long coins) {
        this.coins = coins;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", pass='" + pass + '\'' +
                ", profile='" + profile + '\'' +
                ", referCode='" + referCode + '\'' +
                ", coins=" + coins +
                '}';
    }
}
