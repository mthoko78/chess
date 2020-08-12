package com.mthoko.mobile.service.proxy;

import java.io.File;

import org.apache.commons.net.ftp.FTPFile;

import com.mthoko.mobile.resource.BaseResourceRemote;
import com.mthoko.mobile.service.FTPService;
import com.mthoko.mobile.service.internal.BaseServiceImpl;
import com.mthoko.mobile.util.FTPServiceImpl;

public class FTPServiceProxy extends BaseServiceImpl implements FTPService {

	private final FTPServiceImpl service;

	public FTPServiceProxy() {
		service = new FTPServiceImpl();
	}

	@Override
	public BaseResourceRemote<?> getResource() {
		return service.getResource();
	}

	@Override
	public FTPFile[] getClientFiles(String dir) {
		return service.getClientFiles(dir);
	}

	@Override
	public String uploadFile(File file) {
		return service.uploadFile(file);
	}

	@Override
	public String uploadFile(File file, String serverDir) {
		return service.uploadFile(file, serverDir);
	}

	@Override
	public FTPFile[] getClientFiles() {
		return service.getClientFiles();
	}

	@Override
	public String getCallsRemoteDirectory() {
		return service.getCallsRemoteDirectory();
	}
}
