package com.mthoko.mobile.domain.file;

import com.mthoko.mobile.exception.ApplicationException;

import java.io.*;

public class FileManagementService {

    public static boolean firstRun = true;

    public FileManagementService() {
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
}
