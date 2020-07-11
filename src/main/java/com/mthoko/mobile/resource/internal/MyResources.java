package com.mthoko.mobile.resource.internal;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.mthoko.mobile.service.common.FileManagementService;
import com.mthoko.mobile.service.common.RecordingService;
import com.mthoko.mobile.util.WritableDatabaseHelper;

import java.io.File;

import static com.mthoko.mobile.resource.internal.BaseResource.DATABASE_NAME;
import static com.mthoko.mobile.service.common.RecordingService.EXTENSION;

/**
 * Created by mthokozisi_mhlanga on 10 Jul 2017.
 */

public class MyResources {

    private Context context;

    private static MyResources INSTANCE;

    private FileManagementService fileManagementService;

    private MyResources(Context context) {
        this.setContext(context);
        initialize();
    }

    private void initialize() {
        new WritableDatabaseHelper(getContext(), DATABASE_NAME);
        fileManagementService = new FileManagementService(context);
        //copyDatabases();
        String source = RecordingService.getRecentFilename();
        if (source != null) {
            String dest = getContext().getFilesDir() + "/recording" + EXTENSION;
            fileManagementService.copyFileUsingStream(new File(source), new File(dest));
        }
    }

    public void showMessage(String s) {
        Toast.makeText(getContext(), s, Toast.LENGTH_LONG).show();
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public static void showMessage(Context context, String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    public static void showMessage(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static MyResources getInstance(Context context) {
        if (INSTANCE == null)
            INSTANCE = new MyResources(context);
        return INSTANCE;
    }

    public static MyResources getInstance() {
        return INSTANCE;
    }
}
