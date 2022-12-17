package com.example.quizme.datatest;

import com.example.quizme.models.User;

import java.util.Arrays;
import java.util.List;

public class UserData {
    public static List<User> users = Arrays.asList(
            new User("Nguyen Van Nam", "nam@gmail.com", "123", "123"),
            new User("Pham Thi Quynh", "quynh@gmail.com", "123", "123"),
            new User("Pham Thi Trang", "trang@gmail.com", "123", "123"),
            new User("Tran Quang Tuang", "tuan@gmail.com", "123", "123"),
            new User("Nguyen Viet Thi", "thi@gmail.com", "123", "123")
    );
}
