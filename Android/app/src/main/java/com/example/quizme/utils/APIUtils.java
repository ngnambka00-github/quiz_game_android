package com.example.quizme.utils;

import com.example.quizme.Service.CategoryService;
import com.example.quizme.Service.InviteService;
import com.example.quizme.Service.RetrofitClient;
import com.example.quizme.Service.UserService;

public class APIUtils {
    private APIUtils(){
    };

    public static final String API_URL = "http://192.168.1.14:5000";

    public static UserService getUserService(){
        return RetrofitClient.getClient(API_URL).create(UserService.class);
    }

    public static CategoryService getCategoryService() {
        return RetrofitClient.getClient(API_URL).create(CategoryService.class);
    }

    public static InviteService getInviteService() {
        return RetrofitClient.getClient(API_URL).create(InviteService.class);
    }
}
