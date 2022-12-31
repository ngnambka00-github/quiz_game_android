package com.example.quizme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;


import com.example.quizme.Service.UserService;
import com.example.quizme.databinding.ActivityResultBinding;
import com.example.quizme.models.User;
import com.example.quizme.utils.APIUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResultActivity extends AppCompatActivity {

    ActivityResultBinding binding;
    private MyApplication myApplication = (MyApplication) this.getApplication();
    private UserService userService;
    private User loginUser = null;

    int POINTS = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityResultBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        userService = APIUtils.getUserService();
        loginUser = myApplication.getUserLogin();

        int correctAnswers = getIntent().getIntExtra("correct", 0);
        int totalQuestions = getIntent().getIntExtra("total", 0);

        // Mỗi câu trả lời đúng sẽ nhận được POINT coin
        final long points = correctAnswers * POINTS;

        binding.score.setText(String.format("%d/%d", correctAnswers, totalQuestions));
        binding.earnedCoins.setText(String.valueOf(points));

        // Update coin cho user
        if (correctAnswers != 0) {
            loginUser.setCoins(loginUser.getCoins() + points);
            Call<Void> call = userService.updateUser(loginUser);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    Toast.makeText(ResultActivity.this,
                            "Xin chúc mừng bạn đã nhận được " + String.valueOf(points) + " coins",
                            Toast.LENGTH_SHORT).show();
                    myApplication.setUserLogin(loginUser);
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(ResultActivity.this,
                            "Lỗi server rùi. Không thể cập nhập kết quả",
                            Toast.LENGTH_SHORT).show();
                }
            });
        }

        binding.restartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ResultActivity.this, MainActivity.class));
                finishAffinity();
            }
        });

        binding.shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ResultActivity.this, "Chưa code tính năng này !!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ResultActivity.this, MainActivity.class));
        finishAffinity();
    }
}