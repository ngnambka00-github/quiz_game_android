package com.example.quizme.utils;

import java.util.regex.Pattern;

public class MailUtils {
    public static boolean checkMailValid(String emailAddress) {
        String regexPattern = "^(.+)@(\\S+)$";
        return Pattern.compile(regexPattern)
                .matcher(emailAddress)
                .matches();
    }
}
