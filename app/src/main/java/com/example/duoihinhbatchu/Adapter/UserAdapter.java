package com.example.duoihinhbatchu.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duoihinhbatchu.Module.User;
import com.example.duoihinhbatchu.R;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder>{
    private List<User> listUser;
    private Context context;

    public UserAdapter(List<User> listUser, Context context) {
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
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_user,parent,false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = listUser.get(position);
        holder.txtUsername.setText(user.getName());
        holder.txtScore.setText(String.valueOf(user.getScore()));
    }

    @Override
    public int getItemCount() {
        if (listUser== null){
            return 0;
        }else {
            return listUser.size();
        }

    }

    public class UserViewHolder extends RecyclerView.ViewHolder{

        private TextView txtUsername, txtScore;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            txtUsername = itemView.findViewById(R.id.txtUsername);
            txtScore = itemView.findViewById(R.id.txtScore);
        }
    }
}
