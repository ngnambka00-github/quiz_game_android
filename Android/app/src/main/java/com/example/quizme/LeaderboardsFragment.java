package com.example.quizme;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.quizme.Service.UserService;
import com.example.quizme.adapter.LeaderboardsAdapter;
import com.example.quizme.databinding.FragmentLeaderboardsBinding;
import com.example.quizme.models.User;
import com.example.quizme.utils.APIUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LeaderboardsFragment extends Fragment {
    UserService userService;

    public LeaderboardsFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    FragmentLeaderboardsBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentLeaderboardsBinding.inflate(inflater, container, false);
        userService = APIUtils.getUserService();

//        FirebaseFirestore database = FirebaseFirestore.getInstance();

        final ArrayList<User> users = new ArrayList<>();
        final LeaderboardsAdapter adapter = new LeaderboardsAdapter(getContext(), users);

        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Lấy danh sách user để update view
        Call<List<User>> call = userService.getUsers();
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful()){
                    List<User> listUsers = response.body();
                    for (User user : listUsers) {
                        users.add(user);
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.e("ERROR CALL: ", t.getMessage());
            }
        });

//        database.collection("users")
//                .orderBy("coins", Query.Direction.DESCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//            @Override
//            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                for(DocumentSnapshot snapshot : queryDocumentSnapshots) {
//                    User user = snapshot.toObject(User.class);
//                    users.add(user);
//                }
//                adapter.notifyDataSetChanged();
//            }
//        });

        return binding.getRoot();
    }
}