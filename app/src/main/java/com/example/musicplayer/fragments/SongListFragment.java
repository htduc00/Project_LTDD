package com.example.musicplayer.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.SearchView;

import com.example.musicplayer.AddToPlaylistActivity;
import com.example.musicplayer.MainActivity;
import com.example.musicplayer.PlayerActivity;
import com.example.musicplayer.R;
import com.example.musicplayer.adapters.AddToPlaylistAdapter;
import com.example.musicplayer.adapters.SongAdapter;
import com.example.musicplayer.model.Song;
import com.example.musicplayer.services.MusicService;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SongListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SongListFragment extends Fragment {
    private View v;
    private SongAdapter adapter;
    private RecyclerView recyclerView;
    ArrayList<Song> list;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SongListFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SongListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SongListFragment newInstance(String param1, String param2) {
        SongListFragment fragment = new SongListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_song_list, container, false);
        list = new ArrayList<>();
        final String[] cursor_cols = {
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DURATION,
        };
        final String where = MediaStore.Audio.Media.IS_MUSIC + "=1";
        Cursor cursor = getActivity().getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, cursor_cols, where, null, null);
        while (cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
            String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
            String artistName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
            String albumName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
            long duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
            list.add(new Song(id,title,albumName,artistName,duration,null,null));
        }
//        File[] files = Environment.getExternalStorageDirectory().listFiles();
//        for(File file : files) {
//            File[] songFiles = file.listFiles();
//            for (File file1 : songFiles) {
//                if (file1.getName().endsWith(".mp3")) {
//                    //Song song = new Song(file1.getName(), "", "", 0, "", "");
//                }
//            }
//        }
        recyclerView = v.findViewById(R.id.rv_song_list);
        adapter = new SongAdapter(list,this.getContext());
        adapter.setOnSongItemClickListener(new SongAdapter.SongItemClickListener() {
            @Override
            public void onSongItemClick(View v, Song song, int pos) {
                Intent intent = new Intent(getActivity(), PlayerActivity.class);
                intent.putExtra("list",list);
                intent.putExtra("songPos", pos);
                startActivity(intent);
            }
        });

        adapter.setOnSongBtnClickListener(new SongAdapter.SongBtnClickListener() {
            @Override
            public void onSongBtnClickListener(ImageButton btn, View v, Song song, int pos) {
                PopupMenu popupMenu = new PopupMenu(getContext(),v);
                popupMenu.getMenuInflater().inflate(R.menu.add_to_playlist,popupMenu.getMenu());
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        Intent intent = new Intent(getActivity(), AddToPlaylistActivity.class);
                        intent.putExtra("song",song);
                        startActivity(intent);
                        return true;
                    }
                });
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);


        return v;
        }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.search,menu);
        MenuItem item = menu.findItem(R.id.search_option);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String input = newText.toLowerCase();
                ArrayList<Song> newList = new ArrayList<>();
                final String[] cursor_cols = {
                        MediaStore.Audio.Media._ID,
                        MediaStore.Audio.Media.ARTIST,
                        MediaStore.Audio.Media.ALBUM,
                        MediaStore.Audio.Media.TITLE,
                        MediaStore.Audio.Media.DURATION,
                };
                final String where = MediaStore.Audio.Media.IS_MUSIC + "=1";
                Cursor cursor = getActivity().getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, cursor_cols, where, null, null);
                System.out.println(cursor.getCount());
                while (cursor.moveToNext()){
                    int id = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
                    String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                    String artistName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                    String albumName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                    long duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                    if(title.toLowerCase().contains(input)){
                        newList.add(new Song(id,title,albumName,artistName,duration,null,null));
                    }
                }
                adapter.setSongs(newList);
                recyclerView.setAdapter(adapter);
                return true;
            }

        });
    }
}