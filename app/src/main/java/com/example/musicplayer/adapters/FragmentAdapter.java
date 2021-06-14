package com.example.musicplayer.adapters;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.musicplayer.fragments.AlbumListFragment;
import com.example.musicplayer.fragments.PlaylistListFragment;
import com.example.musicplayer.fragments.SongListFragment;

public class FragmentAdapter extends FragmentStatePagerAdapter {
    private int pageNum;
    public FragmentAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        this.pageNum = behavior;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0: return new SongListFragment();
            case 1: return new AlbumListFragment();
            case 2: return new PlaylistListFragment();
            default: return null;
        }
    }

    @Override
    public int getCount() {
        return pageNum;
    }
}
