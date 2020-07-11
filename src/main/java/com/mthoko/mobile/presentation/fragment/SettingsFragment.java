package com.mthoko.mobile.presentation.fragment;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;

import com.mthoko.mobile.R;
import com.mthoko.mobile.presentation.activity.MyAdapter;
import com.mthoko.mobile.presentation.controller.BaseMediaEventListener;
import com.mthoko.mobile.presentation.controller.RecordingEventListener;
import com.mthoko.mobile.presentation.listener.MenuItemSelector;
import com.mthoko.mobile.service.LocationStampService;
import com.mthoko.mobile.service.common.LocationService;
import com.mthoko.mobile.service.common.ServiceFactory;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class SettingsFragment extends BaseFragment<SettingsFragment> {

    private static final int LOCATIONS_THRESHOLD = 10;
    private static LocationService locationService;
    private static LocationStampService locationStampService;
    public static final String STOP_TRACING = "Stop tracing";
    public static final String STOP_PLAYING = "Stop playing";
    private static final int READ_REQUEST_CODE = 42;

    private Button record, stop, play;
    private final CustomMediaEventListener listener;
    private LocationListener locationListener;
    private final LinkedList<Location> locations;
    private WebView webView;

    public SettingsFragment() {
        listener = new CustomMediaEventListener();
        locations = new LinkedList<>();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initFields(view);
        bindUIEvents(view.getContext());
    }

    private void initFields(View view) {
        recyclerView = view.findViewById(R.id.recyclerView);
        webView = view.findViewById(R.id.webView);
        if (locationService == null) {
            locationService = ServiceFactory.getLocationService(view.getContext());
            locationStampService = ServiceFactory.getLocationStampService(view.getContext());
        }
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                handleReceivedLocation(location);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {
                //showMessage("STATUS CHANGED: " + s);
            }

            @Override
            public void onProviderEnabled(String s) {
                showMessage("PROVIDER ENABLED: " + s);
            }

            @Override
            public void onProviderDisabled(String s) {
                showMessage("PROVIDER ENABLED: " + s);
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                getContext().startActivity(intent);
            }
        };

        //set adapter
        adapter = new MyAdapter(listItems, view.getContext(), R.menu.recorded_call_menu, new MenuItemSelector() {
            @Override
            public void onItemSelect(MenuItem item, int position) {

            }
        });
        record = view.findViewById(R.id.startTracing);
        stop = view.findViewById(R.id.stopTracing);
        play = view.findViewById(R.id.show);
    }

    private void handleReceivedLocation(Location location) {
        final String s = (location.getLatitude() + ", " + location.getLongitude());
        locations.offer(location);
        if (locations.size() == LOCATIONS_THRESHOLD) {
            new Thread() {
                @Override
                public void run() {
                    try {
                        Looper.prepare();
                        saveLocations(locations);
                        locations.clear();
                        showMessage("LOCATIONS SAVED: " + s);
                    }
                    catch (Exception e) {
                        showMessage("Exception caught: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            }.start();
        } else {
            showMessage("LOCATION CHANGED: " + s);
            String latLong = String.format("%s, %s", location.getLatitude(), location.getLongitude());
            String apiKey = "YVT9TNokVXCI5A7TaC88zcTYEzwLVz5A";
            String url = "http://open.mapquestapi.com/geocoding/v1/reverse?key=" + apiKey + "&location=" + latLong + "&includeRoadMetadata=true&includeNearestIntersection=true";
            webView.loadUrl(url);
        }
    }

    private void saveLocations(List<Location> locations) {
        locationStampService.updateMostRecentLatLongsByImei(locationStampService.getImei(), locations);
    }

    private void bindUIEvents(Context context) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        locationService.setContext(context);
        locationService.initLocationRequests(locationListener);
        stop.setEnabled(false);
        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onStart();
            }
        });
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onStop(null);
            }
        });
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMessage("Play");
            }
        });
    }


    class CustomMediaEventListener implements RecordingEventListener {

        @Override
        public void onStart() {
            record.setEnabled(false);
            play.setEnabled(false);
            stop.setEnabled(true);
            locationService.toggleLocationRequest();
            stop.setText(STOP_TRACING);
        }

        @Override
        public void onStop(File file) {
            stop.setEnabled(false);
            record.setEnabled(true);
            play.setEnabled(true);
            locationService.toggleLocationRequest();
            stop.setText("STOP TRACING");
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
