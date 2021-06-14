package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.musicplayer.adapters.SongAdapter;
import com.example.musicplayer.model.Playlist;
import com.example.musicplayer.model.Song;

import java.util.ArrayList;
import java.util.Collections;

public class PlaylistDetailActivity extends AppCompatActivity {
    private TextView playlistName;
    private TextView numOfSongs;
    private Button playShuffle;
    private RecyclerView recyclerView;
    private SongAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist_detail);
        playlistName=findViewById(R.id.playlist_detail_text_view_playlistName);
        numOfSongs=findViewById(R.id.playlist_detail_text_view_numOfSong);
        playShuffle=findViewById(R.id.playlist_detail_button_play_shuffle);
        recyclerView=findViewById(R.id.playlist_detail_recycler_view);

        Intent intent = getIntent();
        Playlist playlist = (Playlist)intent.getSerializableExtra("playlist");
        playlistName.setText(playlist.getName());
        numOfSongs.setText(playlist.getSongs().size()+" bài hát");
        ArrayList listSongs = playlist.getSongs();
        adapter = new SongAdapter(listSongs,this);
        adapter.setOnSongItemClickListener(new SongAdapter.SongItemClickListener() {
            @Override
            public void onSongItemClick(View v, Song song, int pos) {
                Intent intent = new Intent(PlaylistDetailActivity.this,PlayerActivity.class);
                intent.putExtra("list",listSongs);
                intent.putExtra("songPos",pos);
                startActivity(intent);
            }
        });

        adapter.setOnSongBtnClickListener(new SongAdapter.SongBtnClickListener() {
            @Override
            public void onSongBtnClickListener(ImageButton btn, View v, Song song, int pos) {
                PopupMenu popupMenu = new PopupMenu(PlaylistDetailActivity.this,v);
                popupMenu.getMenuInflater().inflate(R.menu.delete_song_from_playlist,popupMenu.getMenu());
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        Uri uri = MediaStore.Audio.Playlists.Members.getContentUri("external", playlist.getId());
                        getContentResolver().delete(uri, MediaStore.Audio.Playlists.Members.AUDIO_ID +" = "+song.getId(), null);
                        listSongs.remove(song);
                        adapter.setSongs(listSongs);
                        recyclerView.setAdapter(adapter);
                        return true;
                    }
                });
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        playShuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Song> listShuffle = listSongs;
                Collections.shuffle(listShuffle);
                Intent intent = new Intent(PlaylistDetailActivity.this,PlayerActivity.class);
                intent.putExtra("list",listShuffle);
                intent.putExtra("songPos",0);
                startActivity(intent);
            }
        });
    }
}