package com.mthoko.mobile.service;

import com.mthoko.mobile.entity.FileInfo;

import org.apache.commons.net.ftp.FTPFile;

import java.io.File;

public interface FTPService extends BaseService<FileInfo> {

    FTPFile[] getClientFiles(String dir);

    String uploadFile(File file);

    String uploadFile(File file, String serverDir);

    FTPFile[] getClientFiles();

    String getCallsRemoteDirectory();
}
