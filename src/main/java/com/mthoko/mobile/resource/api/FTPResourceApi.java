package com.mthoko.mobile.resource.api;

import android.content.Context;
import android.util.Log;

import com.mthoko.mobile.exception.ApplicationException;
import com.mthoko.mobile.service.common.RecordingService;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FTPResourceApi extends BaseResourceApi {

    private String server = "ftpupload.net";
    private int port = 21;
    private String user = "epiz_24456977";
    private String pass = "Qfniw5vLxB7";

    public FTPResourceApi(Context context) {
        super(context, null);

    }

    public void uploadFile(File file, String serverDir) throws ApplicationException {
        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.connect(server, port);
            ftpClient.login(user, pass);
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            uploadUsingInputStream(ftpClient, file, serverDir);
            Log.e("FTP RESPONSE", "" + ftpClient.getReplyString());
        } catch (IOException e) {
            throw new ApplicationException(e);
        } finally {
            try {
                if (ftpClient.isConnected()) {
                    ftpClient.logout();
                    ftpClient.disconnect();
                }
            } catch (IOException e) {
                throw new ApplicationException(e);
            }
        }
    }

    private void uploadUsingInputStream(FTPClient ftpClient, File localFile, String serverOutputDirectory) throws IOException {
        String remoteFile = serverOutputDirectory + localFile.getName();
        InputStream inputStream = new FileInputStream(localFile);
        Log.e("uploading file", remoteFile);
        boolean done = ftpClient.storeFile(remoteFile, inputStream);
        inputStream.close();
        if (done) {
            Log.e("info", "The file is uploaded successfully.");
        } else {
            Log.e("info", "The file was NOT uploaded successfully.");
        }
    }

    public FTPFile[] getClientFiles(String pathName) throws ApplicationException {
        FTPClient ftpClient = new FTPClient();
        FTPFile[] clientFiles;
        try {
            ftpClient.connect(this.server, this.port);
            ftpClient.login(user, pass);
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            clientFiles = ftpClient.listFiles(pathName);
            Log.e("total client files", "" + clientFiles.length);
            for (FTPFile file : clientFiles) {
                if (file.getName().equals(".") || file.getName().equals("..")) {
                    continue;
                }
                String remotePath = pathName + file.getName();
                String newFilename = RecordingService.RECORDING_DIRECTORY + "/" + file.getName();
                File newFile = new File(newFilename);
                if (newFile.exists()) {
                    Log.e("already exists", newFilename);
                    continue;
                }
                Log.e("file", remotePath);
                OutputStream outputStream = new FileOutputStream(newFile);
                if (outputStream == null) {
                    Log.e("download failed", remotePath);
                    continue;
                }
                boolean fileRetrieved = ftpClient.retrieveFile(remotePath, outputStream);
                outputStream.close();
                Log.e(newFilename + "->retrieved", "" + fileRetrieved);
            }
        } catch (IOException e) {
            throw new ApplicationException(e);
        } finally {
            try {
                if (ftpClient.isConnected()) {
                    ftpClient.logout();
                    ftpClient.disconnect();
                }
            } catch (IOException e) {
                throw new ApplicationException(e);
            }
        }
        return clientFiles;
    }
}
