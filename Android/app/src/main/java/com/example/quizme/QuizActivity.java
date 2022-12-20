package com.example.quizme;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.example.quizme.Service.QuestionService;
import com.example.quizme.databinding.ActivityQuizBinding;
import com.example.quizme.models.Question;
import com.example.quizme.utils.APIUtils;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuizActivity extends AppCompatActivity {

    private ActivityQuizBinding binding;
    private QuestionService questionService;
    private ArrayList<Question> questions;
    private Question question;
    private CountDownTimer timer;

    private boolean selectedAnswer = false;
    private int index = 0;
    private int correctAnswers = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuizBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        questionService = APIUtils.getQuestionService();

        questions = new ArrayList<>();
        final int categoryID = Integer.parseInt(getIntent().getStringExtra("category_id"));

        Call<List<Question>> call = questionService.getQuestionsByCategoryAndLimited(categoryID, 5);
        call.enqueue(new Callback<List<Question>>() {
            @Override
            public void onResponse(Call<List<Question>> call, Response<List<Question>> response) {
                if(response.isSuccessful()) {
                    List<Question> listQuestion = response.body();
                    for (Question q : listQuestion) {
                        questions.add(q);
                    }
                    setNextQuestion();
                }
            }

            @Override
            public void onFailure(Call<List<Question>> call, Throwable t) {
                Log.e("ERROR CALL: ", t.getMessage());
            }
        });
        resetTimer();

        // Event sroll cho text (phần câu hỏi)
        binding.question.setMovementMethod(new ScrollingMovementMethod());

        // Event gọi sự trợ giúp
        binding.callRelativeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(QuizActivity.this, "Chưa triển khai tính năng này !!", Toast.LENGTH_SHORT).show();
            }
        });

        // Event 50:50
        binding.help5050Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(QuizActivity.this, "Trợ giúp 50:50", Toast.LENGTH_SHORT).show();
            }
        });

        // Event quiz click
        binding.quizBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(QuizActivity.this, MainActivity.class));
                finish();
            }
        });
    }

    void resetTimer() {
        timer = new CountDownTimer(30000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                binding.timer.setText(String.valueOf(millisUntilFinished/1000));
            }

            @Override
            public void onFinish() {

            }
        };
    }

    void showAnswer() {
        if(question.getAnswer().equals(binding.option1.getText().toString()))
            binding.option1.setBackground(getResources().getDrawable(R.drawable.option_right));
        else if(question.getAnswer().equals(binding.option2.getText().toString()))
            binding.option2.setBackground(getResources().getDrawable(R.drawable.option_right));
        else if(question.getAnswer().equals(binding.option3.getText().toString()))
            binding.option3.setBackground(getResources().getDrawable(R.drawable.option_right));
        else if(question.getAnswer().equals(binding.option4.getText().toString()))
            binding.option4.setBackground(getResources().getDrawable(R.drawable.option_right));
    }

    void setNextQuestion() {
        if(timer != null)
            timer.cancel();

        timer.start();
        if(index < questions.size()) {
            binding.questionCounter.setText(String.format("%d/%d", (index+1), questions.size()));
            question = questions.get(index);
            binding.question.setText(question.getQuestion());
            binding.option1.setText(question.getOption1());
            binding.option2.setText(question.getOption2());
            binding.option3.setText(question.getOption3());
            binding.option4.setText(question.getOption4());

            // Cập nhập image của câu hỏi (nếu có)
            if (question.getImageURL() == null || question.getImageURL().isEmpty()) {
                // Invisible image component
                binding.imageView13.setEnabled(false);
                binding.imageView13.setVisibility(View.INVISIBLE);
                binding.imageView13.getLayoutParams().height = 20;
                binding.imageView13.requestLayout();
            }
            else {
                // Visible image component
                binding.imageView13.setEnabled(true);
                binding.imageView13.setVisibility(View.VISIBLE);
                binding.imageView13.getLayoutParams().height = 750;
                binding.imageView13.requestLayout();

                Glide.with(this.getApplicationContext())
                        .load(question.getImageURL())
                        .into(binding.imageView13);
            }
        }
    }

    void checkAnswer(TextView textView) {
        String selectedAnswer = textView.getText().toString();
        if(selectedAnswer.equals(question.getAnswer())) {
            correctAnswers++;
            textView.setBackground(getResources().getDrawable(R.drawable.option_right));
        } else {
            showAnswer();
            textView.setBackground(getResources().getDrawable(R.drawable.option_wrong));
        }
    }

    void reset() {
        binding.option1.setBackground(getResources().getDrawable(R.drawable.option_unselected));
        binding.option2.setBackground(getResources().getDrawable(R.drawable.option_unselected));
        binding.option3.setBackground(getResources().getDrawable(R.drawable.option_unselected));
        binding.option4.setBackground(getResources().getDrawable(R.drawable.option_unselected));
    }

    public void onClick(View view) {
        switch (view.getId()){
            case R.id.option_1:
            case R.id.option_2:
            case R.id.option_3:
            case R.id.option_4:
                if(timer!=null)
                    timer.cancel();
                selectedAnswer = true;
                TextView selected = (TextView) view;
                checkAnswer(selected);

                break;
            case R.id.nextBtn:
                reset();
                // Check selected answers
                if (!selectedAnswer) {
                    Toast.makeText(QuizActivity.this, "Hãy chọn câu trả lời đi !", Toast.LENGTH_SHORT).show();
                }
                else if(index <= questions.size()) {
                    index++;
                    setNextQuestion();
                    selectedAnswer = false;
                } else {
                    Intent intent = new Intent(QuizActivity.this, ResultActivity.class);
                    intent.putExtra("correct", correctAnswers);
                    intent.putExtra("total", questions.size());
                    startActivity(intent);
                    //Toast.makeText(this, "Quiz Finished.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

}