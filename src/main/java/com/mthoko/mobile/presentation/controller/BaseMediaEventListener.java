package com.mthoko.mobile.presentation.controller;

public interface BaseMediaEventListener {

    void onPlay();

    void onPause();

    void onStop();

    void onResume();

    void onProgress();

    void onRewind();

    void onForward();

}
