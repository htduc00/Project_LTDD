package com.example.musicplayer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicplayer.R;
import com.example.musicplayer.model.Song;

import java.util.ArrayList;
import java.util.List;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.ViewHolder>{
    List<Song> songs;
    Context context;

    SongItemClickListener songItemClickListener;
    SongItemLongClickListener songItemLongClickListener;
    SongBtnClickListener songBtnClickListener;

    public SongAdapter(List<Song> songs, Context context) {
        this.songs = songs;
        this.context = context;
    }

    public void setSongs(ArrayList<Song> newList){
        this.songs = newList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_song,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Song song = songs.get(position);
        holder.tvSongName.setText(song.getTitle());
        holder.tvArtistName.setText(song.getArtistName());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(songItemClickListener != null ) {
                    songItemClickListener.onSongItemClick(v,song,position);
                }
            }
        });

        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(songItemLongClickListener != null) {
                    songItemLongClickListener.onSongItemLongClickListener(v,song,position);
                }
                return true;
            }
        });

        holder.menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(songBtnClickListener != null) {
                    songBtnClickListener.onSongBtnClickListener(holder.menuButton,v,song,position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView tvSongName;
        TextView tvArtistName;
        ImageButton menuButton;
        ImageView ivSongImg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView=itemView.findViewById(R.id.song_card_view);
            tvSongName=itemView.findViewById(R.id.tv_song_name);
            tvArtistName=itemView.findViewById(R.id.tv_artist_name);
            menuButton=itemView.findViewById(R.id.bt_song_menu);
            ivSongImg=itemView.findViewById(R.id.iv_song_coverart);
        }
    }

    public interface SongItemClickListener {
        void onSongItemClick(View v,Song song,int pos);
    }

    public interface SongItemLongClickListener {
        void onSongItemLongClickListener(View v, Song song, int pos);
    }

    public interface SongBtnClickListener {
        void onSongBtnClickListener(ImageButton btn, View v, Song song, int pos);
    }

    public void setOnSongItemClickListener(SongItemClickListener songItemClickListener) {
        this.songItemClickListener = songItemClickListener;
    }

    public void setOnSongItemLongClickListener(SongItemLongClickListener songItemLongClickListener) {
        this.songItemLongClickListener=songItemLongClickListener;
    }
    public void setOnSongBtnClickListener(SongBtnClickListener songBtnClickListener) {
        this.songBtnClickListener = songBtnClickListener;
    }
}
