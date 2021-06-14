package com.example.musicplayer;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.musicplayer.adapters.AddToPlaylistAdapter;
import com.example.musicplayer.model.Playlist;
import com.example.musicplayer.model.Song;

import java.util.ArrayList;
import java.util.List;

public class AddToPlaylistActivity extends AppCompatActivity {
    private Button buttonCreateNewPlaylist;
    private RecyclerView recyclerView;
    private AddToPlaylistAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_playlist);

        buttonCreateNewPlaylist=findViewById(R.id.btCreateNewPlaylist);
        recyclerView=findViewById(R.id.rv_add_to_playlist);
        adapter = new AddToPlaylistAdapter(getAllPlaylist(),this);

        adapter.setOnPlaylistItemClickListener(new AddToPlaylistAdapter.PlaylistItemClickListener() {
            @Override
            public void onPlaylistItemClick(View v, Playlist playlist, int pos) {
                Intent intent = getIntent();
                Song song = (Song) intent.getSerializableExtra("song");
                boolean check = true;
                for(Song songItem : playlist.getSongs()){
                    if(songItem.getId() == song.getId()){
                        check=false;
                        break;
                    }
                }
                if(check){
                    Uri uri = MediaStore.Audio.Playlists.Members.getContentUri("external", playlist.getId());
                    ContentValues values = new ContentValues();
                    values.put(MediaStore.Audio.Playlists.Members.AUDIO_ID,song.getId());
                    values.put(MediaStore.Audio.Playlists.Members.ARTIST,song.getArtistName());
                    values.put(MediaStore.Audio.Playlists.Members.ALBUM,song.getAlbumName());
                    values.put(MediaStore.Audio.Playlists.Members.TITLE,song.getTitle());
                    values.put(MediaStore.Audio.Playlists.Members.DURATION,song.getDuration());
                    getContentResolver().insert(uri,values);
                    finish();
                }else{
                    finish();
                }
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        buttonCreateNewPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(AddToPlaylistActivity.this);

                alert.setTitle("Tạo Playlist");
                final EditText input = new EditText(AddToPlaylistActivity.this);
                input.setHint("Nhập tên Playlist");
                alert.setView(input);

                alert.setPositiveButton("ĐỒNG Ý", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String playlistName = input.getText().toString();
                        if(playlistName.isEmpty()){
                            return;
                        }
                        Uri uri = MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI;
                        ContentValues values = new ContentValues(1);
                        values.put(MediaStore.Audio.Playlists.NAME, playlistName);
                        AddToPlaylistActivity.this.getContentResolver().insert(uri,values);
                        adapter.setPlaylists(getAllPlaylist());
                        recyclerView.setAdapter(adapter);
                    }
                });

                alert.setNegativeButton("BỎ QUA", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                });
                alert.show();
            }
        });


    }

    public ArrayList<Playlist> getAllPlaylist(){
        ArrayList<Playlist> list = new ArrayList<>();
        final String[] cursor_cols = {
                MediaStore.Audio.Playlists._ID,
                MediaStore.Audio.Playlists.NAME,
        };
        Cursor cursor = this.getContentResolver().query(MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI, cursor_cols, null,null, null);
        while (cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Playlists._ID));
            String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Playlists.NAME));
            ArrayList<Song> list1 = new ArrayList<>();
            final String[] cursor_cols1 = {
                    MediaStore.Audio.Playlists.Members.AUDIO_ID,
                    MediaStore.Audio.Playlists.Members.ARTIST,
                    MediaStore.Audio.Playlists.Members.ALBUM,
                    MediaStore.Audio.Playlists.Members.TITLE,
                    MediaStore.Audio.Playlists.Members.DURATION,
            };
            final String where = MediaStore.Audio.Media.IS_MUSIC + "=1";
            Cursor cursor1 = this.getContentResolver().query(MediaStore.Audio.Playlists.Members.getContentUri("external",id), cursor_cols1, where, null, null);
            while (cursor1.moveToNext()){
                int songId = cursor1.getInt(cursor1.getColumnIndex(MediaStore.Audio.Playlists.Members.AUDIO_ID));
                String songTitle = cursor1.getString(cursor1.getColumnIndex(MediaStore.Audio.Playlists.Members.TITLE));
                String artistName = cursor1.getString(cursor1.getColumnIndex(MediaStore.Audio.Playlists.Members.ARTIST));
                String albumName = cursor1.getString(cursor1.getColumnIndex(MediaStore.Audio.Playlists.Members.ALBUM));
                long duration = cursor1.getLong(cursor1.getColumnIndex(MediaStore.Audio.Playlists.Members.DURATION));
                list1.add(new Song(songId,songTitle,albumName,artistName,duration,null,null));
            }
            cursor1.close();
            list.add(new Playlist(id,title,list1));
        }
        cursor.close();
        return list;
    }
}