package com.example.duoihinhbatchu.Models;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class User implements Serializable {
    private String name;
    private int score;
    private int isAdmin;

    public User() {
    }

    public User(String name) {
        this.name = name;
        this.score = 0;
        this.isAdmin = 0;
    }

    public int getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(int isAdmin) {
        this.isAdmin = isAdmin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Map<String,Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("score",score);
        result.put("name",name);
        return result;
    }
    public Map<String,Object> toMapAdmin(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("isAdmin",isAdmin);
        return result;
    }
}
