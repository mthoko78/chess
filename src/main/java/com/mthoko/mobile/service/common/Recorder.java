package com.mthoko.mobile.service.common;

import android.content.Context;
import android.media.MediaRecorder;
import android.util.Log;
import android.widget.Toast;

import com.mthoko.mobile.exception.ApplicationException;
import com.mthoko.mobile.presentation.controller.RecordingEventListener;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import static com.mthoko.mobile.service.common.RecordingService.getNewFilename;
import static com.mthoko.mobile.util.EntityMapper.getTimeStampFromDate;

public class Recorder {

    private final Context context;
    private boolean recording;
    private long startTime;
    private String outputFileName;
    private MediaRecorder myAudioRecorder;
    private final RecordingEventListener eventListener;

    public Recorder(Context context, RecordingEventListener eventListener) {
        this.context = context;
        this.eventListener = eventListener;
    }

    public boolean isRecording() {
        return recording;
    }

    public void startRecording(String outputFileName) {
        if (recording) {
            return;
        }
        if (outputFileName == null) {
            outputFileName = getNewFilename();
        }
        try {
            myAudioRecorder = new MediaRecorder();
            myAudioRecorder.reset();
            myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
            myAudioRecorder.setOutputFile(outputFileName);
            myAudioRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            myAudioRecorder.prepare();
            try {
                myAudioRecorder.start();
            }
            catch (RuntimeException e) {
                //bypass device specific error... the nature and impact has been investigated and
                //won't affect current flow therefore safe to bypass
            }
            startTime = System.currentTimeMillis();
            this.outputFileName = outputFileName;
            recording = true;
            if (eventListener != null) {
                eventListener.onStart();
            }
        } catch (IOException | RuntimeException e) {
            throw new ApplicationException(e);
        }
    }

    public File stopRecording() {
        if (recording) {
            try {
                myAudioRecorder.stop();
            } catch (RuntimeException e) {
//                e.printStackTrace();
//                showInfo("Failed to stop recording:" + e.getMessage());
            }
            myAudioRecorder.release();
            recording = false;
            Date date = new Date(System.currentTimeMillis() - startTime);
            String time = getTimeStampFromDate(date);
            File file = new File(outputFileName);
            String fileLength = file.length() / 1024 + "KB";
            ArrayList<String> data = new ArrayList<>();
            data.add(fileLength);
            data.add(time);
//            playlistData.put(outputFile, data);
            showInfo("Disconnected, Length: " + data.get(1) + ", size: " + data.get(0));
            if (eventListener != null) {
                eventListener.onStop(file);
            }
            return file;
        }
        return null;
    }

    public RecordingEventListener getEventListener() {
        return eventListener;
    }

    public String getOutputFilename() {
        return outputFileName;
    }


    public void showInfo(String text) {
        try {
            Toast.makeText(context, text, Toast.LENGTH_LONG).show();
        } catch (RuntimeException e) {
            Log.e("CAUGHT", e.getMessage());
        }
    }
}
