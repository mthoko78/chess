package com.mthoko.mobile.presentation.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mthoko.mobile.R;
import com.mthoko.mobile.entity.FileInfo;
import com.mthoko.mobile.entity.RecordedCall;
import com.mthoko.mobile.presentation.activity.DrawerActivity;
import com.mthoko.mobile.presentation.activity.MyAdapter;
import com.mthoko.mobile.presentation.controller.MediaPlayerController;
import com.mthoko.mobile.presentation.listener.MenuItemSelector;
import com.mthoko.mobile.service.FTPService;
import com.mthoko.mobile.service.RecordedCallService;
import com.mthoko.mobile.service.common.ServiceFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class RecordedCallsFragment extends BaseFragment<RecordedCallsFragment> {

    private final List<String> fileNames = new ArrayList<>();

    private MediaPlayerController mediaController;

    public RecordedCallsFragment() {
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initComponents(view);
        loadRecordedCallsList();
    }

    public void initComponents(View view) {
        recyclerView = view.findViewById(R.id.recyclerView);
        adapter = new MyAdapter(listItems, view.getContext(), R.menu.recorded_call_menu, new MenuItemSelector() {
            @Override
            public void onItemSelect(MenuItem item, int position) {
                switch (item.getItemId()) {
                    case R.id.mnu_item_play:
                        playItemAt(position);
                        break;
                    case R.id.mnu_item_delete:
                        adapter.deleteItemAt(position);
                        break;
                    default:
                        break;
                }
            }
        });
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        mediaController = new MediaPlayerController(
                (TextView) view.findViewById(R.id.startTime),
                (TextView) view.findViewById(R.id.endTime),
                (SeekBar) view.findViewById(R.id.seekBar1),
                (ImageButton) view.findViewById(R.id.playPauseButton),
                (ImageButton) view.findViewById(R.id.stopButton),
                (ImageButton) view.findViewById(R.id.forwardButton),
                (ImageButton) view.findViewById(R.id.rewindButton)
        );
        setHasOptionsMenu(true);
        setMenuVisibility(true);
    }

    private void loadRecordedCallsList() {
        RecordedCallService recordedCallService = ServiceFactory.getRecordedCallService(this.getContext());
        FTPService ftpService = ServiceFactory.getFtpService(this.getContext());
        List<RecordedCall> recordedCalls = recordedCallService.retrieveAll();
        List<Long> ids = new ArrayList<>();
        for (RecordedCall call : recordedCalls) {
            ids.add(call.getFileInfoId());
        }
        List<FileInfo> fileInfoList = ftpService.findByIdsIn(ids);
        fileNames.clear();
        for (int i = fileInfoList.size() - 1; i >= 0; i--) {
            RecordedCall call = recordedCalls.get(i);
            FileInfo fileInfo = fileInfoList.get(i);
            addItem(call.getContactName(), call.getTimestamp() + " " + recordedCallService.getDurationToString(call.getDuration()), call.getId(), false);
            fileNames.add(fileInfo.absolutePath());
        }
        refresh();
    }

    public void playItemAt(int position) {
        File file = new File(fileNames.get(position));
        Toast.makeText(adapter.getContext(), "Playing: " + file.getName() + " " + file.exists(), Toast.LENGTH_LONG).show();
        getMediaController().play(file);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        //super.onCreateOptionsMenu(menu, menuInflater);
        menuInflater.inflate(R.menu.calls_filter_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getTitle().toString()) {
            case "Received":
                DrawerActivity.INSTANCE.getSupportActionBar().setTitle("Received calls");
                break;
            case "Missed":
                DrawerActivity.INSTANCE.getSupportActionBar().setTitle("Missed calls");
                break;
            case "Dialed":
                DrawerActivity.INSTANCE.getSupportActionBar().setTitle("Dialed Calls");
                break;
        }
        return true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recorded_calls, container, false);
    }

    public MediaPlayerController getMediaController() {
        return mediaController;
    }

    public void setMediaController(MediaPlayerController mediaController) {
        this.mediaController = mediaController;
    }
}
