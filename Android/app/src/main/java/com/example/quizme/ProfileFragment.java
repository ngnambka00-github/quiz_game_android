package com.example.quizme;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.quizme.Service.UserService;
import com.example.quizme.databinding.FragmentProfileBinding;
import com.example.quizme.models.User;
import com.example.quizme.utils.APIUtils;

import com.theartofdev.edmodo.cropper.CropImage;

import org.mindrot.jbcrypt.BCrypt;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.annotation.Nullable;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProfileFragment extends Fragment {

    private User loginUser = null;
    UserService userService = null;
    FragmentProfileBinding binding;
    private ProgressDialog dialog;
    String imageAvatarPath = null;
    private MainActivity parentActivity;

    public ProfileFragment() {
    }

    public ProfileFragment(User user) {
        this.loginUser = user;
    }

    public ProfileFragment(User user, MainActivity app) {
        this.loginUser = user;
        this.parentActivity = app;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userService = APIUtils.getUserService();

        dialog = new ProgressDialog(getContext());
        dialog.setMessage("Upload Image ...");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        binding.emailBox.setText(loginUser.getEmail());
        binding.txtFullname.setText("[" + String.valueOf(loginUser.getUserId()) + "] " + loginUser.getName());

        String avatarUser = loginUser.getImagePath();
        if (avatarUser != null && !avatarUser.isEmpty()) {
            System.out.println(avatarUser);
            Glide.with(getContext())
                    .load(APIUtils.API_URL + "/static/" + avatarUser)
                    .into(binding.profileImage);
        }

        binding.updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = binding.nameBox.getText().toString();
                String password = binding.passBox.getText().toString();

                if (!name.isEmpty() && !password.isEmpty()) {
                    String hash_pass = BCrypt.hashpw(loginUser.getEmail() + password, BCrypt.gensalt(12));
                    loginUser.setName(name);
                    loginUser.setPass(hash_pass);

                    if (imageAvatarPath != null) {
                        loginUser.setImagePath(imageAvatarPath);
                    }

                    Call<Void> call = userService.updateUser(loginUser);
                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.isSuccessful()) {
                                binding.txtFullname.setText("[" + String.valueOf(loginUser.getUserId()) + "] " + loginUser.getName());
                                Toast.makeText(getActivity(), "Cập nhập thông tin cá nhân thành công", Toast.LENGTH_SHORT).show();

                                // Update main logined user
                                parentActivity.updateUserLogin(loginUser);
                            } else {
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

        binding.profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity()
                        .start(getContext(), ProfileFragment.this);
            }
        });

        binding.updateLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProgressDialog dialog = new ProgressDialog(getContext());
                dialog.setMessage("Logging out...");
                dialog.show();
                Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Do something after 100ms
                        startActivity(new Intent(getContext(), LoginActivity.class));
                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
                        prefs.edit().putInt("userid", 0).commit();

                        dialog.dismiss();
                        getActivity().finish();
                    }
                }, 1000);
            }
        });

//        return inflater.inflate(R.layout.fragment_profile, container, false);
        return binding.getRoot();
    }

    void postRequest(String postUrl, RequestBody postBody) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(postUrl)
                .post(postBody)
                .build();
        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                dialog.dismiss();
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                String serverImagePath = response.body().string();
                imageAvatarPath = "avatar/" + serverImagePath;
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        dialog.show();
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && data != null) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == Activity.RESULT_OK) {

                try {
                    Uri imageUri = result.getUri();
                    InputStream imageStream = this.getActivity().getContentResolver().openInputStream(imageUri);
                    Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    binding.profileImage.setImageBitmap(selectedImage);

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    selectedImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] byteArray = stream.toByteArray();

                    String[] users = loginUser.getEmail().split("@");
                    MultipartBody.Builder multipartBodyBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
                    multipartBodyBuilder.addFormDataPart("image", users[0] + ".jpg", RequestBody.create(MediaType.parse("image/*jpg"), byteArray));
                    RequestBody postBodyImage = multipartBodyBuilder.build();
                    postRequest(APIUtils.API_URL + "/upload_image", postBodyImage);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    dialog.dismiss();
                    Toast.makeText(null, "Something went wrong", Toast.LENGTH_LONG).show();
                }


            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        } else {
            dialog.dismiss();
        }
    }
}