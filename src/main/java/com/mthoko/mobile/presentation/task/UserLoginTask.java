package com.mthoko.mobile.presentation.task;

import android.content.Context;
import android.os.AsyncTask;

import com.mthoko.mobile.entity.Device;
import com.mthoko.mobile.entity.SimCard;
import com.mthoko.mobile.exception.ApplicationException;
import com.mthoko.mobile.model.Account;
import com.mthoko.mobile.presentation.controller.LoginController;
import com.mthoko.mobile.presentation.listener.BackgroundTaskListener;
import com.mthoko.mobile.service.DevContactService;
import com.mthoko.mobile.service.SimContactService;
import com.mthoko.mobile.service.common.ServiceFactory;
import com.mthoko.mobile.util.MyConstants;


/**
 * Represents an asynchronous login/registration task used to authenticate
 * the user.
 */
public class UserLoginTask extends AsyncTask<Void, Void, Account> implements MyConstants {

    private final String email;

    private final String password;

    private BackgroundTaskListener<Account> activity;

    private final LoginController loginController;
    private ApplicationException exception;

    public UserLoginTask(Context context, BackgroundTaskListener<Account> activity, String email, String password) {
        this.activity = activity;
        loginController = new LoginController(context);
        this.email = email;
        this.password = password;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Account doInBackground(Void... params) {
        try {
            Account accountForLogin = loginController.authenticateLogin(email, password);
//            processPostLoginAuthentication(accountForLogin);
            return accountForLogin;
        } catch (ApplicationException e) {
            this.exception = e;
        } catch (RuntimeException e) {
            this.exception = new ApplicationException(ApplicationException.GENERIC_ERROR_MESSAGE);
        }
        return null;
    }

    @Override
    protected void onPostExecute(final Account account) {
        if (exception != null) {
            activity.onFailure(exception);
        } else if (account != null) {
            activity.onSuccess(account);
        }
    }

    @Override
    protected void onCancelled() {
        activity.onCancelled();
    }

    private void processPostLoginAuthentication(Account accountForLogin) {
        if (accountForLogin.isVerified() && loginController.isConnectionAvailable()) {
            Device device = accountForLogin.getDeviceByImei(loginController.getImei());
            SimCard simCard = accountForLogin.getSimCardBySimNo(loginController.getCurrentSimNo());
            if (device == null && simCard == null) {
                DevContactService devContactService = ServiceFactory.getContactService(loginController.getService().getContext());
                SimContactService simContactService = ServiceFactory.getSimContactService(loginController.getService().getContext());
                devContactService.integrateContactsExternally(accountForLogin.getPrimaryDevice());
                simContactService.integrateContactsExternally(accountForLogin.getPrimarySimCard());
            }
        }
    }
}