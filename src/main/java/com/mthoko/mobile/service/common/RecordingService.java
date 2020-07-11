package com.mthoko.mobile.service.common;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Environment;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.mthoko.mobile.entity.FileInfo;
import com.mthoko.mobile.entity.RecordedCall;
import com.mthoko.mobile.exception.ApplicationException;
import com.mthoko.mobile.presentation.controller.BaseMediaEventListener;
import com.mthoko.mobile.presentation.controller.RecordingEventListener;
import com.mthoko.mobile.presentation.listener.BackgroundTaskListener;
import com.mthoko.mobile.presentation.task.FileUploadTask;
import com.mthoko.mobile.service.FTPService;
import com.mthoko.mobile.service.RecordedCallService;

import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * Created by mthokozisi_mhlanga on 22 Apr 2018.
 */

public class RecordingService extends Service {

    public static final String RECORDING_DIRECTORY = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Alarms";
    public static final String EXTENSION = ".amr";
    private static RecordingService INSTANCE;

    public Intent intent;
    private String owner;
    private Context context;
    private Recorder recorder;
    private BaseMediaEventListener mediaEventListener;
    private RecordedCallService callService;
    private FTPService ftpService;
    private FileInfo currentFileInfo;
    private RecordedCall currentCall;

    public RecordingService() {
        File recordingDir = new File(RECORDING_DIRECTORY);
        if (!recordingDir.exists()) {
            recordingDir.mkdir();
        }
        INSTANCE = this;
    }

    public static RecordingService getInstance() {
        return INSTANCE;
    }

    public static String getNewFilename() {
        String outputFile = RECORDING_DIRECTORY + "/recording" + EXTENSION;
        int i = 1;
        while (new File(outputFile).exists()) {
            outputFile = RECORDING_DIRECTORY + "/recording-" + (i++) + EXTENSION;
        }
        return outputFile;
    }

    public static String getRecentFilename() {
        String outputFile = RECORDING_DIRECTORY + "/recording" + EXTENSION;
        if (new File(outputFile).exists()) {
            String recent;
            int i = 1;
            do {
                recent = outputFile;
                outputFile = RECORDING_DIRECTORY + "/recording-" + (i++) + EXTENSION;
            } while (new File(outputFile).exists());
            return recent;
        }
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        startForeground(1, new Notification());
    }

    public String currentFilename = null;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        init(intent);
//        handleDefaultStartCommand(intent);
        try {
            handleStartCommand();
        } catch (ApplicationException e) {
            e.printStackTrace();
            showInfo("START ERROR: " + e.getMessage());
        }
        return Service.START_STICKY;
    }

    private void init(Intent intent) {
        INSTANCE = this;
        this.context = this;
        this.intent = intent;
        this.callService = ServiceFactory.getRecordedCallService(getContext());
        this.ftpService = ServiceFactory.getFtpService(getContext());
        this.currentCall = getNewCall();
        this.currentFileInfo = getNewFileInfo();
        this.recorder = new Recorder(getContext(), new CallRecordingEventListener());
    }

    private void handleStartCommand() {
        if (recorder.isRecording()) {
            return;
        }
        recorder.startRecording(currentFileInfo.absolutePath());
    }

    private FileInfo getNewFileInfo() {
        String category = intent.getStringExtra("category");
        String fileName = category + "-" + currentCall.getUniqueIdentifier() + EXTENSION;
        FileInfo fileInfo = new FileInfo();
        fileInfo.setLocalDirectory(RECORDING_DIRECTORY);
        fileInfo.setFileName(fileName);
        fileInfo.setOwnerImei(ftpService.getImei());
        fileInfo.setDateCreated(new Date());
        return fileInfo;
    }

    private RecordedCall getNewCall() {
        RecordedCall currentCall = new RecordedCall();
        currentCall.setTimestamp(new Date(intent.getLongExtra("date", new Date().getTime())));
        currentCall.setCategory(intent.getStringExtra("category"));
        currentCall.setContactName(intent.getStringExtra("number"));
        currentCall.setCaller(intent.getStringExtra("number"));
        currentCall.setReceiverImei(callService.getImei());
        currentCall.setReceiverSimNo(callService.getCurrentSimNo());
        return currentCall;
    }

    @Override
    public void onDestroy() {
        INSTANCE = null;
        shutdown();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        showInfo("Binding intent: " + intent.getAction());
        return null;
    }

    public void shutdown() {
        if (recorder.isRecording()) {
            try {
                recorder.stopRecording();
            } catch (RuntimeException e) {
                e.printStackTrace();
                ServiceFactory.getRecordedCallService(getContext()).showNotification("Error while ending call: " + e.getMessage());
            }
        }
    }

    public void runFileUploadTask(final File file, final RecordedCall currentCall) {
        new FileUploadTask(file, getContext()).execute(new BackgroundTaskListener<String>() {

            @Override
            public void onStart() {
                showInfo("Uploading file: " + file.getName());
            }

            @Override
            public void onFailure(ApplicationException exception) {
                showInfo("Upload failed: " + exception);
                getContext().stopService(intent);
            }

            @Override
            public void onSuccess(String info) {
                showInfo("file uploaded successfully: " + info);
                FTPService ftpService = ServiceFactory.getFtpService(getContext());
                String remoteBaseDirectory = ftpService.getAppProperty(ftpService.getAppProperty("ftp.base_url"));
                String callsDirectory = ftpService.getAppProperty("ftp.calls_directory");
                currentFileInfo.setRemoteDirectory(remoteBaseDirectory + callsDirectory);
                ftpService.update(currentFileInfo);
                getContext().stopService(intent);
            }

            @Override
            public void onCancelled() {
            }
        });
    }

    public int getDuration(File file) {
        try {
            MediaPlayer mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(file.getAbsolutePath());
            mediaPlayer.prepare();
            int duration = mediaPlayer.getDuration();
            mediaPlayer.release();
            return duration;
        } catch (IOException e) {
            throw new ApplicationException(e);
        }
    }

    public void validateInputFilename(String inputFilename) {
        if (inputFilename == null || !new File(inputFilename).exists()) {
            throw new ApplicationException("cannot play : file '" + inputFilename + "' not found");
        } else if (recorder.isRecording()) {
            throw new ApplicationException("cannot play : recording in progress");
        }
    }

    public void showInfo(String text) {
        try {
            try {
                Looper.prepare();
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
            Toast.makeText(this, text, Toast.LENGTH_LONG).show();
        } catch (RuntimeException e) {
            e.printStackTrace();
            Log.e("CAUGHT", e.getMessage());
        }
    }

// mutators and accessors

    public String getOutputFilename() {
        return recorder.getOutputFilename();
    }

    public static int getRecordingDuration(File recording) {
        try {
            MediaPlayer mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(recording.getAbsolutePath());
            mediaPlayer.prepare();
            return mediaPlayer.getDuration();
        } catch (IOException e) {
            throw new ApplicationException(e);
        }
    }

    public Context getContext() {
        return this.context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public void endCurrentCall() {
        recorder.stopRecording();
    }

    private class CallRecordingEventListener implements RecordingEventListener {
        @Override
        public void onStart() {
            showInfo("recording service created");
            ftpService.save(currentFileInfo);
            currentCall.setFileInfoId(currentFileInfo.getId());
            callService.save(currentCall);
        }

        @Override
        public void onPause() {

        }

        @Override
        public void onStop(File file) {
            RecordedCallService callService = ServiceFactory.getRecordedCallService(getContext());
            if (currentCall != null) {
                currentCall.setDuration(getDuration(file));
                callService.update(currentCall);
                runFileUploadTask(file, currentCall);
            }
        }

        @Override
        public void onResume() {

        }

        @Override
        public void onProgress() {

        }
    }
}
