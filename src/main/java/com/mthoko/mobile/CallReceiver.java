package com.mthoko.mobile;

import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.mthoko.mobile.service.RecordedCallService;
import com.mthoko.mobile.service.common.RecordingService;
import com.mthoko.mobile.service.common.ServiceFactory;

import java.util.Date;

import static com.mthoko.mobile.service.internal.RecordedCallServiceImpl.INCOMING;
import static com.mthoko.mobile.service.internal.RecordedCallServiceImpl.OUTGOING;

public class CallReceiver extends PhoneCallReceiver {

    private RecordedCallService callService;

    @Override
    protected void setContext(Context context) {
        super.setContext(context);
        if (callService == null) {
            callService = ServiceFactory.getRecordedCallService(context);
        } else {
            callService.setContext(context);
        }
    }

    @Override
    protected void onIncomingCallReceived(Context context, String number, Date start) {
        callService.showNotification("Incoming call from: " + number);
        createNewCall(number, start, INCOMING);
    }

    @Override
    protected void onIncomingCallAnswered(Context context, String number, Date start) {
        callService.showNotification("IncomingCallAnswered");
    }

    @Override
    protected void onIncomingCallEnded(Context context, String number, Date start, Date end) {
        callService.showNotification("IncomingCallEnded");
        handleEndOfCall();
    }

    @Override
    protected void onOutgoingCallStarted(Context context, String number, Date start) {
        callService.showNotification("Outgoing call to: " + number);
        createNewCall(number, start, OUTGOING);
    }

    @Override
    protected void onOutgoingCallEnded(Context context, String number, Date start, Date end) {
        callService.showNotification("OutgoingCallEnded");
        handleEndOfCall();
    }


    @Override
    protected void onMissedCall(Context context, String number, Date start) {
        callService.showNotification("MissedCall");
        handleEndOfCall();
    }

    @Override
    protected void onExtraState(int state) {
        callService.showNotification("EXTRA STATE: " + state);
    }

    private void createNewCall(String number, Date start, String category) {
        RecordingService instance = RecordingService.getInstance();
        Intent recordingIntent = new Intent(context, RecordingService.class);
        recordingIntent.putExtra("category", category);
        recordingIntent.putExtra("number", number);
        recordingIntent.putExtra("date", start.getTime());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(recordingIntent);
        } else {
            context.startService(recordingIntent);
        }


        getContext().startService(recordingIntent);
    }

    private void handleEndOfCall() {
        RecordingService recordingService = RecordingService.getInstance();
        if (recordingService != null) {
            recordingService.endCurrentCall();
        }
        else {
            callService.showNotification("Could not finalize call");
        }
    }
}
