package com.example.quizme;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.quizme.Service.MailService;
import com.example.quizme.models.MailObject;
import com.example.quizme.models.User;
import com.example.quizme.utils.APIUtils;
import com.example.quizme.utils.MailUtils;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BottomSheetDialog extends BottomSheetDialogFragment {

    private User loginedUser = null;
    private MailService mailService = null;
    private ProgressDialog dialog;

    public BottomSheetDialog() {
    }

    public BottomSheetDialog(User loginedUser) {
        this.loginedUser = loginedUser;
        mailService = APIUtils.getMailService();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable
            ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottom_sheet_layout, container, false);

        final TextView emailTxt = v.findViewById(R.id.emailBox);
        Button sendBtn = v.findViewById(R.id.sendBtn);

        dialog = new ProgressDialog(getContext());
        dialog.setMessage("Friend Invitation...");

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String toMail = emailTxt.getText().toString();
                // check mail is empty
                if (toMail.isEmpty()) {
                    Toast.makeText(getContext(), "Không được để chống email !", Toast.LENGTH_LONG).show();
                    return;
                }

                // check mail if valid
                if (!MailUtils.checkMailValid(toMail)) {
                    Toast.makeText(getContext(), "Email không hợp lệ", Toast.LENGTH_LONG).show();
                    return;
                }

                final MailObject mailObject = new MailObject(
                        toMail,
                        "QuizGame Invitation",
                        "Mình là " + loginedUser.getName() + "! Tham gia Quiz Game cùng mình nhé :3 :3");
                dialog.show();
                Call<Void> mailCall = mailService.sendMail(mailObject);
                mailCall.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(getContext(), "Mời tham gia thành công !!", Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                            dismiss();
                        } else dialog.dismiss();
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Log.e("ERROR CALL: ", t.getMessage());
                        dialog.dismiss();
                        dismiss();
                    }
                });
            }
        });


        return v;
    }
}