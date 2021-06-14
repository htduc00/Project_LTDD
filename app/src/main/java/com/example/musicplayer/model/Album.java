package com.example.musicplayer.model;

import java.util.ArrayList;

public class Album {
    private int id;
    private ArrayList<Song> songs;

    public Album(int id, ArrayList<Song> songs) {
        this.id = id;
        this.songs = songs;
    }

    public Album() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<Song> getSongs() {
        return songs;
    }

    public void setSongs(ArrayList<Song> songs) {
        this.songs = songs;
    }
    public String getAlbumName(){
        if(songs.size()>0){
            return songs.get(0).getAlbumName();
        }else{
            return " ";
        }
    }
    public String getArtistName(){
        if(songs.size()>0){
            return songs.get(0).getArtistName();
        }else{
            return " ";
        }
    }
}
