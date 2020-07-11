package com.mthoko.mobile.presentation.task;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Looper;

import com.mthoko.mobile.exception.ApplicationException;
import com.mthoko.mobile.presentation.listener.BackgroundTaskListener;
import com.mthoko.mobile.service.FTPService;
import com.mthoko.mobile.service.common.ServiceFactory;
import com.mthoko.mobile.util.MyConstants;

import java.io.File;


/**
 * Represents an asynchronous login/registration task used to authenticate
 * the user.
 */
public class FileUploadTask extends AsyncTask<BackgroundTaskListener, Void, Void> implements MyConstants {

    private final File file;
    private final Context context;
    private ApplicationException exception;

    public FileUploadTask(File file, Context context) {
        this.file = file;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(BackgroundTaskListener... params) {
        BackgroundTaskListener listener = params[0];
        if (file != null && file.exists()) {
            FTPService ftpService = ServiceFactory.getFtpService(context);
            if (ftpService.isConnectionAvailable()) {
                String callsDirectory = ftpService.getCallsRemoteDirectory();
                try {
                    String result = ftpService.uploadFile(this.file, callsDirectory);
                    listener.onSuccess(result);
                } catch (ApplicationException e) {
                    listener.onFailure(e);
                }
            } else {
                listener.onFailure(new ApplicationException("Unable to connect to the internet"));
            }
        } else {
            listener.onFailure(new ApplicationException("File not found"));
        }
        return null;
    }

    @Override
    protected void onPostExecute(final Void v) {
    }

    @Override
    protected void onCancelled() {
    }
}