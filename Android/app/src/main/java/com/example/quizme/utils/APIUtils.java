package com.example.quizme.utils;

import com.example.quizme.Service.CategoryService;
import com.example.quizme.Service.MailService;
import com.example.quizme.Service.QuestionService;
import com.example.quizme.Service.RetrofitClient;
import com.example.quizme.Service.UserService;

public class APIUtils {
    private APIUtils(){
    };

    public static final String API_URL = "http://192.168.98.34:5000";

    // get User Service to use GET, POST, ... API
    public static UserService getUserService(){
        return RetrofitClient.getClient(API_URL).create(UserService.class);
    }

    // get Category Service to use GET, POST, ... API
    public static CategoryService getCategoryService() {
        return RetrofitClient.getClient(API_URL).create(CategoryService.class);
    }

    // get Question Service to use GET, POST, ... API
    public static QuestionService getQuestionService() {
        return RetrofitClient.getClient(API_URL).create(QuestionService.class);
    }

    public static MailService getMailService() {
        return RetrofitClient.getClient(API_URL).create(MailService.class);
    }
}
