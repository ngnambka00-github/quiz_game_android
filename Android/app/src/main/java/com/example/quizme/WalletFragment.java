package com.example.quizme;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.quizme.Service.MailService;
import com.example.quizme.Service.UserService;
import com.example.quizme.databinding.FragmentWalletBinding;
import com.example.quizme.models.MailObject;
import com.example.quizme.models.User;
import com.example.quizme.models.WithdrawRequest;
import com.example.quizme.utils.APIUtils;
import com.example.quizme.utils.MailUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WalletFragment extends Fragment {
    FragmentWalletBinding binding;

    private User loginedUser = null;
    MailService mailService = null;
    UserService userService = null;
    private MainActivity parentActivity = null;
    private ProgressDialog dialog;


    public WalletFragment() { }

    public WalletFragment(User user, MainActivity app) {
        this.loginedUser = user;
        this.parentActivity = app;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentWalletBinding.inflate(inflater, container, false);
        mailService = APIUtils.getMailService();
        userService = APIUtils.getUserService();

        if (loginedUser != null) {
            binding.currentCoins.setText(String.valueOf(loginedUser.getCoins()));
        }

        binding.sendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (loginedUser.getCoins() < 300) {
                    Toast.makeText(getContext(), "Lượng coin phải lớn hơn 300!", Toast.LENGTH_LONG).show();
                    return;
                }

                String toMail = binding.emailBox.getText().toString();
                // check mail is empty
                if (toMail.isEmpty()) {
                    Toast.makeText(getContext(), "Không được để chống email !", Toast.LENGTH_LONG).show();
                    binding.emailBox.requestFocus();
                    return;
                }

                // check mail if valid
                if (!MailUtils.checkMailValid(toMail)) {
                    Toast.makeText(getContext(), "Email không hợp lệ", Toast.LENGTH_LONG).show();
                    binding.emailBox.requestFocus();
                    return;
                }


                dialog = new ProgressDialog(getContext());
                dialog.setMessage("Chuyển coins ...");
                dialog.show();

                String bodyMessage = "Nhận " + loginedUser.getCoins() + " coins của " + loginedUser.getName() +" từ Quiz Game App";
                final MailObject mailObject = new MailObject(toMail, "Chuyển tiền thưởng Coin", bodyMessage);
                Call<Void> mailCall = mailService.sendMail(mailObject);
                mailCall.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            loginedUser.setCoins(1);
                            Call<Void> userCall = userService.updateUser(loginedUser);
                            userCall.enqueue(new Callback<Void>() {
                                @Override
                                public void onResponse(Call<Void> call, Response<Void> response) {
                                    if (response.isSuccessful()) {
                                        Toast.makeText(getActivity(), "Chuyển coins thành công !!", Toast.LENGTH_SHORT).show();
                                        binding.emailBox.setText("");
                                        binding.currentCoins.setText("0");
                                        parentActivity.updateUserLogin(loginedUser);

                                        dialog.dismiss();
                                    }
                                    else {
                                        Toast.makeText(getActivity(), "Chuyển coins thất bại rồi, Lỗi server !!", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    }
                                }

                                @Override
                                public void onFailure(Call<Void> call, Throwable t) {
                                    Log.e("ERROR CALL: ", t.getMessage());
                                    dialog.dismiss();
                                }
                            });
                        } else dialog.dismiss();
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Log.e("ERROR CALL: ", t.getMessage());
                        dialog.dismiss();
                    }
                });

            }
        });

        return binding.getRoot();
    }
}