package com.example.duoihinhbatchu.Models;

import java.util.HashMap;
import java.util.Map;

public class CauDo {
    private int id;
    private String imgUrl;
    private String dapan;

    public CauDo() {
    }

    public CauDo(String imgUrl, String dapan) {
        this.imgUrl = imgUrl;
        this.dapan = dapan;
    }

    public CauDo(int id, String imgUrl, String dapan) {
        this.id = id;
        this.imgUrl = imgUrl;
        this.dapan = dapan;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getDapan() {
        return dapan;
    }

    public void setDapan(String dapan) {
        this.dapan = dapan;
    }

    public Map<String,Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("dapan",dapan);
        result.put("imgUrl",imgUrl);
        return result;
    }
}
