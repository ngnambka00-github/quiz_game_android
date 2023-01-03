package com.example.quizme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.quizme.Service.UserService;
import com.example.quizme.databinding.ActivitySignupBinding;
import com.example.quizme.models.User;
import com.example.quizme.utils.APIUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity {

    private ActivitySignupBinding binding;
    private ProgressDialog dialog;
    private UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        userService = APIUtils.getUserService();

        dialog = new ProgressDialog(this);
        dialog.setMessage("We're creating new account...");

        binding.createNewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email, pass, name, referCode;

                email = binding.emailBox.getText().toString();
                pass = binding.passwordBox.getText().toString();
                name = binding.nameBox.getText().toString();
                referCode = binding.referBox.getText().toString();

                if (name.isEmpty()) {
                    binding.nameBox.requestFocus();
                    Toast.makeText(SignupActivity.this, "Yêu cầu phải nhập họ tên", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (email.isEmpty()) {
                    binding.emailBox.requestFocus();
                    Toast.makeText(SignupActivity.this, "Yêu cầu phải nhập email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (pass.isEmpty()) {
                    binding.passwordBox.requestFocus();
                    Toast.makeText(SignupActivity.this, "Yêu cầu phải nhập password", Toast.LENGTH_SHORT).show();
                    return;
                }

                dialog.show();
                final User user = new User(name, email, pass, referCode);

                // Check existed email and add new user
                Call<List<User>> callListUser = userService.getUsers();
                callListUser.enqueue(new Callback<List<User>>() {
                    @Override
                    public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                        if (response.isSuccessful()) {
                            boolean isExist = false;
                            List<User> listUsers = response.body();
                            for (User u : listUsers) {
                                // Check tồn tại email
                                if (u.getEmail().equals(user.getEmail())) {
                                    isExist = true;
                                    break;
                                }
                            }

                            if (!isExist) {
                                // Nếu chưa tồn tại email
                                Call<Void> callAddUser = userService.addUser(user);
                                callAddUser.enqueue(new Callback<Void>() {
                                    @Override
                                    public void onResponse(Call<Void> call, Response<Void> response) {
                                        if (response.isSuccessful()) {
                                            Toast.makeText(SignupActivity.this, "Thêm tài khoản mới thành công. Vui lòng đăng nhập !!", Toast.LENGTH_SHORT).show();
                                            dialog.dismiss();
                                            startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                                            finish();
                                        } else { dialog.dismiss(); }
                                    }

                                    @Override
                                    public void onFailure(Call<Void> call, Throwable t) {
                                        dialog.dismiss();
                                        Log.e("ERROR CALL: ", t.getMessage());
                                    }
                                });
                            } else {
                                // Nếu đã tồn tại email
                                dialog.dismiss();
                                binding.emailBox.setText("");
                                binding.emailBox.requestFocus();
                                Toast.makeText(SignupActivity.this, "Email đã tồn tại vui lòng nhập email khác !!", Toast.LENGTH_SHORT).show();
                            }
                        } else { dialog.dismiss(); }
                    }


                    @Override
                    public void onFailure(Call<List<User>> call, Throwable t) {
                        dialog.dismiss();
                        Log.e("ERROR CALL: ", t.getMessage());
                    }
                });
            }
        });

        binding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
            }
        });

    }
}