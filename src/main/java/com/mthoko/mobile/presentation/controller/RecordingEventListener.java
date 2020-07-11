package com.mthoko.mobile.presentation.controller;

import java.io.File;

public interface RecordingEventListener {

    void onStart();

    void onPause();

    void onStop(File file);

    void onResume();

    void onProgress();
}
