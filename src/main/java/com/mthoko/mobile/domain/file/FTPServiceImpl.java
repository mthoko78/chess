package com.mthoko.mobile.domain.file;

import com.mthoko.mobile.common.service.BaseServiceImpl;
import org.apache.commons.net.ftp.FTPFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class FTPServiceImpl extends BaseServiceImpl<FileInfo> implements FTPService {

	@Autowired
	private FTPRepo ftpRepo;

	@Autowired
	private FileInfoRepo fileInfoRepo;

	@Override
	public String uploadFile(File file) {
		return uploadFile(file, getAppProperty("ftp.base_directory"));
	}

	@Override
	public String uploadFile(File file, String serverDir) {
		return ftpRepo.uploadFile(file, serverDir);
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
		return ftpRepo.getClientFiles(dir);
	}

	@Override
	public JpaRepository<FileInfo, Long> getRepo() {
		return fileInfoRepo;
	}

}