package com.mthoko.mobile.service.proxy;

import com.mthoko.mobile.entity.FileInfo;
import com.mthoko.mobile.resource.remote.BaseResourceRemote;
import com.mthoko.mobile.service.FTPService;
import com.mthoko.mobile.service.internal.BaseServiceImpl;
import com.mthoko.mobile.util.FTPServiceImpl;

import org.apache.commons.net.ftp.FTPFile;

import java.io.File;

public class FTPServiceProxy extends BaseServiceImpl<FileInfo> implements FTPService {

    private final FTPServiceImpl service;

    public FTPServiceProxy() {
        service = new FTPServiceImpl();
    }

    @Override
    public BaseResourceRemote getRemoteResource() {
        return service.getRemoteResource();
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
