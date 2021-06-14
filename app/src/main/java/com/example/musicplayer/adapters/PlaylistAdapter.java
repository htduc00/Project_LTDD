package com.example.musicplayer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicplayer.R;
import com.example.musicplayer.model.Playlist;

import java.util.ArrayList;
import java.util.List;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.ViewHolder> {

    List<Playlist> playlists;
    Context context;
    PlaylistItemClickListener playlistItemClickListener;
    PlaylistBtnClickListener playlistBtnClickListener;

    public PlaylistAdapter(List<Playlist> playlists, Context context) {
        this.playlists = playlists;
        this.context = context;
    }

    public void setPlaylists(ArrayList<Playlist> list){
        this.playlists = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_playlist,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Playlist playlist = playlists.get(position);
        holder.tvPlaylistName.setText(playlist.getName());
        holder.tvPlaylistSongs.setText(playlist.getSongs().size()+" bài hát");
        holder.btPlaylistMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(playlistBtnClickListener!=null){
                    playlistBtnClickListener.onListBtnClickListener(holder.btPlaylistMenu,v,playlist,position);
                }
            }
        });

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(playlistItemClickListener != null){
                    playlistItemClickListener.onPlaylistItemClick(v,playlist,position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return playlists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        TextView tvPlaylistName;
        TextView tvPlaylistSongs;
        ImageButton btPlaylistMenu;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.playlist_card_view);
            tvPlaylistName=itemView.findViewById(R.id.tv_playlist_name);
            tvPlaylistSongs=itemView.findViewById(R.id.tv_playlist_songs);
            btPlaylistMenu=itemView.findViewById(R.id.bt_playlist_menu);
        }
    }

    public interface PlaylistItemClickListener {
        void onPlaylistItemClick(View v,Playlist playlist,int pos);
    }

    public interface PlaylistBtnClickListener {
        void onListBtnClickListener(ImageButton btn, View v, Playlist playlist, int pos);
    }

    public void setOnPlaylistItemClickListener(PlaylistItemClickListener playlistItemClickListener) {
        this.playlistItemClickListener = playlistItemClickListener;
    }
    public void setOnSongBtnClickListener(PlaylistBtnClickListener playlistBtnClickListener) {
        this.playlistBtnClickListener = playlistBtnClickListener;
    }
}
