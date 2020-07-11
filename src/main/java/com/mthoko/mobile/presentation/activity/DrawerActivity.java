package com.mthoko.mobile.presentation.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.mthoko.mobile.R;
import com.mthoko.mobile.exception.ApplicationException;
import com.mthoko.mobile.model.Account;
import com.mthoko.mobile.presentation.controller.LoginController;
import com.mthoko.mobile.presentation.fragment.DashboardFragment;
import com.mthoko.mobile.presentation.fragment.DevContactsFragment;
import com.mthoko.mobile.presentation.fragment.ExportFragment;
import com.mthoko.mobile.presentation.fragment.ImportFragment;
import com.mthoko.mobile.presentation.fragment.LogoutFragment;
import com.mthoko.mobile.presentation.fragment.MediaPlayerFragment;
import com.mthoko.mobile.presentation.fragment.MessagesFragment;
import com.mthoko.mobile.presentation.fragment.RecordedCallsFragment;
import com.mthoko.mobile.presentation.fragment.RecorderFragment;
import com.mthoko.mobile.presentation.fragment.SettingsFragment;
import com.mthoko.mobile.presentation.fragment.SimContactsFragment;
import com.mthoko.mobile.resource.internal.MyResources;
import com.mthoko.mobile.util.MyConstants;


public class DrawerActivity extends BackgroundTaskRunner implements MyConstants {

    public static DrawerActivity INSTANCE;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private NavigationView navDrawer;
    private LoginController loginController;
    private View progressView;
    private TextView textViewProfileEmail;
    private TextView textViewProfileIMEI;
    private TextView textViewProfileDisplayName;
    private int currentMenuId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        INSTANCE = this;
        initUIComponents();
        initBackgroundProcesses();
        loginController = new LoginController(getApplicationContext());
        Long accountId = getIntent().getLongExtra("accountId", -1);
        String imei = loginController.getImei();
        String simNo = loginController.getCurrentSimNo();
        showMessage("Imei", imei);
        final Account account;
        if (accountId != -1) {
            account = loginController.findAccountByMemberId(accountId);
        } else {
            account = loginController.findAccountBySimNo(loginController.getCurrentSimNo());
        }
        if (account == null) {
            return;
        }
        String deviceName = loginController.getService().getDeviceName();
        String profileInfo = String.format("IMEI: %s\nSim No: %s\n%s", imei, simNo, deviceName);
        setDisplayName(account.getMember().getName() + " " + account.getMember().getSurname());
        setEmail(account.getMember().getEmail());
        setProfileInfo(profileInfo);
    }

    private void setProfileInfo(String profileInfo) {
        textViewProfileIMEI.setText(profileInfo);
    }

    private void setEmail(String email) {
        textViewProfileEmail.setText(email);
    }

    private void setDisplayName(String displayName) {
        textViewProfileDisplayName.setText(displayName);
    }

    private void initBackgroundProcesses() {
        if (MyResources.getInstance() == null) {
            MyResources.getInstance(this.getApplicationContext());
        }
        MyResources.getInstance().setContext(this.getApplicationContext());
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            Log.i("Service status", "" + service.clientPackage);
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i("Service status", "Running");
                return true;
            }
        }
        Log.i("Service status", "Not running");
        return false;
    }

    private void initUIComponents() {
        setContentView(R.layout.activity_drawer);
        navDrawer = findViewById(R.id.navDrawer);
        mDrawerLayout = findViewById(R.id.drawer);
        progressView = findViewById(R.id.drawer_progress);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        textViewProfileEmail = navDrawer.getHeaderView(0).findViewById(R.id.profileEmail);
        textViewProfileDisplayName = navDrawer.getHeaderView(0).findViewById(R.id.profileDisplayName);
        textViewProfileIMEI = navDrawer.getHeaderView(0).findViewById(R.id.profileIMEI);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setUpDrawerContent(navDrawer);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Toast.makeText(this.getApplicationContext(), "Exiting", Toast.LENGTH_LONG).show();
    }

    @Override
    public void runTask(AsyncTask task, Object... params) {
        task.execute(params);
    }


    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.

        final View toggleView = mDrawerLayout;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            toggleView.setVisibility(show ? View.GONE : View.VISIBLE);
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
            toggleView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    toggleView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });
            progressView.setVisibility(show ? View.VISIBLE : View.GONE);
            progressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    progressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            progressView.setVisibility(show ? View.VISIBLE : View.GONE);
            toggleView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }


    public void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        MenuItem menuItem = findViewById(R.id.logout);
        //selectItemDrawer(menuItem);
        showMessage("back", "Item: " + menuItem);
        //super.onBackPressed();
    }

    public void selectItemDrawer(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId == currentMenuId) {
            showMessage("", "Select different item");
        } else {
            try {
                select(menuItem, itemId);
            }
            catch (ApplicationException e) {
                showMessage("", "An error occurred, please try again later");
            }
        }
        mDrawerLayout.closeDrawers();

    }

    public void select(MenuItem menuItem, int itemId) {
        currentMenuId = itemId;
        Fragment fragment = getFragment(itemId);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
    }

    private Fragment getFragment(int fragmentId) {
        try {
            return (Fragment) getFragmentClass(fragmentId).newInstance();
        } catch (IllegalAccessException | InstantiationException e) {
            throw new ApplicationException(e);
        }
    }

    private Class getFragmentClass(int fragmentId) {
        switch (fragmentId) {
            case R.id.recorder:
                return RecorderFragment.class;
            case R.id.recordedCalls:
                return RecordedCallsFragment.class;
            case R.id.export:
                return ExportFragment.class;
            case R.id.importFiles:
                return ImportFragment.class;
            case R.id.messages:
                return MessagesFragment.class;
            case R.id.deviceContacts:
                return DevContactsFragment.class;
            case R.id.simContacts:
                return SimContactsFragment.class;
            case R.id.settings:
                return SettingsFragment.class;
            case R.id.logout:
                return LogoutFragment.class;
            case R.id.player:
                return MediaPlayerFragment.class;
            default:
                return DashboardFragment.class;
        }
    }

    public void setUpDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selectItemDrawer(item);
                return true;
            }
        });
    }
}