package com.example.quizme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.quizme.Service.UserService;
import com.example.quizme.databinding.ActivityLoginBinding;
import com.example.quizme.models.User;
import com.example.quizme.utils.APIUtils;

import org.mindrot.jbcrypt.BCrypt;

import java.nio.charset.StandardCharsets;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private MyApplication myApplication = (MyApplication) this.getApplication();
    private ProgressDialog dialog;
    private UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        userService = APIUtils.getUserService();

        dialog = new ProgressDialog(this);
        dialog.setMessage("Logging in...");

        // Get Login information
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
        prefs = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
        final int userid = prefs.getInt("userid", 0);
        if (userid != 0) {
            dialog.show();

            Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Call<User> call = userService.getUserByID(userid);
                    call.enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            if (response.isSuccessful()) {
                                User user = response.body();
                                if (user != null) {
                                    myApplication.setUserLogin(user);
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                    dialog.dismiss();
                                    finish();
                                } else dialog.dismiss();
                            } else dialog.dismiss();
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            dialog.dismiss();
                            Log.e("ERROR CALL: ", t.getMessage());
                        }
                    });
                }
            }, 1000);
        }

        binding.submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email, pass;
                email = binding.emailBox.getText().toString();
                pass = binding.passwordBox.getText().toString();

                if (email.isEmpty()) {
                    binding.emailBox.requestFocus();
                    Toast.makeText(LoginActivity.this, "Yêu cầu phải nhập email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (pass.isEmpty()) {
                    binding.passwordBox.requestFocus();
                    Toast.makeText(LoginActivity.this, "Yêu cầu phải nhập password", Toast.LENGTH_SHORT).show();
                    return;
                }

                dialog.show();

                // Nếu login thành công sẽ chuyển sang MainActivity
                // Nếu login không thành công sẽ show Toast
                try {
                    checkLogin(email, pass);
                } catch (Exception ex) {
                    Toast.makeText(LoginActivity.this, "Server bị lỗi rồi. Check server đi !!", Toast.LENGTH_LONG).show();
                }
            }
        });

        binding.createNewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            }
        });
    }

    public void checkLogin(final String email, final String password) {
        Call<List<User>> call = userService.getUsers();
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    List<User> listUsers = response.body();
                    for (User user : listUsers) {
                        boolean check1 = user.getEmail().equals(email);
                        boolean check2 = BCrypt.checkpw(user.getEmail() + password, user.getPass());

                        if (check2 && check1) {
                            // Update global variable
                            myApplication.setUserLogin(user);

                            // Save login information
                            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                            prefs.edit().putInt("userid", user.getUserId()).commit();

                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                            return;
                        }
                    }

                    Toast.makeText(LoginActivity.this, "Tài khoản hoặc mật khẩu không chính xác", Toast.LENGTH_SHORT).show();
                    binding.emailBox.requestFocus();
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                dialog.dismiss();
                Log.e("ERROR CALL: ", t.getMessage());
            }
        });
    }
}