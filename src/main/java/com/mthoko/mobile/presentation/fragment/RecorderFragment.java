package com.mthoko.mobile.presentation.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.mthoko.mobile.R;
import com.mthoko.mobile.entity.FileInfo;
import com.mthoko.mobile.exception.ApplicationException;
import com.mthoko.mobile.presentation.controller.AudioPlayer;
import com.mthoko.mobile.presentation.controller.BaseMediaEventListener;
import com.mthoko.mobile.presentation.controller.RecordingEventListener;
import com.mthoko.mobile.service.FTPService;
import com.mthoko.mobile.service.common.FileManagementService;
import com.mthoko.mobile.service.common.Recorder;
import com.mthoko.mobile.service.common.RecordingService;
import com.mthoko.mobile.service.common.ServiceFactory;

import java.io.File;
import java.util.Date;

import static com.mthoko.mobile.exception.ApplicationException.GENERIC_ERROR_MESSAGE;

public class RecorderFragment extends BaseFragment<RecorderFragment> {

    public static final String STOP_RECORDING = "Stop recording";
    public static final String STOP_PLAYING = "Stop playing";

    private static final int READ_REQUEST_CODE = 42;
    private Button record, stop, play;
    private Recorder recorder;
    private AudioPlayer player;
    private File recording;
    private FTPService ftpService;

    public RecorderFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void performSearch() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("audio/*");
        startActivityForResult(intent, READ_REQUEST_CODE);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initFields(view);
        bindUIEvents();
        //performSearch();
    }

    private void initFields(View view) {
        record = view.findViewById(R.id.record);
        stop = view.findViewById(R.id.stop);
        play = view.findViewById(R.id.play);
        recorder = new Recorder(view.getContext(), new CustomMediaEventListener());
        player = new AudioPlayer(view.getContext(), playerListener);
        ftpService = ServiceFactory.getFtpService(view.getContext());
    }

    private void bindUIEvents() {
        stop.setEnabled(false);
        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    recorder.startRecording(null);
                } catch (ApplicationException e) {
                    recorder.showInfo("Error while starting recording: " + e.getMessage());
                }
            }
        });
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (recorder.isRecording()) {
                        recording = recorder.stopRecording();
                        saveRecentRecording();
                    } else if (!player.isStopped()) {
                        player.stop();
                    }
                } catch (ApplicationException e) {
                    handleException(e);
                }
            }
        });

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (recording == null) {
                        showMessage("No file specified");
                    } else if (!recording.exists()) {
                        showMessage("The file specified was not found");
                    } else {
                        player.play(recording);
                    }
                } catch (ApplicationException e) {
                    handleException(e);
                }
            }
        });
    }

    public void saveRecentRecording() {
        saveRecording(this.recording);
    }

    private void saveRecording(File recording) {
        FileInfo fileInfo = new FileInfo();
        fileInfo.setDateCreated(new Date());
        fileInfo.setOwnerImei(ftpService.getImei());
        fileInfo.setLocalDirectory(recording.getParent());
        fileInfo.setFileName(recording.getName());
        ftpService.save(fileInfo);
    }

    private void handleException(ApplicationException e) {
        ServiceFactory.getRecordingService(getContext()).showInfo(getMessage(e));
    }

    private String getMessage(ApplicationException e) {
        return e.getMessage() != null ? e.getMessage() : GENERIC_ERROR_MESSAGE;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (resultData != null) {
                Uri uri = resultData.getData();
                showMessage("Uri: " + uri.toString());
                String path = uri.getPath();
                path = path.substring(path.indexOf(":") + 1);
                path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + path.substring(path.indexOf(":") + 1);
                File f = new File(path);
                play.setText("old path: " + f.getAbsolutePath());
                if (f.exists()) {
                    try {
                        copy(path, f);
                    } catch (ApplicationException e) {
                        play.setText("An error occurred: " + f.exists() + " - " + path);
                    }
                } else {
                    play.setText("Path exists: " + f.exists() + " - " + path);
                }
            }
        }
    }

    private void copy(String path, File f) {
        String name = path.substring(path.lastIndexOf('/'));
        String newDir = getContext().getFilesDir() + name;
        //play.setText("new path: "+newDir);
        File newFile = new File(newDir);
        boolean renamed = f.renameTo(newFile);
        new FileManagementService(getContext()).copyFileUsingStream(new File(path), newFile);
        if (newFile.exists()) {
            if (newFile.length() == f.length()) {
                //play.setText(" moved:"+(newFile.exists() && f.delete()));
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recorder, container, false);
    }

    class CustomMediaEventListener implements RecordingEventListener {

        @Override
        public void onStart() {
            String outputFile = RecordingService.getNewFilename();
            showMessage("Overwritten:" + new File(outputFile).exists());
            record.setEnabled(false);
            play.setEnabled(false);
            stop.setEnabled(true);
            stop.setText(STOP_RECORDING);
        }

        @Override
        public void onStop(File file) {
            stop.setEnabled(false);
            record.setEnabled(true);
            play.setEnabled(true);
            stop.setText("STOP");
        }

        @Override
        public void onPause() {

        }

        @Override
        public void onResume() {

        }

        @Override
        public void onProgress() {

        }

    }

    private BaseMediaEventListener playerListener = new BaseMediaEventListener() {
        @Override
        public void onPlay() {
            play.setEnabled(false);
            record.setEnabled(false);
            stop.setEnabled(true);
            stop.setText(STOP_PLAYING);
            showMessage("Playing audio");
        }

        @Override
        public void onPause() {

        }

        @Override
        public void onStop() {
            stop.setEnabled(false);
            record.setEnabled(true);
            play.setEnabled(true);
            stop.setText("STOP");
            showMessage("Playback stopped");
        }

        @Override
        public void onResume() {

        }

        @Override
        public void onProgress() {

        }

        @Override
        public void onRewind() {

        }

        @Override
        public void onForward() {

        }
    };

    public void showMessage(String text) {
        Toast.makeText(getContext(), text, Toast.LENGTH_LONG).show();
    }

}
