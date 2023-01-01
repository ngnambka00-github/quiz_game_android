package com.example.quizme.Service;

import com.example.quizme.models.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface InviteService {

    @POST("/sendMail")
    Call<Void> sendMail(@Body String email);
}
