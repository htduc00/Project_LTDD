package com.example.musicplayer.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.musicplayer.PlayerActivity;
import com.example.musicplayer.R;
import com.example.musicplayer.adapters.AlbumAdapter;
import com.example.musicplayer.adapters.SongAdapter;
import com.example.musicplayer.model.Album;
import com.example.musicplayer.model.Song;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AlbumListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AlbumListFragment extends Fragment {
    private View v;
    private RecyclerView recyclerView;
    private AlbumAdapter adapter;
    ArrayList<Album> list;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AlbumListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AlbumListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AlbumListFragment newInstance(String param1, String param2) {
        AlbumListFragment fragment = new AlbumListFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_album_list, container, false);
        list = new ArrayList<>();
        final String[] cursor_cols = {
                MediaStore.Audio.Albums._ID,
                MediaStore.Audio.Albums.ALBUM,
                MediaStore.Audio.Albums.ARTIST,
                MediaStore.Audio.Albums.NUMBER_OF_SONGS
        };
        Cursor cursor = getActivity().getContentResolver().query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI, cursor_cols, null,null, null);
        while (cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Albums._ID));
            ArrayList<Song> list1 = new ArrayList<>();
            final String[] cursor_cols1 = {
                    MediaStore.Audio.Media._ID,
                    MediaStore.Audio.Media.ARTIST,
                    MediaStore.Audio.Media.ALBUM,
                    MediaStore.Audio.Media.TITLE,
                    MediaStore.Audio.Media.DURATION,
                    MediaStore.Audio.Media.ALBUM_ID,
            };
            final String where = MediaStore.Audio.Media.IS_MUSIC + "=1";
            Cursor cursor1 = getActivity().getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, cursor_cols1, where, null, null);
            while (cursor1.moveToNext()){
                int songId = cursor1.getInt(cursor1.getColumnIndex(MediaStore.Audio.Media._ID));
                String title = cursor1.getString(cursor1.getColumnIndex(MediaStore.Audio.Media.TITLE));
                String artistName = cursor1.getString(cursor1.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                String albumName = cursor1.getString(cursor1.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                long duration = cursor1.getLong(cursor1.getColumnIndex(MediaStore.Audio.Media.DURATION));
                int albumId = cursor1.getInt(cursor1.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
                if(albumId == id){
                    list1.add(new Song(songId,title,albumName,artistName,duration,null,null));
                }
            }
            list.add(new Album(id,list1));
        }


        recyclerView = v.findViewById(R.id.recyclerView);
        adapter = new AlbumAdapter(list,this.getContext());
        adapter.setOnAlbumItemClickListener(new AlbumAdapter.AlbumItemClickListener() {
            @Override
            public void onAlbumItemClick(View v, Album album, int pos) {
                Intent intent = new Intent(getActivity(), PlayerActivity.class);
                intent.putExtra("list",album.getSongs());
                intent.putExtra("songPos", 0);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);
        return v;
    }
}