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
import com.example.musicplayer.model.Album;

import java.util.List;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder>{
    List<Album> albums;
    Context context;

    AlbumItemClickListener albumItemClickListener;

    public AlbumAdapter(List<Album> albums, Context context) {
        this.albums = albums;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_album,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Album album = albums.get(position);
        holder.tvAlbumName.setText(album.getAlbumName());
        holder.tvArtistName.setText(album.getSongs().size()+" bài hát");

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(albumItemClickListener != null ) {
                    albumItemClickListener.onAlbumItemClick(v,album,position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return albums.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView tvAlbumName;
        TextView tvArtistName;
        ImageView ivAlbumImg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView=itemView.findViewById(R.id.album_card_view);
            tvAlbumName=itemView.findViewById(R.id.album_name);
            tvArtistName=itemView.findViewById(R.id.artist_name);
            ivAlbumImg=itemView.findViewById(R.id.album_art);
        }
    }

    public interface AlbumItemClickListener {
        void onAlbumItemClick(View v,Album album,int pos);
    }

    public void setOnAlbumItemClickListener(AlbumItemClickListener albumItemClickListener) {
        this.albumItemClickListener = albumItemClickListener;
    }

}
