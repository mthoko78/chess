package com.mthoko.mobile.presentation.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.mthoko.mobile.R;
import com.mthoko.mobile.exception.ApplicationException;
import com.mthoko.mobile.exception.ErrorCode;
import com.mthoko.mobile.model.Account;
import com.mthoko.mobile.presentation.controller.LoginController;
import com.mthoko.mobile.presentation.listener.BackgroundTaskListener;
import com.mthoko.mobile.presentation.task.PreLoginTask;
import com.mthoko.mobile.presentation.task.UserLoginTask;
import com.mthoko.mobile.resource.internal.MyResources;
import com.mthoko.mobile.util.MyConstants;

import java.util.ArrayList;
import java.util.regex.Pattern;

import static com.mthoko.mobile.exception.ApplicationException.GENERIC_ERROR_MESSAGE;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BackgroundTaskRunner implements MyConstants {

    /**
     * Id to identity READ_CONTACTS permission request.
     */

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private LoginController loginController;
    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    private boolean ALL_PERMISSIONS_GRANTED = true;
    private Button mEmailSignInButton;
    private Button mEmailRegisterButton;

    private BackgroundTaskListener<Account> preLoginListener = new BackgroundTaskListener<Account>() {
        @Override
        public void onStart() {
            showProgress(true);
        }

        @Override
        public void onFailure(ApplicationException exception) {
            showProgress(false);
            showMessage("Failure", handlePreLoginException(exception));
        }

        @Override
        public void onSuccess(Account account) {
            setEmail(account.getEmail());
            setPassword(account.getPassword());
            showProgress(false);
        }

        @Override
        public void onCancelled() {
            showProgress(false);
            showMessage("Warning", "Activity interrupted");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginController = new LoginController(getApplicationContext());
        setContentView(R.layout.activity_login);
        permissionsGranted(PERMISSIONS, CODE_ALL_PERMISSIONS);
    }

    public boolean permissionsGranted(String[] permissions, int permissionCode) {
        ArrayList<String> deniedPermissions = new ArrayList<>();
        for (int i = 0; i < permissions.length; i++)
            if (ActivityCompat.checkSelfPermission(this, permissions[i]) != PackageManager.PERMISSION_GRANTED)
                deniedPermissions.add(permissions[i]);
        if (!deniedPermissions.isEmpty()) {
            String[] permissionsToRequest = new String[deniedPermissions.size()];
            for (int i = 0; i < permissionsToRequest.length; i++)
                permissionsToRequest[i] = deniedPermissions.get(i);
            ActivityCompat.requestPermissions(this, permissionsToRequest, permissionCode);
        } else {
            init();
            return true;
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        ALL_PERMISSIONS_GRANTED = true;
        ArrayList<String> deniedPermissions = new ArrayList<>();
        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                if (ALL_PERMISSIONS_GRANTED)
                    ALL_PERMISSIONS_GRANTED = false;
                deniedPermissions.add(permissions[i]);
            }
        }

        if (ALL_PERMISSIONS_GRANTED) {
            init();
        } else {
            showMessage(
                    "Permissions needed", "Some PERMISSIONS have been denied, app cannot run" +
                            " without these PERMISSIONS please re-run the app and grant the required PERMISSIONS: " + deniedPermissions
            );
        }
    }

    public void init() {
        MyResources.getInstance(this.getApplicationContext());
        // Set up the login form.
        mEmailView = findViewById(R.id.email);
        mPasswordView = findViewById(R.id.password);
        mEmailSignInButton = findViewById(R.id.email_sign_in_button);
        mEmailRegisterButton = findViewById(R.id.email_register_button);
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        bindEvents();
        startPreLoginProcess();
    }

    private void bindEvents() {
        mPasswordView.setOnEditorActionListener(
                new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                        if (id == R.id.login || id == EditorInfo.IME_NULL) {
                            attemptLogin();
                            return true;
                        }
                        return false;
                    }
                }
        );
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
        mEmailRegisterButton.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        attemptRegister();
                    }
                }
        );
    }

    /**
     * Attempts to sign in or register the accountForCurrSim specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }
        String email = getEmail();
        String password = getPassword();
        try {
            validateInputsFormat(email, password);
            new UserLoginTask(this, new BackgroundTaskListener<Account>() {
                @Override
                public void onStart() {
                    showProgress(true);
                }

                @Override
                public void onFailure(ApplicationException exception) {
                    handleLoginFailure(exception);
                    showProgress(false);
                }

                @Override
                public void onSuccess(Account account) {
                    processFoundAccount(account);
                    setTask(null);
                    showProgress(false);
                }

                @Override
                public void onCancelled() {
                    setTask(null);
                    showProgress(false);
                }
            }, email, password).execute((Void) null);
        } catch (ApplicationException e) {
            EditText invalidView;
            if (e.getErrorCode().equals(ErrorCode.INVALID_EMAIL_FORMAT)) {
                invalidView = mEmailView;
            } else {
                invalidView = mPasswordView;
            }
            invalidView.setError(e.getMessage());
            invalidView.requestFocus();
        }
    }

    public String getEmail() {
        return mEmailView.getText().toString();
    }

    private void validateInputsFormat(String email, String password) {
        clearErrorMessages();
        validatePasswordFormat(password);
        validateEmailFormat(email);
    }

    private void clearErrorMessages() {
        mEmailView.setError(null);
        mPasswordView.setError(null);
    }

    private void validateEmailFormat(String email) {
        String errorMessage = null;
        if (TextUtils.isEmpty(email)) {
            errorMessage = getString(R.string.error_field_required);
        } else if (!isEmailValid(email)) {
            errorMessage = getString(R.string.error_invalid_email);
        }
        if (errorMessage != null) {
            throw new ApplicationException(errorMessage, ErrorCode.INVALID_EMAIL_FORMAT);
        }
    }

    private void validatePasswordFormat(String password) {
        String errorMessage = null;
        if (TextUtils.isEmpty(password)) {
            errorMessage = getString(R.string.error_field_required);
        } else if (password.length() < 4) {
            errorMessage = getString(R.string.error_invalid_password);
        } else if (!isPasswordValid(password)) {
            errorMessage = getString(R.string.error_invalid_password_format);
        }
        if (errorMessage != null) {
            throw new ApplicationException(errorMessage, ErrorCode.INVALID_PASSWORD_FORMAT);
        }
    }

    private void attemptRegister() {
        Account accountForCurrSim = loginController.findAccountBySimNo(loginController.getCurrentSimNo());
        if (accountForCurrSim != null) {
            showMessage("Registration not allowed", "The sim card detected has been registered in this device, you can only use login option");
            return;
        }
        startActivity(new Intent(this, RegisterActivity.class));
    }

    private void startPreLoginProcess() {
        String lastBoot = "" + loginController.getService().getProperty("lastBoot");
        showMessage("Last booted", lastBoot);
        String currentSimNo = loginController.getCurrentSimNo();
        if (currentSimNo == null) {
            super.showMessage("No Sim Card Detected", "Please insert sim card");
        } else {
            runTask(new PreLoginTask(this, preLoginListener), (Void) null);
        }
    }

    private boolean isEmailValid(String email) {
        return Pattern.matches("(\\w+)(\\.\\w+)*@(\\w+)\\.(\\w+)(\\.(\\w+))*", email);
    }

    private boolean isPasswordValid(String password) {
        return Pattern.matches("\\w+", password);
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    public void setEmail(String email) {
        mEmailView.setText(email);
    }

    public void setPassword(String password) {
        mPasswordView.setText(password);
    }

    public void setPasswordError(String string) {
        mPasswordView.setError(string);
    }

    public void setEmailError(String string) {
        mEmailView.setError(string);
    }

    public void setTask(UserLoginTask task) {
        this.mAuthTask = task;
    }

    public void handleLoginFailure(ApplicationException e) {
        String errorMessage = null;
        if (e.getErrorCode() != null) {
            switch (e.getErrorCode()) {
                case NO_INTERNET_CONNECTION:
                    errorMessage = "Please ensure you have a stable internet connection and try again";
                    break;
                case INCORRECT_PASSWORD:
                    errorMessage = getString(R.string.error_incorrect_password);
                    setPasswordError(errorMessage);
                    break;
                case ACCOUNT_DOES_NOT_EXIST:
                    errorMessage = "No account found with that email, please type in a registered email... otherwise click 'Register' to create a new account";
                    setEmailError(getString(R.string.error_invalid_email));
                    mEmailView.requestFocus();
                    break;
                case EMAIL_AND_PHONE_ALREADY_IN_USE:
                case PHONE_ALREADY_IN_USE:
                case EMAIL_ALREADY_IN_USE:
                    String email = getEmail();
                    String supportEmail = "mthoko78@outlook.com";
                    errorMessage = "The account with that email has been found but needs attention, " +
                            "it has not been verified and the following was encountered while attempting to verify your account: " +
                            "the email or phone already belongs to a registered user. Please send an email with enquiries to " +
                            supportEmail + ". Please note that the email should be sent from your email: " +
                            email + " otherwise your query may not be resolved.";
                    break;
            }
        }
        if (errorMessage == null) {
            errorMessage = GENERIC_ERROR_MESSAGE;
            /**
             * TODO: generate error id, log this error and send email to support team
             */
        }
        showMessage("Failed to login", errorMessage);
        showProgress(false);
    }

    public void processFoundAccount(@NonNull Account account) {
        int loginStatus;
        if (account.getSimCardBySimNo(loginController.getCurrentSimNo()) != null) {
            // current sim card belongs to this account: native login
            if (account.isVerified()) {
                loginStatus = STATUS_NATIVE_SUCCESS_VERIFIED;
            } else {
                loginStatus = STATUS_NATIVE_SUCCESS_UNVERIFIED;
            }
        } else if (account.isVerified()) {
            loginStatus = STATUS_FOREIGN_SUCCESS_VERIFIED;
        } else {
            loginStatus = STATUS_FOREIGN_SUCCESS_UNVERIFIED;
        }

        if (account.getPassword().equals(getPassword())) {
            String statusDescription = loginController.processAccountStatus(account, loginController.getCurrentSimNo(), loginStatus);
            showMessage("Notification", statusDescription);
            loginController.getService().setProperty("currentMemberId", String.valueOf(account.getMember().getId()));
            showProgress(false);
            Intent intent = new Intent(this, DrawerActivity.class);
            intent.putExtra("accountId", account.getMember().getId());
            startActivity(intent);
        } else {
            showMessage("Unable to login", GENERIC_ERROR_MESSAGE);
        }
    }

    public String getPassword() {
        return mPasswordView.getText().toString();
    }


    private String handlePreLoginException(ApplicationException e) {
        e.printStackTrace();
        if (e.getMessage() != null) {
            return e.getMessage();
        } else {
            return getErrorMessageByErrorCode(e.getErrorCode());
        }
    }

    private String getErrorMessageByErrorCode(@NonNull ErrorCode errorCode) {
        if (errorCode != null) {
            switch (errorCode) {
                case NO_SIM_CARD_DETECTED:
                    return "No sim card detected. Please insert a RICA compliant sim card and restart the app.";
                case NO_INTERNET_CONNECTION:
                    return "We were unable search for an account linked to the current sim card " +
                            "due to failure to connect to the internet, if you would like to synchronize your " +
                            "information, please make sure you have reliable internet connection and restart the app.";
                case PHONE_ALREADY_IN_USE:
                case EMAIL_ALREADY_IN_USE:
                case EMAIL_AND_PHONE_ALREADY_IN_USE:
                    String supportEmail = "mthoko78@outlook.com";
                    return "An existing account with linked with current sim card has been found but needs attention, " +
                            "it has not been verified and the following was encountered while attempting to verify your account: " +
                            "the email or phone already belongs to a registered user. Please send an email with enquiries to " +
                            supportEmail + ". Please note that the email should be sent from the registered email otherwise your query may not be resolved.";
            }
        }
        return ApplicationException.GENERIC_ERROR_MESSAGE;
    }
}