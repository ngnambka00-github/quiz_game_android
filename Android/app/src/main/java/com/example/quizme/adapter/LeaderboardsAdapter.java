package com.example.quizme.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.quizme.R;
import com.example.quizme.databinding.RowLeaderboardsBinding;
import com.example.quizme.models.User;
import com.example.quizme.utils.APIUtils;

import java.util.ArrayList;

public class LeaderboardsAdapter extends RecyclerView.Adapter<LeaderboardsAdapter.LeaderboardViewHolder> {

    Context context;
    ArrayList<User> users;

    public LeaderboardsAdapter(Context context, ArrayList<User> users) {
        this.context = context;
        this.users = users;
    }

    @NonNull
    @Override
    public LeaderboardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_leaderboards, parent, false);
        return new LeaderboardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LeaderboardViewHolder holder, int position) {
        User user = users.get(position);

        holder.binding.index.setText(String.format("#%d", position+1));
        holder.binding.name.setText(user.getName());
        holder.binding.coins.setText(String.valueOf(user.getCoins()));

        String defaultImagePath = "https://www.emmegi.co.uk/wp-content/uploads/2019/01/User-Icon.jpg";
        String avatarUser = user.getImagePath();
        if (avatarUser != null && !avatarUser.isEmpty()) {
            Glide.with(context)
                    .load(APIUtils.API_URL + "/static/" + avatarUser)
                    .into(holder.binding.imageView7);
        }
        else {
            Glide.with(context)
                    .load("https://www.emmegi.co.uk/wp-content/uploads/2019/01/User-Icon.jpg")
                    .into(holder.binding.imageView7);
        }

//        Glide.with(context)
//                .load(user.getProfile())
//                .into(holder.binding.imageView7);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class LeaderboardViewHolder extends RecyclerView.ViewHolder {

        RowLeaderboardsBinding binding;
        public LeaderboardViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = RowLeaderboardsBinding.bind(itemView);
        }
    }
}
