package com.example.duoihinhbatchu.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SwitchCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duoihinhbatchu.Database.UserDB;
import com.example.duoihinhbatchu.Models.User;
import com.example.duoihinhbatchu.R;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private List<User> listUser;
    private Context context;
    private onItemLongClickListener mListener;

    public interface onItemLongClickListener {
        void onItemClick(int position, View v);
    }
    public void setOnItemLongClickListener(onItemLongClickListener listener) {
        mListener = listener;
    }
    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<User> list) {
        this.listUser = list;
        notifyDataSetChanged();
    }

    public UserAdapter(List<User> listUser, Context context) {
        this.listUser = listUser;
        this.context = context;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_user, parent, false);
        return new UserAdapter.UserViewHolder(view,mListener);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = listUser.get(position);
        UserDB userDB = new UserDB();
        holder.txScore.setText("Score: " + user.getScore());
        holder.txName.setText(user.getName());
        holder.isAdmin.setChecked(user.getIsAdmin() != 0);
        holder.isAdmin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    user.setIsAdmin(1);
                } else {
                    user.setIsAdmin(0);
                }
                userDB.setAdmin(user, context);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (listUser == null || listUser.isEmpty()) {
            return 0;
        } else return listUser.size();

    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        private TextView txName, txScore;
        private SwitchCompat isAdmin;


        public UserViewHolder(@NonNull View itemView, onItemLongClickListener listener) {
            super(itemView);
            txName = itemView.findViewById(R.id.tvName);
            txScore = itemView.findViewById(R.id.tvDiem);
            isAdmin = itemView.findViewById(R.id.switchAdmin);
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (listener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position,v);
                        }
                    }
                    return false;
                }
            });
        }
    }
}
