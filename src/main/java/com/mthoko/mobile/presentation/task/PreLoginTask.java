package com.mthoko.mobile.presentation.task;

import android.content.Context;
import android.os.AsyncTask;

import com.mthoko.mobile.exception.ApplicationException;
import com.mthoko.mobile.model.Account;
import com.mthoko.mobile.presentation.controller.LoginController;
import com.mthoko.mobile.presentation.listener.BackgroundTaskListener;

public class PreLoginTask extends AsyncTask<Object, Object, Account> {

    private final LoginController loginController;
    private final BackgroundTaskListener listener;

    private ApplicationException exception;

    public PreLoginTask(Context context, BackgroundTaskListener listener) {
        this.listener = listener;
        loginController = new LoginController(context);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        listener.onStart();
    }

    @Override
    protected Account doInBackground(Object... params) {
        try {
            return loginController.processPreLogin();
        } catch (ApplicationException e) {
            this.exception = e;
            return null;
        }
    }

    @Override
    protected void onPostExecute(final Account accountForCurrSim) {
        if (exception != null) {
            listener.onFailure(exception);
        } else if (accountForCurrSim != null) {
            listener.onSuccess(accountForCurrSim);
        }
    }

    @Override
    protected void onCancelled() {
        listener.onCancelled();
    }
}