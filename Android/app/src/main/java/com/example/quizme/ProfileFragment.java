package com.example.quizme;

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

import com.example.quizme.Service.UserService;
import com.example.quizme.databinding.FragmentProfileBinding;
import com.example.quizme.models.User;
import com.example.quizme.utils.APIUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

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

    public ProfileFragment() { }
    public ProfileFragment(User user) {
        this.loginUser = user;
    }
    public static final int PICK_IMAGE = 1;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userService = APIUtils.getUserService();
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

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && data != null) {
            MultipartBody.Builder multipartBodyBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
            Uri pickedImage = data.getData();

            // Let's read picked image path using content resolver
            String[] filePath = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContext().getContentResolver().query(pickedImage, filePath, null, null, null);
            cursor.moveToFirst();

            String imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);
//            converting Java bitmap to byte array
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            multipartBodyBuilder.addFormDataPart("image" + 1, loginUser.getEmail().toString() +  ".jpg", RequestBody.create(MediaType.parse("image/*jpg"), byteArray));
            RequestBody postBodyImage = multipartBodyBuilder.build();
            postRequest(APIUtils.API_URL, postBodyImage);
            
            cursor.close();
        }
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

        binding.profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, PICK_IMAGE);
            }
        });


//        return inflater.inflate(R.layout.fragment_profile, container, false);
        return binding.getRoot();
    }
}