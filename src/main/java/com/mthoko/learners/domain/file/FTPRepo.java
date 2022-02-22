package com.mthoko.learners.domain.file;

import com.mthoko.learners.common.repo.BaseResourceRemote;
import com.mthoko.learners.exception.ApplicationException;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;

import static com.mthoko.learners.common.util.MyConstants.print;

@Component
public class FTPRepo extends BaseResourceRemote {

    private static final String RECORDING_DIRECTORY = "";

    @Value("${ftp.host}")
    private String host;

    @Value("${ftp.port}")
    private int port;

    @Value("${ftp.username}")
    private String username;

    @Value("${ftp.password}")
    private String password;

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

    private void uploadUsingInputStream(FTPClient ftpClient, File localFile, String serverOutputDirectory)
            throws IOException {
        String remoteFile = serverOutputDirectory + localFile.getName();
        InputStream inputStream = new FileInputStream(localFile);
        print("uploading file: " + remoteFile);
        boolean done = ftpClient.storeFile(remoteFile, inputStream);
        inputStream.close();
        if (done) {
            print("info: " + "The file is uploaded successfully.");
        } else {
            print("info: The file was NOT uploaded successfully.");
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
            print("total client files: " + clientFiles.length);
            for (FTPFile file : clientFiles) {
                if (file.getName().equals(".") || file.getName().equals("..")) {
                    continue;
                }
                String remotePath = pathName + file.getName();
                String newFilename = RECORDING_DIRECTORY + "/" + file.getName();
                File newFile = new File(newFilename);
                if (newFile.exists()) {
                    print("already exists: " + newFilename);
                    continue;
                }
                print("file: " + remotePath);
                OutputStream outputStream = new FileOutputStream(newFile);
                boolean fileRetrieved = ftpClient.retrieveFile(remotePath, outputStream);
                outputStream.close();
                print(newFilename + "->retrieved -> " + fileRetrieved);
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