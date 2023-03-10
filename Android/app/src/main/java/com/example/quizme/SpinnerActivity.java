package com.example.quizme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.quizme.Service.UserService;
import com.example.quizme.SpinWheel.LuckyWheelView;
import com.example.quizme.SpinWheel.model.LuckyItem;
import com.example.quizme.databinding.ActivitySpinnerBinding;
import com.example.quizme.models.PlaySound;
import com.example.quizme.models.User;
import com.example.quizme.utils.APIUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SpinnerActivity extends AppCompatActivity {

    ActivitySpinnerBinding binding;
    private MyApplication myApplication = (MyApplication) this.getApplication();
    private User loginUser = null;
    private UserService userService;
    private MediaPlayer spinner_sound;
    private PlaySound playSound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySpinnerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        playSound = new PlaySound(this);
        loginUser = myApplication.getUserLogin();
        userService = APIUtils.getUserService();

        List<LuckyItem> data = new ArrayList<>();
        LuckyItem luckyItem1 = new LuckyItem(
                "5", "COINS",
                Color.parseColor("#eceff1"),
                Color.parseColor("#212121"));
        data.add(luckyItem1);

        LuckyItem luckyItem2 = new LuckyItem(
                "10", "COINS",
                Color.parseColor("#00cf00"),
                Color.parseColor("#ffffff"));
        data.add(luckyItem2);

        LuckyItem luckyItem3 = new LuckyItem(
                "15", "COINS",
                Color.parseColor("#eceff1"),
                Color.parseColor("#212121"));
        data.add(luckyItem3);

        LuckyItem luckyItem4 = new LuckyItem(
                "20", "COINS",
                Color.parseColor("#7f00d9"),
                Color.parseColor("#ffffff"));
        data.add(luckyItem4);

        LuckyItem luckyItem5 = new LuckyItem(
                "25", "COINS",
                Color.parseColor("#eceff1"),
                Color.parseColor("#212121"));
        data.add(luckyItem5);

        LuckyItem luckyItem6 = new LuckyItem(
                "30", "COINS",
                Color.parseColor("#dc0000"),
                Color.parseColor("#ffffff"));
        data.add(luckyItem6);

        LuckyItem luckyItem7 = new LuckyItem(
                "35", "COINS",
                Color.parseColor("#eceff1"),
                Color.parseColor("#212121"));
        data.add(luckyItem7);

        LuckyItem luckyItem8 = new LuckyItem(
                "0", "COINS",
                Color.parseColor("#008bff"),
                Color.parseColor("#ffffff"));
        data.add(luckyItem8);

        binding.wheelview.setData(data);
        binding.wheelview.setRound(5);

        binding.spinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinner_sound = MediaPlayer.create(getBaseContext(), R.raw.spinner);
                spinner_sound.start();
                Random r = new Random();
                int randomNumber = r.nextInt(8);

                binding.wheelview.startLuckyWheelWithTargetIndex(randomNumber);
            }
        });

        binding.wheelview.setLuckyRoundItemSelectedListener(new LuckyWheelView.LuckyRoundItemSelectedListener() {
            @Override
            public void LuckyRoundItemSelected(int index) {
                spinner_sound.stop();
                updateCash(index);
            }
        });
    }

    void updateCash(int index) {
        int cash = 0;
        switch (index) {
            case 0:
                cash = 5;
                break;
            case 1:
                cash = 10;
                break;
            case 2:
                cash = 15;
                break;
            case 3:
                cash = 20;
                break;
            case 4:
                cash = 25;
                break;
            case 5:
                cash = 30;
                break;
            case 6:
                cash = 35;
                break;
            case 7:
                cash = 0;
                break;
        }

        final int finalCash = cash;
        loginUser.setCoins(loginUser.getCoins() + cash);
        Call<Void> call = userService.updateUser(loginUser);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (finalCash == 0) {
                    Toast.makeText(SpinnerActivity.this,
                             "??en r???i. Ch??c may m???n l???n sau !",
                            Toast.LENGTH_SHORT).show();
                    playSound.playSoundFailSpinner();
                } else {
                    Toast.makeText(SpinnerActivity.this,
                            "Xin ch??c m???ng b???n ???? nh???n ???????c " + String.valueOf(finalCash) + " coins",
                            Toast.LENGTH_SHORT).show();
                    playSound.playSoundFinalSpinner();
                }
                myApplication.setUserLogin(loginUser);
                startActivity(new Intent(SpinnerActivity.this, MainActivity.class));
                finish();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(SpinnerActivity.this,
                        "Quay ???????c " + finalCash + " coin. Nh??ng l???i server r??i nha !!",
                        Toast.LENGTH_SHORT).show();
            }
        });

    }

}