package com.example.quizme;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.quizme.Service.CategoryService;
import com.example.quizme.adapter.CategoryAdapter;
import com.example.quizme.databinding.FragmentHomeBinding;
import com.example.quizme.models.CategoryModel;
import com.example.quizme.models.User;
import com.example.quizme.utils.APIUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragment extends Fragment {

    private CategoryService categoryService;
    private User loginedUser = null;

    public HomeFragment() {
    }

    public HomeFragment(User loginedUser) {
        this.loginedUser = loginedUser;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    FragmentHomeBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        categoryService = APIUtils.getCategoryService();

        final ArrayList<CategoryModel> categories = new ArrayList<>();
        final CategoryAdapter adapter = new CategoryAdapter(getContext(), categories);

        // View category
        Call<List<CategoryModel>> call = categoryService.getCategories();
        call.enqueue(new Callback<List<CategoryModel>>() {
            @Override
            public void onResponse(Call<List<CategoryModel>> call, Response<List<CategoryModel>> response) {
                if(response.isSuccessful()) {
                    List<CategoryModel> listCategories = response.body();
                    for (CategoryModel category : listCategories) {
                        categories.add(category);
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<CategoryModel>> call, Throwable t) {
                Log.e("ERROR CALL: ", t.getMessage());
            }
        });

        binding.categoryList.setLayoutManager(new GridLayoutManager(getContext(), 2));
        binding.categoryList.setAdapter(adapter);

        // SpinWheel Button
        binding.spinwheel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 startActivity(new Intent(getContext(), SpinnerActivity.class));
            }
        });

        // inviteFriend Button
        binding.inviteFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetDialog bottomSheet = new BottomSheetDialog(loginedUser);
                bottomSheet.show(getActivity().getSupportFragmentManager(), "ModalBottomSheet");
            }
        });

        // Inflate the layout for this fragment
        return binding.getRoot();
    }
}