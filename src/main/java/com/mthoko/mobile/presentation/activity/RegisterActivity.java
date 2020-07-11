package com.mthoko.mobile.presentation.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.mthoko.mobile.R;
import com.mthoko.mobile.exception.ApplicationException;
import com.mthoko.mobile.util.MyConstants;
import com.mthoko.mobile.presentation.controller.RegisterController;
import com.mthoko.mobile.presentation.task.UserRegistrationTask;
import com.mthoko.mobile.resource.internal.MyResources;

/**
 * A login screen that offers login via email/password.
 */
public class RegisterActivity extends BackgroundTaskRunner implements MyConstants {
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserRegistrationTask mAuthTask = null;

    // UI references.
    private EditText mEmailView;
    private EditText mNameView;
    private EditText mSurnameView;
    private EditText mPhoneView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mRegisterFormView;
    private CheckBox deviceOwner;

    private RegisterController registerController;
    private View mEmailRegisterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        MyResources.getInstance().setContext(this);
        registerController = new RegisterController(getApplicationContext());
        initUIComponents();
        bindActionEvents();
    }

    private void bindActionEvents() {
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.register_action || id == EditorInfo.IME_NULL) {
                    attemptRegister();
                    return true;
                }
                return false;
            }
        });
        mEmailRegisterButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegister();
            }
        });
    }

    private void initUIComponents() {
        mNameView = findViewById(R.id.name);
        mSurnameView = findViewById(R.id.surname);
        mPhoneView = findViewById(R.id.phone);
        mEmailView = (AutoCompleteTextView) findViewById(R.id.register_email);
        mPasswordView = findViewById(R.id.register_password);
        deviceOwner = findViewById(R.id.deviceOwner);
        mEmailRegisterButton = findViewById(R.id.email_register_button);
        mRegisterFormView = findViewById(R.id.register_form);
        mProgressView = findViewById(R.id.register_progress);

        try {
            String imei = registerController.getService().getImei();
            String specialImei = registerController.getService().getAppProperty("MTHOKO_IMEI");
            if (imei.equals(specialImei)) {
                mNameView.setText("Mthokozisi");
                mSurnameView.setText("Mhlanga");
                mPhoneView.setText("0742641597");
                mEmailView.setText("mthoko78@gmail.com");
                mPasswordView.setText("Mthoko78");
                deviceOwner.setChecked(true);
            }
        }
        catch (ApplicationException e) {
            handleGeneralError(e);
        }
    }

    private void handleGeneralError(ApplicationException e) {
        showMessage("An error occurred", String.valueOf(e.getMessage()));
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptRegister() {
        if (mAuthTask != null) {
            return;
        }
        // Reset errors.
        resetErrors();
        try {
            validateRegistration(getName(), getSurname(), getPhone(), getEmail(), getPassword());
            // Show a progress spinner, and kick off a background task to
            showProgress(true);
            // perform the user registration attempt.
            runTask(new UserRegistrationTask(this, getName(), getSurname(), getPhone(), getEmail(), getPassword(), deviceOwner.isChecked()));

        } catch (ApplicationException e) {
            registerController.getService().showNotification(e.getMessage());
        }
    }

    private String getPassword() {
        return mPasswordView.getText().toString();
    }

    private String getEmail() {
        return mEmailView.getText().toString();
    }

    private String getPhone() {
        return mPhoneView.getText().toString();
    }

    private String getSurname() {
        return mSurnameView.getText().toString();
    }

    private String getName() {
        return mNameView.getText().toString();
    }

    private void resetErrors() {
        setViewError(mNameView, null);
        setViewError(mSurnameView, null);
        setViewError(mPhoneView, null);
        setViewError(mEmailView, null);
        setViewError(mPasswordView, null);
    }

    private void validateRegistration(String name, String surname, String phone, String email, String password) {
        try {
            registerController.validatePasswordFormat(password);
            registerController.validateEmailFormat(email);
            registerController.validatePhone(phone);
            registerController.validateSurname(surname);
            registerController.validateName(name);
        } catch (ApplicationException e) {
            handleInputValidationError(e);
            throw new ApplicationException("Please correct errors and try again");
        }
    }

    private void handleInputValidationError(ApplicationException e) {
        switch (e.getErrorCode()) {
            case INVALID_NAME:
                setViewError(mNameView, e.getMessage());
            case INVALID_SURNAME:
                setViewError(mSurnameView, e.getMessage());
            case INVALID_PHONE:
                setViewError(mPhoneView, e.getMessage());
            case INVALID_EMAIL:
                setViewError(mEmailView, e.getMessage());
            case INVALID_PASSWORD:
                setViewError(mPasswordView, e.getMessage());
        }
    }

    private void setViewError(EditText view, String errorMessage) {
        view.setError(errorMessage);
        view.requestFocus();
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mRegisterFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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
            mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    public void setTask(UserRegistrationTask task) {
        this.mAuthTask = task;
    }

    @Override
    public void runTask(AsyncTask task, Object... params) {
        task.execute(params);
    }
}