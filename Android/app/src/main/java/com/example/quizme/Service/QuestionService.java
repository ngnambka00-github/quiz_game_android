package com.example.quizme.Service;

import com.example.quizme.models.Question;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface QuestionService {
    @GET("/question/{category_id}/{limited_number}")
    Call<List<Question>> getQuestionsByCategoryAndLimited(
            @Path("category_id") int categoryId,
            @Path("limited_number") int limitedNumber
    );
}
