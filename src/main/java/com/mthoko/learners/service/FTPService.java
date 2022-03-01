package com.mthoko.learners.service;

import com.mthoko.learners.persistence.entity.FileInfo;
import org.apache.commons.net.ftp.FTPFile;

import java.io.File;

public interface FTPService extends BaseService<FileInfo> {

	FTPFile[] getClientFiles(String dir);

	String uploadFile(File file);

	String uploadFile(File file, String serverDir);

	FTPFile[] getClientFiles();

	String getCallsRemoteDirectory();
}
