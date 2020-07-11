package com.mthoko.mobile.presentation.controller;

import android.content.Context;
import android.media.MediaPlayer;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.mthoko.mobile.R;

import java.io.File;
import java.util.concurrent.TimeUnit;

/**
 * Created by Mthoko on 17 Jun 2018.
 */

public class MediaPlayerController {

    private final TextView startTimeField, endTimeField;
    private ImageButton
            playPauseButton,
            stopButton,
            forwardButton,
            rewindButton;
    private SeekBar seekbar;

    private final AudioPlayer player;

    public MediaPlayerController(TextView startTimeField, TextView endTimeField, SeekBar seekbar, final ImageButton playPauseButton, ImageButton stopButton, ImageButton forwardButton, ImageButton rewindButton) {
        this.startTimeField = startTimeField;
        this.endTimeField = endTimeField;
        this.seekbar = seekbar;
        this.playPauseButton = playPauseButton;
        this.stopButton = stopButton;
        this.forwardButton = forwardButton;
        this.rewindButton = rewindButton;
        this.player = new AudioPlayer(getContext(), this.mediaListener);
        this.playPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                player.togglePlay();
            }
        });
        this.stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                player.stop();
            }
        });
        this.forwardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                player.forward();
            }
        });
        this.rewindButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                player.rewind();
            }
        });
        this.seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                player.jumpTo(progress);
                player.showMessage("progress  : " + progress + "\nfinal time: " + player.getFinalTime());
            }
        });
    }

    public void play(File file) {
        player.play(file);
    }

    MediaPlayer.OnCompletionListener completionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            player.stop();
        }
    };

    private Context getContext() {
        return playPauseButton.getContext();
    }

    BaseMediaEventListener mediaListener = new BaseMediaEventListener() {

        @Override
        public void onPlay() {
            seekbar.setMax((int) player.getFinalTime());
            updateCurrentTime();
            updateFinalTime();
            seekbar.setProgress((int) (long) player.getCurrentTime());
            playPauseButton.setImageResource(R.drawable.ic_pause_black_24dp);
        }

        @Override
        public void onPause() {
            playPauseButton.setImageResource(R.drawable.ic_play_arrow_black_24dp);
        }

        @Override
        public void onStop() {
            playPauseButton.setImageResource(R.drawable.ic_play_arrow_black_24dp);
            onProgress();
        }

        @Override
        public void onResume() {
            playPauseButton.setImageResource(R.drawable.ic_pause_black_24dp);
        }

        @Override
        public void onRewind() {

        }

        @Override
        public void onForward() {

        }

        @Override
        public void onProgress() {
            updateCurrentTime();
            seekbar.setProgress((int) player.getCurrentTime());
        }
    };

    public void updateFinalTime() {
        endTimeField.setText(String.format("%s:%s", (TimeUnit.MILLISECONDS.toMinutes((long) player.getFinalTime()) < 10 ? "0" : "") + TimeUnit.MILLISECONDS.toMinutes((long) player.getFinalTime()), (TimeUnit.MILLISECONDS.toSeconds((long) player.getFinalTime()) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) player.getFinalTime())) < 10 ? "0" : "") + (TimeUnit.MILLISECONDS.toSeconds((long) player.getFinalTime()) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) player.getFinalTime())))));
    }

    public void updateCurrentTime() {
        long time = TimeUnit.MILLISECONDS.toMinutes((long) player.getCurrentTime());
        long newTime = TimeUnit.MILLISECONDS.toSeconds((long) player.getCurrentTime()) - TimeUnit.MINUTES.toSeconds(time);
        startTimeField.setText(String.format("%s:%s", (time < 10 ? "0" : "") + time, (newTime < 10 ? "0" : "") + newTime));
    }
}
