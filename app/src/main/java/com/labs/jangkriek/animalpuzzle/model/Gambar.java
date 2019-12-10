package com.labs.jangkriek.animalpuzzle.model;

public class Gambar {

    private int imageView;
    private String nama;

    public Gambar(int imageView, String nama) {
        this.imageView = imageView;
        this.nama = nama;
    }

    public int getImageView() {
        return imageView;
    }

    public void setImageView(int imageView) {
        this.imageView = imageView;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }
}
