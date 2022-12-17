package com.example.quizme.utils;

import com.example.quizme.datatest.UserData;
import com.example.quizme.models.User;

public class UserUtils {
    public static boolean checkHasUser(String email) {
        for (User user : UserData.users) {
            if (user.getEmail().equals(email))
                return true;
        }
        return false;
    }

    public static User findUserByEmail(String email) {
        for (User user : UserData.users) {
            if (user.getEmail().equals(email))
                return user;
        }
        return null;
    }

    public static boolean checkLogin(String email, String password) {
        User user = findUserByEmail(email);
        if (user != null) {
            if (user.getPass().equals(password)) {
                return true;
            }
        }
        return false;
    }

    // Cần xử lý hàm add user ở đây
    public static boolean addUser(User newUser){
        return true;
//        if (checkHasUser(newUser.getEmail())) {
//            return false;
//        }
//        UserData.users.add(newUser);
//        System.out.println("[CHECK]");
//        return true;
    }
}
