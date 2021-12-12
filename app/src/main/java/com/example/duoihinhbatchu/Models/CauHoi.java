package com.example.duoihinhbatchu.Models;

public class CauHoi {
    private int id;
    private String imgUrl;
    private String dapan;

    public CauHoi() {
    }

    public CauHoi(String imgUrl, String dapan) {
        this.imgUrl = imgUrl;
        this.dapan = dapan;
    }

    public CauHoi(int id, String imgUrl, String dapan) {
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
}
