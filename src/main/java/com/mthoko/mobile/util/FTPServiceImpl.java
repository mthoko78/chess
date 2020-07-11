package com.mthoko.mobile.util;

import android.content.Context;

import com.mthoko.mobile.entity.FileInfo;
import com.mthoko.mobile.resource.internal.BaseResource;
import com.mthoko.mobile.resource.internal.FTPResource;
import com.mthoko.mobile.resource.remote.FTPResourceRemote;
import com.mthoko.mobile.service.FTPService;
import com.mthoko.mobile.service.internal.BaseServiceImpl;

import org.apache.commons.net.ftp.FTPFile;

import java.io.File;

/**
 * A program that demonstrates how to upload files from local computer
 * to a remote FTP server using Apache Commons Net API.
 *
 * @author Mthoko
 */
public class FTPServiceImpl extends BaseServiceImpl<FileInfo> implements FTPService {

    private final FTPResource ftpResource;
    private final FTPResourceRemote ftpResourceRemote;

    public FTPServiceImpl(Context context) {
        ftpResource = new FTPResource(context);
        ftpResourceRemote = new FTPResourceRemote(FileInfo.class, context, new ConnectionWrapper(null));
    }

    @Override
    public String uploadFile(File file) {
        return uploadFile(file, getAppProperty("ftp.base_directory"));
    }

    @Override
    public String uploadFile(File file, String serverDir) {
        return ftpResourceRemote.uploadFile(file, serverDir);
    }

    @Override
    public FTPFile[] getClientFiles() {
        return getClientFiles(getAppProperty("ftp.base_directory"));
    }

    @Override
    public String getCallsRemoteDirectory() {
        return getAppProperty("ftp.calls_directory");
    }

    @Override
    public FTPFile[] getClientFiles(String dir) {
        return ftpResourceRemote.getClientFiles(dir);
    }

    @Override
    public BaseResource getResource() {
        return ftpResource;
    }

    @Override
    public FTPResourceRemote getRemoteResource() {
        return ftpResourceRemote;
    }

    @Override
    public void setContext(Context context) {
        ftpResource.setContext(context);
    }
}