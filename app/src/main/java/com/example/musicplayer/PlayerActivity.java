package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentUris;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.musicplayer.model.Song;
import java.util.ArrayList;
import java.util.Collections;

public class PlayerActivity extends AppCompatActivity {
    private boolean repeat = false;
    private boolean shuffle = false;
    private boolean completion = false;
    private static MediaPlayer mp;
    private static ArrayList<Song> list;
    private int songPos;

    private ImageButton buttonPlay,buttonNext,buttonPrev,buttonShuffle,buttonRepeat;
    private ImageView buttonBack,buttonMenu;
    private TextView songName,artistName,currentTime,totalTime;
    private SeekBar seekBar;
    Thread updateSeekBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        initView();
        if(mp != null){
            mp.stop();
            mp.reset();
            mp.release();
            mp = null;
        }
        Intent intent = getIntent();
        list = (ArrayList<Song>)intent.getSerializableExtra("list");
        final ArrayList<Song> prevList = (ArrayList<Song>)intent.getSerializableExtra("list");
        songPos = intent.getIntExtra("songPos",0);
        songName.setText(list.get(songPos).getTitle());
        artistName.setText(list.get(songPos).getArtistName());
        Uri trackUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, list.get(songPos).getId());
        mp = MediaPlayer.create(this,trackUri);
        mp.start();
        totalTime.setText(DurationToTime(mp.getDuration()));
        buttonPlay.setImageResource(R.drawable.ic_baseline_pause_24);

//        updateSeekBar = new Thread(){
//            @Override
//            public void run() {
//                int totalDuration = mp.getDuration();
//                int currentpos = 0;
//                while(currentpos < totalDuration){
//                    try {
//                        sleep(1000);
//                        currentpos = mp.getCurrentPosition();
//                        seekBar.setProgress(currentpos);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        };
//        updateSeekBar.start();
        seekBar.setMax(mp.getDuration());


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mp.seekTo(seekBar.getProgress());
            }
        });


        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                currentTime.setText(DurationToTime(mp.getCurrentPosition()));
                handler.postDelayed(this,1000);
            }
        });

        handler.post(new Runnable() {
            @Override
            public void run() {
                int totalDuration = mp.getDuration();
                int currentpos = 0;
                if(currentpos < totalDuration){
                    currentpos = mp.getCurrentPosition();
                    seekBar.setProgress(currentpos);
                }
                handler.postDelayed(this,1000);
            }
        });

        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                completion = true;
                buttonNext.performClick();
            }
        });

        buttonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mp.isPlaying()){
                    buttonPlay.setImageResource(R.drawable.ic_baseline_play_arrow_24);
                    mp.pause();
                }else{
                    buttonPlay.setImageResource(R.drawable.ic_baseline_pause_24);
                    mp.start();
                }
            }
        });
        buttonRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!repeat){
                    buttonRepeat.setImageResource(R.drawable.ic_baseline_repeat_on);
                    repeat=true;
                }else{
                    buttonRepeat.setImageResource(R.drawable.ic_baseline_repeat_24);
                    repeat=false;
                }
            }
        });

        buttonShuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(shuffle == false){
                    shuffle=true;
                    buttonShuffle.setImageResource(R.drawable.ic_baseline_shuffle_on);
                    Song currSong = list.get(songPos);
                    ArrayList<Song> shuffleList = new ArrayList<>();
                    for(Song song : list){
                        if(!song.equals(currSong))
                            shuffleList.add(song);
                    }
                    Collections.shuffle(shuffleList);
                    shuffleList.add(0,currSong);
                    songPos = 0;
                    setList(shuffleList);
                }else{
                    shuffle=false;
                    buttonShuffle.setImageResource(R.drawable.ic_baseline_shuffle_24);
                    setList(prevList);
                    for(int i =0; i<list.size(); i++){
                        if(list.get(i).getTitle().equalsIgnoreCase(songName.getText().toString())){
                            songPos = i;
                            break;
                        }
                    }
                }
                for(Song song : list){
                    System.out.println(song.getTitle());
                }
                System.out.println(songPos);
            }
        });

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checkState = false;
                if(completion){
                    checkState = true;
                }
                mp.stop();
                mp.reset();
                mp.release();
                songPos = (songPos+1)%list.size();
                songName.setText(list.get(songPos).getTitle());
                artistName.setText(list.get(songPos).getArtistName());
                Uri trackUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, list.get(songPos).getId());
                mp = MediaPlayer.create(getApplicationContext(),trackUri);
                mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        buttonNext.performClick();
                        completion = true;
                    }
                });
                mp.start();
                completion = false;
                seekBar.setMax(mp.getDuration());
                totalTime.setText(DurationToTime(mp.getDuration()));
                buttonPlay.setImageResource(R.drawable.ic_baseline_pause_24);
                if(!repeat && songPos == 0 && checkState){
                    mp.pause();
                    buttonPlay.setImageResource(R.drawable.ic_baseline_play_arrow_24);
                }
            }
        });
        buttonPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                songPos = songPos- 1;
                if(songPos < 0){
                    songPos=list.size()-1;
                }
                mp.stop();
                mp.reset();
                mp.release();
                songName.setText(list.get(songPos).getTitle());
                artistName.setText(list.get(songPos).getArtistName());
                Uri trackUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, list.get(songPos).getId());
                mp = MediaPlayer.create(getApplicationContext(),trackUri);
                mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        buttonNext.performClick();
                        completion = true;
                    }
                });
                mp.start();
                completion = false;
                seekBar.setMax(mp.getDuration());
                totalTime.setText(DurationToTime(mp.getDuration()));
                buttonPlay.setImageResource(R.drawable.ic_baseline_pause_24);
            }
        });
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    public void initView(){
        buttonPlay=findViewById(R.id.buttonPlay);
        buttonNext=findViewById(R.id.buttonNext);
        buttonPrev=findViewById(R.id.buttonPrev);
        buttonShuffle=findViewById(R.id.buttonShuffle);
        buttonRepeat=findViewById(R.id.buttonRepeat);
        buttonBack=findViewById(R.id.buttonBack);
        buttonMenu=findViewById(R.id.buttonMenu);
        songName=findViewById(R.id.textTitle);
        artistName=findViewById(R.id.textArtist);
        currentTime=findViewById(R.id.textCurrentTime);
        totalTime=findViewById(R.id.textTotalTime);
        seekBar=findViewById(R.id.playerSeekBar);
    }

    public String DurationToTime(int duration){
        String time = "";

        int minutes = duration/1000/60;
        int seconds = duration/1000%60;

        time = minutes+":";
        if(seconds < 10){
            time +="0";
        }
        time+=seconds;
        return time;
    }

    public void setList(ArrayList<Song> list1){
        this.list = list1;
    }

}