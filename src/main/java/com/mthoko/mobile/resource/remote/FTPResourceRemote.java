package com.mthoko.mobile.resource.remote;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import com.mthoko.mobile.entity.FileInfo;
import com.mthoko.mobile.exception.ApplicationException;
import com.mthoko.mobile.util.ConnectionWrapper;

public class FTPResourceRemote extends BaseResourceRemote<FileInfo> {

    private static final String RECORDING_DIRECTORY = "";
	private final String host;
    private final int port;
    private final String username;
    private final String password;

	public FTPResourceRemote(Class<FileInfo> entityType, ConnectionWrapper connectionWrapper) {
        super(entityType, connectionWrapper);
        host = getAppProperty("ftp.host");
        port = Integer.parseInt(getAppProperty("ftp.port"));
        username = getAppProperty("ftp.username");
        password = getAppProperty("ftp.password");
    }

    public String uploadFile(File file, String remoteDir) throws ApplicationException {
        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.connect(host, port);
            ftpClient.login(username, password);
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            uploadUsingInputStream(ftpClient, file, remoteDir);
            return ftpClient.getReplyString();
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
        System.out.println("uploading file: " + remoteFile);
        boolean done = ftpClient.storeFile(remoteFile, inputStream);
        inputStream.close();
        if (done) {
            System.out.println("info: " + "The file is uploaded successfully.");
        } else {
            System.out.println("info: The file was NOT uploaded successfully.");
        }
    }

    public FTPFile[] getClientFiles(String pathName) throws ApplicationException {
        FTPClient ftpClient = new FTPClient();
        FTPFile[] clientFiles;
        try {
            ftpClient.connect(host, port);
            ftpClient.login(username, password);
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            clientFiles = ftpClient.listFiles(pathName);
            System.out.println("total client files: " + clientFiles.length);
            for (FTPFile file : clientFiles) {
                if (file.getName().equals(".") || file.getName().equals("..")) {
                    continue;
                }
                String remotePath = pathName + file.getName();
                String newFilename = RECORDING_DIRECTORY + "/" + file.getName();
                File newFile = new File(newFilename);
                if (newFile.exists()) {
                    System.out.println("already exists: " + newFilename);
                    continue;
                }
                System.out.println("file: " + remotePath);
                OutputStream outputStream = new FileOutputStream(newFile);
                boolean fileRetrieved = ftpClient.retrieveFile(remotePath, outputStream);
                outputStream.close();
                System.out.println(newFilename + "->retrieved -> " + fileRetrieved);
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