package com.example.quizme.Service;

import com.example.quizme.models.MailObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface MailService {
    @POST("/send_mail")
    Call<Void> sendMail(@Body MailObject sendObject);
}
