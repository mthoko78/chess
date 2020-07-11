package com.mthoko.mobile.presentation.controller;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.widget.Toast;

import com.mthoko.mobile.R;

import java.io.File;
import java.io.IOException;

public class AudioPlayer {


    public static final int
            PLAYING = 0,
            PAUSED = 1,
            STOPPED = 2,
            FORWARD_TIME = 5000,
            BACKWARD_TIME = 5000;
    private final BaseMediaEventListener mediaEventListener;

    private MediaPlayer mediaPlayer;
    private Handler myHandler = new Handler();
    String currentFilename;

    private int
            currentTime = 0,
            finalTime = 0;

    private int status = STOPPED;
    private Context context;

    private MediaPlayer.OnCompletionListener completionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            stop();
        }
    };

    private Runnable progressHandler = new Runnable() {
        public void run() {
            currentTime = mediaPlayer.getCurrentPosition();
            myHandler.postDelayed(this, 100);
            mediaEventListener.onProgress();
        }
    };


    public AudioPlayer(Context context, BaseMediaEventListener mediaEventListener) {
        this.context = context;
        this.mediaEventListener = mediaEventListener;
    }

    public void play(File file) {
        if (file != null && file.exists()) {
            if (status != STOPPED) {
                stop();
            }
            currentFilename = file.getAbsolutePath();
            status = STOPPED;
            togglePlay();
        }
    }

    public void togglePlay() {
        switch (status) {
            case STOPPED: {
                play();
                break;
            }
            case PLAYING: {
                pause();
                break;
            }
            case PAUSED: {
                resume();
            }
        }
    }

    public void resume() {
        showMessage("resuming:" + currentFilename);
        mediaPlayer.start();
        status = PLAYING;
        mediaEventListener.onResume();
    }

    public void play() {
        if (currentFilename == null) {
            currentFilename = getContext().getResources().getResourceName(R.raw.song);
        }
        if (!new File(currentFilename).exists()) {
            showMessage("File not found");
            return;
        }
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setOnCompletionListener(completionListener);
            mediaPlayer.setDataSource(currentFilename);
            mediaPlayer.prepare();
            mediaPlayer.start();
            finalTime = mediaPlayer.getDuration();
            currentTime = mediaPlayer.getCurrentPosition();
            myHandler.postDelayed(progressHandler, 100);
            showMessage("playing");
            status = PLAYING;
            mediaEventListener.onPlay();
        } catch (IOException | RuntimeException e) {
            e.printStackTrace();
            showMessage("Error, MediaPlayerController.java line 111:" + e.toString());
        }
    }

    public void pause() {
        showMessage("Paused");
        mediaPlayer.pause();
        mediaEventListener.onPause();
        status = PAUSED;
    }

    private Context getContext() {
        return context;
    }

    public void forward() {
        if (status == STOPPED) {
            showMessage("Not playing");
            return;
        }
        int newTime = currentTime + FORWARD_TIME;
        if (newTime > finalTime) {
            newTime = finalTime;
            showMessage("...");
        }
        jumpTo(newTime);
        mediaEventListener.onForward();
    }

    public void jumpTo(int time) {
        if (status != STOPPED) {
            currentTime = time;
            mediaPlayer.seekTo(time);
        }
    }

    public void rewind() {
        if (status == STOPPED) {
            showMessage("Not playing");
            return;
        }
        int newTime = (int) (currentTime - BACKWARD_TIME);
        if (newTime < 0) {
            newTime = 0;
            showMessage("Beginning");
        }
        jumpTo(newTime);
        mediaEventListener.onRewind();
    }

    public void stop() {
        switch (status) {
            case STOPPED:
                showMessage("Sound already stopped");
                break;
            case PLAYING:
            case PAUSED:
                try {
                    currentTime = 0;
                    myHandler.removeCallbacks(progressHandler);
                    mediaPlayer.stop();
                    mediaPlayer.reset();
                    mediaPlayer.release();
                    mediaPlayer = null;
                    mediaEventListener.onStop();
                    showMessage("STOPPED");
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    showMessage("An error occurred while closing: " + e.getMessage());
                }
        }
        status = STOPPED;
    }

    public void showMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    public double getCurrentTime() {
        return currentTime;
    }

    public int getFinalTime() {
        return finalTime;
    }

    public int getStatus() {
        return status;
    }

    public boolean isPlaying() {
        return status == PLAYING;
    }

    public boolean isStopped() {
        return status == STOPPED;
    }
}
