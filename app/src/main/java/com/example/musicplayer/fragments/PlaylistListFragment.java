package com.example.musicplayer.fragments;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;

import com.example.musicplayer.AddToPlaylistActivity;
import com.example.musicplayer.PlaylistDetailActivity;
import com.example.musicplayer.R;
import com.example.musicplayer.adapters.PlaylistAdapter;
import com.example.musicplayer.model.Playlist;
import com.example.musicplayer.model.Song;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PlaylistListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlaylistListFragment extends Fragment {
    private View v;
    private PlaylistAdapter adapter;
    private RecyclerView recyclerView;
    private Button btCreateNewPlaylist;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PlaylistListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PlaylistListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PlaylistListFragment newInstance(String param1, String param2) {
        PlaylistListFragment fragment = new PlaylistListFragment();
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
        v = inflater.inflate(R.layout.fragment_playlist_list, container, false);
        recyclerView=v.findViewById(R.id.fm_recycler_view);
        btCreateNewPlaylist=v.findViewById(R.id.fm_bt_create_new);
        adapter = new PlaylistAdapter(getAllPlaylist(),getContext());
        adapter.setOnPlaylistItemClickListener(new PlaylistAdapter.PlaylistItemClickListener() {
            @Override
            public void onPlaylistItemClick(View v, Playlist playlist, int pos) {
                Intent intent = new Intent(getActivity(), PlaylistDetailActivity.class);
                intent.putExtra("playlist",playlist);
                startActivity(intent);
            }
        });
        adapter.setOnSongBtnClickListener(new PlaylistAdapter.PlaylistBtnClickListener() {
            @Override
            public void onListBtnClickListener(ImageButton btn, View v, Playlist playlist, int pos) {
                PopupMenu popupMenu = new PopupMenu(getContext(),v);
                popupMenu.getMenuInflater().inflate(R.menu.delete_playlist,popupMenu.getMenu());
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        AlertDialog.Builder al = new AlertDialog.Builder(getContext());

                        al.setTitle("Co chac muon xoa ?");

                        al.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Uri uri = MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI;
                                getActivity().getContentResolver().delete(uri,MediaStore.Audio.Playlists._ID +" = "+playlist.getId(),null);
                                adapter.setPlaylists(getAllPlaylist());
                                recyclerView.setAdapter(adapter);
                            }
                        });

                        al.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                return;
                            }
                        });
                        al.show();
                        return true;
                    }
                });
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        btCreateNewPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());

                alert.setTitle("Tạo Playlist");
                final EditText input = new EditText(getContext());
                input.setHint("Nhập tên Playlist");
                alert.setView(input);

                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String playlistName = input.getText().toString();
                        if(playlistName.isEmpty()){
                            return;
                        }
                        Uri uri = MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI;
                        ContentValues values = new ContentValues(1);
                        values.put(MediaStore.Audio.Playlists.NAME, playlistName);
                        getActivity().getContentResolver().insert(uri,values);
                        adapter.setPlaylists(getAllPlaylist());
                        recyclerView.setAdapter(adapter);
                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                });
                alert.show();
            }
        });

        return v;
    }

    public ArrayList<Playlist> getAllPlaylist(){
        ArrayList<Playlist> list = new ArrayList<>();
        final String[] cursor_cols = {
                MediaStore.Audio.Playlists._ID,
                MediaStore.Audio.Playlists.NAME,
        };
        Cursor cursor = getActivity().getContentResolver().query(MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI, cursor_cols, null,null, null);
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
            Cursor cursor1 = getActivity().getContentResolver().query(MediaStore.Audio.Playlists.Members.getContentUri("external",id), cursor_cols1, where, null, null);
            while (cursor1.moveToNext()){
                int songId = cursor1.getInt(cursor1.getColumnIndex(MediaStore.Audio.Playlists.Members.AUDIO_ID));
                String songTitle = cursor1.getString(cursor1.getColumnIndex(MediaStore.Audio.Playlists.Members.TITLE));
                String artistName = cursor1.getString(cursor1.getColumnIndex(MediaStore.Audio.Playlists.Members.ARTIST));
                String albumName = cursor1.getString(cursor1.getColumnIndex(MediaStore.Audio.Playlists.Members.ALBUM));
                long duration = cursor1.getLong(cursor1.getColumnIndex(MediaStore.Audio.Playlists.Members.DURATION));
                list1.add(new Song(songId,songTitle,albumName,artistName,duration,null,null));
            }
            list.add(new Playlist(id,title,list1));
        }
        return list;
    }


}