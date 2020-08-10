package com.mthoko.mobile.service.internal;

import java.io.File;
import java.util.concurrent.TimeUnit;

import com.mthoko.mobile.entity.RecordedCall;
import com.mthoko.mobile.resource.remote.BaseResourceRemote;
import com.mthoko.mobile.resource.remote.RecordedCallResourceRemote;
import com.mthoko.mobile.service.RecordedCallService;
import com.mthoko.mobile.util.ConnectionWrapper;

public class RecordedCallServiceImpl extends BaseServiceImpl implements RecordedCallService {

	public static final String INCOMING = "i";
	public static final String OUTGOING = "o";
	public static final String END_OF_INCOMING_CALL = "end-" + INCOMING;
	public static final String END_OF_OUTGOING_CALL = "end-" + OUTGOING;
	private final RecordedCallResourceRemote recordedCallResourceRemote;

	public RecordedCallServiceImpl() {
		recordedCallResourceRemote = new RecordedCallResourceRemote(RecordedCall.class, new ConnectionWrapper(null));
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
		long startSec = TimeUnit.MILLISECONDS.toSeconds((long) duration)
				- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) duration));
		return (String.format("%s:%s", (startMin < 10 ? "0" : "") + startMin, (startSec < 10 ? "0" : "") + startSec));
	}

	@Override
	public BaseResourceRemote<RecordedCall> getResource() {
		return this.recordedCallResourceRemote;
	}

}