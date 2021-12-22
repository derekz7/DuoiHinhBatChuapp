package com.example.duoihinhbatchu.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duoihinhbatchu.Models.User;
import com.example.duoihinhbatchu.R;

import java.util.List;

public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.PlayerViewHolder>{
    private List<User> listUser;
    private Context context;

    public PlayerAdapter(List<User> listUser, Context context) {
        this.listUser = listUser;
        this.context = context;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<User> list){
        this.listUser = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PlayerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_player,parent,false);
        return new PlayerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayerAdapter.PlayerViewHolder holder, int position) {
        User user = listUser.get(position);
        holder.txtUsername.setText(user.getName());
        holder.txtScore.setText(String.valueOf(user.getScore()));
    }


    @Override
    public int getItemCount() {
        if (listUser == null || listUser.isEmpty()){
            return 0;
        }else {
            return listUser.size();
        }

    }

    public class PlayerViewHolder extends RecyclerView.ViewHolder{

        private TextView txtUsername, txtScore;

        public PlayerViewHolder(@NonNull View itemView) {
            super(itemView);
            txtUsername = itemView.findViewById(R.id.txtUsername);
            txtScore = itemView.findViewById(R.id.txtScore);
        }
    }
}
