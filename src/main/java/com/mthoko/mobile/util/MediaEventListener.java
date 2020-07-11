package com.mthoko.mobile.util;

public interface MediaEventListener {

  void onPlay();

  void onStopPlaying();

  void onPausePlaying();

  void onResumePlaying();

  void onStartRecording();

  void onStopRecording();

  void onPauseRecording();

  void onResumeRecording();
}