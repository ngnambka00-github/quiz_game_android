package com.example.quizme;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.quizme.Service.UserService;
import com.example.quizme.databinding.FragmentProfileBinding;
import com.example.quizme.models.User;
import com.example.quizme.utils.APIUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProfileFragment extends Fragment {

    private User loginUser = null;
    UserService userService = null;
    FragmentProfileBinding binding;

    public ProfileFragment() { }
    public ProfileFragment(User user) {
        this.loginUser = user;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userService = APIUtils.getUserService();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        binding.emailBox.setText(loginUser.getEmail());

        binding.updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = binding.nameBox.getText().toString();
                String password = binding.passBox.getText().toString();

                if (!name.isEmpty() && !password.isEmpty()) {
                    loginUser.setName(name);
                    loginUser.setPass(password);

                    Call<Void> call = userService.updateUser(loginUser);
                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(getActivity(), "Cập nhập thông tin cá nhân thành công", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(getActivity(), "Cập nhập thông tin cá nhân thất bại", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Log.e("ERROR CALL: ", t.getMessage());
                        }
                    });
                }
            }
        });

//        return inflater.inflate(R.layout.fragment_profile, container, false);
        return binding.getRoot();
    }
}