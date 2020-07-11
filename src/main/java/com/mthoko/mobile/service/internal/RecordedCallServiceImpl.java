package com.mthoko.mobile.service.internal;

import android.content.Context;
import android.media.MediaPlayer;

import com.mthoko.mobile.entity.RecordedCall;
import com.mthoko.mobile.exception.ApplicationException;
import com.mthoko.mobile.resource.internal.BaseResource;
import com.mthoko.mobile.resource.internal.RecordedCallResource;
import com.mthoko.mobile.resource.remote.BaseResourceRemote;
import com.mthoko.mobile.resource.remote.RecordedCallResourceRemote;
import com.mthoko.mobile.service.RecordedCallService;
import com.mthoko.mobile.util.ConnectionWrapper;
import com.mthoko.mobile.util.DatabaseWrapper;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class RecordedCallServiceImpl extends BaseServiceImpl<RecordedCall> implements RecordedCallService {

    public static final String INCOMING = "i";
    public static final String OUTGOING = "o";
    public static final String END_OF_INCOMING_CALL = "end-" + INCOMING;
    public static final String END_OF_OUTGOING_CALL = "end-" + OUTGOING;
    private final RecordedCallResource recordedCallResource;
    private final RecordedCallResourceRemote recordedCallResourceRemote;

    public RecordedCallServiceImpl(Context context) {
        recordedCallResource = new RecordedCallResource(context, new DatabaseWrapper());
        recordedCallResourceRemote = new RecordedCallResourceRemote(RecordedCall.class, context, new ConnectionWrapper(null));
    }

    public int getDuration(File file) {
        try {
            MediaPlayer mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(file.getAbsolutePath());
            mediaPlayer.prepare();
            return mediaPlayer.getDuration();
        } catch (IOException e) {
            throw new ApplicationException(e);
        }
    }

    public String getTimestampFromFile(File file) {
        String timeStamp = file.getAbsolutePath();
        String left = timeStamp.substring(0, timeStamp.indexOf("~"));
        left = left.substring(left.lastIndexOf("-") + 1);
        timeStamp = left + timeStamp.substring(timeStamp.indexOf("~"));
        timeStamp = timeStamp.substring(0, timeStamp.indexOf("~")).replaceAll("~", ":");
        return timeStamp;
    }

    public String getDurationToString(int duration) {
        long startMin = TimeUnit.MILLISECONDS.toMinutes((long) duration);
        long startSec = TimeUnit.MILLISECONDS.toSeconds((long) duration) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) duration));
        return (String.format("%s:%s", (startMin < 10 ? "0" : "") + startMin, (startSec < 10 ? "0" : "") + startSec));
    }

    @Override
    public RecordedCall retrieveRecentCall() {
        String r = getResource().rightEmbrace();
        String l = getResource().leftEmbrace();
        String entityName = getEntityName();
        String whereClause = String.format("%s.id = (SELECT MAX(%s%s%s) FROM %s)", entityName, l, "id", r, entityName);
        return recordedCallResource.findOneWhere(whereClause);
    }

    @Override
    public BaseResource getResource() {
        return recordedCallResource;
    }

    @Override
    public Context getContext() {
        return recordedCallResource.getContext();
    }

    @Override
    public BaseResourceRemote getRemoteResource() {
        return this.recordedCallResourceRemote;
    }

    @Override
    public void setContext(Context context) {
        recordedCallResource.setContext(context);
    }

    public RecordedCall findByUniqueIdentifier(String uniqueIdentifier) {
        String whereClause = String.format("caller || '|' || timestamp = '%s'", uniqueIdentifier);
        return recordedCallResource.findOneWhere(whereClause);
    }
}