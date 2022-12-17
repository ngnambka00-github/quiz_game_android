package com.example.quizme;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.quizme.databinding.FragmentHomeBinding;
import com.example.quizme.models.CategoryModel;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class HomeFragment extends Fragment {


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    FragmentHomeBinding binding;
    FirebaseFirestore database;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        database = FirebaseFirestore.getInstance();

        ArrayList<CategoryModel> categories = new ArrayList<>();
        categories.add(new CategoryModel("Id1", "Math", "https://cdn.dribbble.com/users/2552641/screenshots/6549959/icon_challenge_originals_edu2_1x.jpg"));
        categories.add(new CategoryModel("Id2", "History", "https://cdn-icons-png.flaticon.com/512/2132/2132336.png"));
        categories.add(new CategoryModel("Id3", "English", "https://cleandye.com/wp-content/uploads/2020/01/English-icon.png"));
        CategoryAdapter adapter = new CategoryAdapter(getContext(), categories);
        adapter.notifyDataSetChanged();

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

        binding.spinwheel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), SpinnerActivity.class));
            }
        });

        // Inflate the layout for this fragment
        return binding.getRoot();
    }
}