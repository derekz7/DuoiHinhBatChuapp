package com.example.duoihinhbatchu.Database;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.duoihinhbatchu.Module.User;
import com.example.duoihinhbatchu.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserDB {
    private FirebaseDatabase database;
    private DatabaseReference userRef;
    private List<User> listUser;

    public UserDB() {
        database = FirebaseDatabase.getInstance();
        userRef = database.getReference("User");
        listUser = new ArrayList<>();
    }

    public void createUser(User user, Context context) {
        userRef.child(user.getName()).setValue(user, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                if (error == null) {
                    Toast.makeText(context, R.string.createusercomplete, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Error create new user", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public List<User> getDataUser() {
        listUser = new ArrayList<>();
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listUser.clear();
                for (DataSnapshot snap: snapshot.getChildren()){
                    User user = snap.getValue(User.class);
                    listUser.add(user);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return listUser;
    }

    public User getUser(String username, List<User> list){
        User user = new User();
        for (User user1: list){
            if (user1.getName().equals(username)){
               user = user1;
            }
        }
        return user;
    }

    public boolean checkUsername(List<User> list, String name){
        boolean kt = true;
        for (User u : list) {
            if (u.getName().equalsIgnoreCase(name) ){
                kt = false;
            }
        }
        return kt;
    }
    public boolean checkAdmin(List<User> list, String name){
        boolean kt = false;
        for (User u : list) {
            if (u.getName().equals(name) && u.getIsAdmin() == 1) {
                kt = true;
                break;
            }
        }
        return kt;
    }

    public void updateScore(String name, int score){
        userRef.child(name).child("score").setValue(score, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                Log.d("UpdateDiem","Update diem thanh cong");
            }
        });

    }

}
