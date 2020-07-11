package com.mthoko.mobile.entity;

import com.mthoko.mobile.annotations.PrimaryKey;

import java.util.Date;

public class FileInfo extends UniqueEntity {

    @PrimaryKey
    private Long id;

    private Long verificationId;

    private String ownerImei;

    private String fileName;

    private String localDirectory;

    private String remoteDirectory;

    private Date dateCreated;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public Long getVerificationId() {
        return verificationId;
    }

    @Override
    public void setVerificationId(Long verificationId) {
        this.verificationId = verificationId;
    }

    public String getOwnerImei() {
        return ownerImei;
    }

    public void setOwnerImei(String ownerImei) {
        this.ownerImei = ownerImei;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    @Override
    public String getUniqueIdentifier() {
        return localDirectory +"|"+fileName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getLocalDirectory() {
        return localDirectory;
    }

    public void setLocalDirectory(String localDirectory) {
        this.localDirectory = localDirectory;
    }

    public String getRemoteDirectory() {
        return remoteDirectory;
    }

    public void setRemoteDirectory(String remoteDirectory) {
        this.remoteDirectory = remoteDirectory;
    }

    public String absolutePath() {
        return getLocalDirectory() + "/" + getFileName();
    }
}
