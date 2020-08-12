package com.mthoko.mobile.util;

import java.io.File;

import org.apache.commons.net.ftp.FTPFile;

import com.mthoko.mobile.entity.FileInfo;
import com.mthoko.mobile.resource.FTPResourceRemote;
import com.mthoko.mobile.service.FTPService;
import com.mthoko.mobile.service.internal.BaseServiceImpl;

/**
 * A program that demonstrates how to upload files from local computer
 * to a remote FTP server using Apache Commons Net API.
 *
 * @author Mthoko
 */
public class FTPServiceImpl extends BaseServiceImpl implements FTPService {

    private final FTPResourceRemote ftpResourceRemote;

    public FTPServiceImpl() {
        ftpResourceRemote = new FTPResourceRemote(FileInfo.class, new ConnectionWrapper(null));
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
    public FTPResourceRemote getResource() {
        return ftpResourceRemote;
    }

}