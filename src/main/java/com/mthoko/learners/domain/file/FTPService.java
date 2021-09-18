package com.mthoko.learners.domain.file;

import com.mthoko.learners.common.service.BaseService;
import org.apache.commons.net.ftp.FTPFile;

import java.io.File;

public interface FTPService extends BaseService<FileInfo> {

	FTPFile[] getClientFiles(String dir);

	String uploadFile(File file);

	String uploadFile(File file, String serverDir);

	FTPFile[] getClientFiles();

	String getCallsRemoteDirectory();
}
