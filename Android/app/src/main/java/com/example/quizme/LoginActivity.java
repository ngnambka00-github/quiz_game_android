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
import com.example.quizme.databinding.ActivityLoginBinding;
import com.example.quizme.models.User;
import com.example.quizme.utils.APIUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;
    FirebaseAuth auth;
    ProgressDialog dialog;

    UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        userService = APIUtils.getUserService();

        dialog = new ProgressDialog(this);
        dialog.setMessage("Logging in...");

        // Nếu user đã đăng nhập sẵn rồi thì tự động vào MainActivity
//        if(auth.getCurrentUser() != null) {
//            startActivity(new Intent(LoginActivity.this, MainActivity.class));
//            finish();
//        }

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
                checkLogin(email, pass);

//                auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        dialog.dismiss();
//                        if(task.isSuccessful()) {
//                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
//                            finish();
//                        } else {
//                            Toast.makeText(LoginActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
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
                if(response.isSuccessful()){
                    List<User> listUsers = response.body();
                    for (User user : listUsers){
                        if (user.getPass().equals(password) && user.getEmail().equals(email)) {
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