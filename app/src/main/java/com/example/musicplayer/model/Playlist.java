package com.example.musicplayer.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Playlist implements Serializable {
    private int id;
    private String name;
    private ArrayList<Song> songs;

    public Playlist() {
    }

    public Playlist(int id, String name, ArrayList<Song> songs) {
        this.id = id;
        this.name = name;
        this.songs = songs;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Song> getSongs() {
        return songs;
    }

    public void setSongs(ArrayList<Song> songs) {
        this.songs = songs;
    }
}
