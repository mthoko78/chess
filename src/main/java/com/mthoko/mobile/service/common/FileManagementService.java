package com.mthoko.mobile.service.common;

import android.content.Context;

import com.mthoko.mobile.exception.ApplicationException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileManagementService {

    public static boolean firstRun = true;

    private Context context;

    public FileManagementService(Context context) {
        this.context = context;
    }

    public void copyFileUsingStream(File source, File dest, boolean override) {
        if (dest == null || (dest.exists() && !override)) {
            firstRun = false;
            return;//showMessage("No need to copy", dest.getAbsolutePath()+" already exists");
        }
        InputStream in = null;
        OutputStream out = null;
        try {
            /*AssetManager am = context.getResources().getAssets();
            in = am.open(source);*/
            in = new FileInputStream(source);
            out = new FileOutputStream(dest);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
        } catch (IOException ex) {
            throw new ApplicationException(ex);
        } finally {
            try {
                in.close();
                out.close();
            } catch (IOException ex) {
                throw new ApplicationException(ex);
            }
        }
    }

    public void copyFileUsingStream(File source, File dest) {
        this.copyFileUsingStream(source, dest, false);
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
