package com.example.quizme.Service;

import com.example.quizme.models.CategoryModel;
import com.example.quizme.models.User;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface CategoryService {
    @GET("/category")
    Call<List<CategoryModel>> getCategories();

    @POST("/category")
    Call<Void> addCategory(@Body CategoryModel category);
}
