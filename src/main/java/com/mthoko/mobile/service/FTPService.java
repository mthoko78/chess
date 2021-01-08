package com.mthoko.mobile.service;

import java.io.File;

import org.apache.commons.net.ftp.FTPFile;

import com.mthoko.mobile.entity.FileInfo;

public interface FTPService extends BaseService<FileInfo> {

	FTPFile[] getClientFiles(String dir);

	String uploadFile(File file);

	String uploadFile(File file, String serverDir);

	FTPFile[] getClientFiles();

	String getCallsRemoteDirectory();
}
