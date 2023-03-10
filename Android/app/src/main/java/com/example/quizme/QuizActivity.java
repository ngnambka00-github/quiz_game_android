package com.example.quizme;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.media.MediaPlayer;
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
import com.example.quizme.models.PlaySound;
import com.example.quizme.models.Question;
import com.example.quizme.utils.APIUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
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
    private boolean isClick5050Help = false;
    private int index = 0;
    private int correctAnswers = 0;
    private PlaySound playSound;
    MediaPlayer coundown_sound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        playSound = new PlaySound(this);
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

        // Event sroll cho text (ph???n c??u h???i)
        binding.question.setMovementMethod(new ScrollingMovementMethod());

        // Event g???i s??? tr??? gi??p
        binding.callRelativeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playSound.playSoundSupport();
                String explanation = question.getExplanation();
                if (explanation == null || explanation.isEmpty()) {
                    Toast.makeText(QuizActivity.this, "Xin l???i c??u n??y kh??ng c?? g???i ?? !!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(QuizActivity.this, "G???i ??: " + explanation, Toast.LENGTH_LONG).show();
                }
            }
        });

        // Event 50:50
        binding.help5050Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playSound.playSound5050();
                isClick5050Help = true;
                handler5050Help();
            }
        });

        // Event quiz click
        binding.quizBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                coundown_sound.stop();
                startActivity(new Intent(QuizActivity.this, MainActivity.class));
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        coundown_sound.stop();
    }

    void resetTimer() {
        timer = new CountDownTimer(30000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                binding.timer.setText(String.valueOf(millisUntilFinished/1000));
            }

            @Override
            public void onFinish() {
                selectedAnswer = true;
                isClick5050Help = false;
                showAnswer();
                binding.option1.setClickable(false);
                binding.option2.setClickable(false);
                binding.option3.setClickable(false);
                binding.option4.setClickable(false);
                binding.help5050Btn.setClickable(false);
                coundown_sound.stop();
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
        coundown_sound = MediaPlayer.create(this, R.raw.ticktock);
        coundown_sound.start();
//        playSound.playSoundTimer();
        if(index < questions.size()) {
            binding.questionCounter.setText(String.format("%d/%d", (index+1), questions.size()));
            question = questions.get(index);
            binding.question.setText(question.getQuestion());
            binding.option1.setText(question.getOption1());
            binding.option2.setText(question.getOption2());
            binding.option3.setText(question.getOption3());
            binding.option4.setText(question.getOption4());

            // C???p nh???p image c???a c??u h???i (n???u c??)
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
        coundown_sound.stop();
        if(selectedAnswer.equals(question.getAnswer())) {
            correctAnswers++;
            playSound.playSoundCorrect();
            textView.setBackground(getResources().getDrawable(R.drawable.option_right));
        } else {
            showAnswer();
            playSound.playSoundFail();
            textView.setBackground(getResources().getDrawable(R.drawable.option_wrong));
        }
    }

    void reset() {
        binding.help5050Btn.setClickable(true);
        binding.option1.setClickable(true);
        binding.option2.setClickable(true);
        binding.option3.setClickable(true);
        binding.option4.setClickable(true);

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
                isClick5050Help = false;
                binding.help5050Btn.setClickable(false);
                binding.option1.setClickable(false);
                binding.option2.setClickable(false);
                binding.option3.setClickable(false);
                binding.option4.setClickable(false);

                TextView selected = (TextView) view;
                checkAnswer(selected);
                break;

            case R.id.nextBtn:
                if (isClick5050Help || !selectedAnswer){
                    Toast.makeText(QuizActivity.this, "H??y ch???n c??u tr??? l???i ??i !", Toast.LENGTH_SHORT).show();
                    break;
                }

                reset();
                // Check selected answers
                if(index < questions.size()) {
                    index++;
                    setNextQuestion();
                    selectedAnswer = false;
                }

                if (index == questions.size()) {
                    coundown_sound.stop();
                    playSound.playSoundResult();
                    Intent intent = new Intent(QuizActivity.this, ResultActivity.class);
                    intent.putExtra("correct", correctAnswers);
                    intent.putExtra("total", questions.size());
                    startActivity(intent);
                    //Toast.makeText(this, "Quiz Finished.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public void handler5050Help() {
        List<String> incorrectAnswers = new ArrayList<>();
        String correctAnswer = question.getAnswer();

        if (!question.getOption1().equals(correctAnswer)) { incorrectAnswers.add(question.getOption1()); }
        if (!question.getOption2().equals(correctAnswer)) { incorrectAnswers.add(question.getOption2()); }
        if (!question.getOption3().equals(correctAnswer)) { incorrectAnswers.add(question.getOption3()); }
        if (!question.getOption4().equals(correctAnswer)) { incorrectAnswers.add(question.getOption4()); }

        Random rand = new Random();
        List<String> newList = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            int randomIndex = rand.nextInt(incorrectAnswers.size());
            String item = incorrectAnswers.get(randomIndex);

            boolean check1 = false;
            for (String text : newList) {
                if (text.equals(item)) {
                    check1 = true;
                    break;
                }
            }
            if (!check1) { newList.add(incorrectAnswers.get(randomIndex)); }
            else{ i -= 1; }
        }

        for (String text : newList) {
            if (text.equals(binding.option1.getText().toString())) {
                binding.option1.setClickable(false);
                binding.option1.setBackground(getResources().getDrawable(R.drawable.option_wrong));
            }
            if (text.equals(binding.option2.getText().toString())) {
                binding.option2.setClickable(false);
                binding.option2.setBackground(getResources().getDrawable(R.drawable.option_wrong));
            }
            if (text.equals(binding.option3.getText().toString())) {
                binding.option3.setClickable(false);
                binding.option3.setBackground(getResources().getDrawable(R.drawable.option_wrong));
            }
            if (text.equals(binding.option4.getText().toString())) {
                binding.option4.setClickable(false);
                binding.option4.setBackground(getResources().getDrawable(R.drawable.option_wrong));
            }
        }
        binding.help5050Btn.setClickable(false);
        Toast.makeText(QuizActivity.this, "Tr??? gi??p 50:50", Toast.LENGTH_SHORT).show();
    }

}