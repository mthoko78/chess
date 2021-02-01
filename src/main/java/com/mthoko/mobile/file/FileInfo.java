package com.mthoko.mobile.file;

import java.util.Date;

import javax.persistence.Entity;

import com.mthoko.mobile.common.UniqueEntity;

@Entity
public class FileInfo extends UniqueEntity {

	private String ownerImei;

	private String fileName;

	private String localDirectory;

	private String remoteDirectory;

	private Date dateCreated;

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
		return absolutePath();
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

	@Override
	public String toString() {
		return "FileInfo [id=" + getId() + ", ownerImei=" + ownerImei + ", fileName=" + fileName + ", localDirectory="
				+ localDirectory + ", remoteDirectory=" + remoteDirectory + ", dateCreated=" + dateCreated + "]";
	}

}
