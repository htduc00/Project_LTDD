package com.example.musicplayer.model;

import java.io.Serializable;

public class Song implements Serializable {
    private int id;
    private String title;
    private String albumName;
    private String artistName;
    private long duration;
    private String Img;
    private String data;

    public Song(int id, String title, String albumName, String artistName, long duration, String img, String data) {
        this.id = id;
        this.title = title;
        this.albumName = albumName;
        this.artistName = artistName;
        this.duration = duration;
        Img = img;
        this.data = data;
    }

    public Song() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getImg() {
        return Img;
    }

    public void setImg(String img) {
        Img = img;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
