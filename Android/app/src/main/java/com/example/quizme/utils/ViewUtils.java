package com.example.quizme.utils;

import android.content.Context;
import android.widget.EditText;
import android.widget.Toast;

import com.example.quizme.LoginActivity;

public class ViewUtils {
    public static void requestFocusView(EditText view, String messageLog, Context context) {
        String data = view.getText().toString();
        if (data.isEmpty()) {
            Toast.makeText(context, messageLog, Toast.LENGTH_SHORT).show();
            view.requestFocus();
        }
    }
}
