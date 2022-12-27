package com.example.quizme;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

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
    private Uri image;
    private Bitmap bitmap;

    public ProfileFragment() { }
    public ProfileFragment(User user) {
        this.loginUser = user;
    }
    public static final int PICK_IMAGE = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userService = APIUtils.getUserService();
        String path = APIUtils.API_URL + "/static/avatar_image/" + loginUser.getUserId() + ".png";
//        System.out.println(path);
//        Glide.with(getContext()).load("https://cdn.sstatic.net/Img/teams/teams-illo-free-sidebar-promo.svg?v=47faa659a05e").into(binding.profileImage);
//        Picasso.get().load("http://i.imgur.com/DvpvklR.png").into(imageView);
//        Bitmap bm = getBitmapFromURL(path);

    }
    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
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

            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                if(response.isSuccessful()) {
//                    String path = APIUtils.API_URL + "/static/avatar_image/" + loginUser.getUserId() + ".png";
//                    Picasso.get().load(path).into(binding.profileImage);
                    binding.profileImage.setImageBitmap(bitmap);

                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == Activity.RESULT_OK) {
                MultipartBody.Builder multipartBodyBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
                Uri resultUri = result.getUri();
                image = resultUri;
                try {
                    bitmap =  MediaStore.Images.Media.getBitmap(this.getContext().getContentResolver(),  resultUri);
                    //Converting Java bitmap to byte array
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] byteArray = stream.toByteArray();
                    multipartBodyBuilder.addFormDataPart("image" + 1, new Integer(loginUser.getUserId()).toString() + ".png", RequestBody.create(MediaType.parse("image/*png"), byteArray));
                    RequestBody postBodyImage = multipartBodyBuilder.build();
                    postRequest(APIUtils.API_URL, postBodyImage);
//                    String path = APIUtils.API_URL + "/static/avatar_image/" + loginUser.getUserId() + ".png";
//                    Glide.with(getContext()).load(path).into(binding.profileImage);
//                    String path = "http://172.12.1.253:5000/static/avatar_image/b.jpg";
//                    Picasso.get().load(path).into(binding.profileImage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        binding.emailBox.setText(loginUser.getEmail());
        String path = APIUtils.API_URL + "/static/avatar_image/4.png";
        System.out.println(path);
        Glide.with(getContext()).load(path).into(binding.profileImage);
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

        binding.profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity()
                        .start(getContext(), ProfileFragment.this); // (DO NOT use `getActivity()`)
            }
        });

//        return inflater.inflate(R.layout.fragment_profile, container, false);
        return binding.getRoot();
    }
}