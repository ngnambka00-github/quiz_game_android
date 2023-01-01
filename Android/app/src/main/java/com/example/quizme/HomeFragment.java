package com.example.quizme;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.quizme.Service.CategoryService;
import com.example.quizme.Service.InviteService;
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

    CategoryService categoryService;
    InviteService inviteService;
    private String email;
    public HomeFragment() {
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
                List<CategoryModel> listCategories = response.body();
                for (CategoryModel category : listCategories) {
                    categories.add(category);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<CategoryModel>> call, Throwable t) {
                Log.e("ERROR CALL: ", t.getMessage());
            }
        });

//        database.collection("categories")
//                .addSnapshotListener(new EventListener<QuerySnapshot>() {
//                    @Override
//                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//                        categories.clear();
//                        for (DocumentSnapshot snapshot : value.getDocuments()) {
//                            CategoryModel model = snapshot.toObject(CategoryModel.class);
//                            model.setCategoryId(snapshot.getId());
//                            categories.add(model);
//                        }
//                        adapter.notifyDataSetChanged();
//                    }
//                });

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
        inviteService = APIUtils.getInviteService();
        binding.inviteFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = showAddItemDialog(HomeFragment.this);
                Call<Void> sendMail = inviteService.sendMail(email);
                sendMail.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            System.out.println(response.toString());
                            Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(getActivity(), "Mời bạn bè không thành công!!!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Log.e("ERROR CALL: ", t.getMessage());
                    }
                });
//                Toast.makeText(getActivity(), "Invite Friend! Chưa dev chức năng này", Toast.LENGTH_SHORT).show();
            }
        });

        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    private String showAddItemDialog(HomeFragment c) {
        final EditText taskEditText = new EditText(c.getTargetFragment().getActivity());
        AlertDialog dialog = new AlertDialog.Builder(c.getTargetFragment().getActivity())
                .setTitle("Invite Friend")
                .setMessage("Who do you want to invite?")
                .setView(taskEditText)
                .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        email = String.valueOf(taskEditText.getText());
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
        return email;
    }
}