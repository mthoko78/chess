package com.mthoko.mobile.file;

import java.io.File;

import org.apache.commons.net.ftp.FTPFile;

import com.mthoko.mobile.common.BaseService;

public interface FTPService extends BaseService<FileInfo> {

	FTPFile[] getClientFiles(String dir);

	String uploadFile(File file);

	String uploadFile(File file, String serverDir);

	FTPFile[] getClientFiles();

	String getCallsRemoteDirectory();
}
