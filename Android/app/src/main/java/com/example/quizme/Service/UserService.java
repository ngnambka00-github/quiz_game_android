package com.example.quizme.Service;

import com.example.quizme.models.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UserService {
    @GET("/user")
    Call<List<User>> getUsers();

    @POST("/user")
    Call<Void> addUser(@Body User user);

    @PUT("/user")
    Call<User> updateUser(@Body User user);

    @DELETE("/user/{id}")
    Call<Void> deleteUser(@Path("id") int id);
}
