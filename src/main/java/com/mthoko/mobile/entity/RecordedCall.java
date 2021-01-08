package com.mthoko.mobile.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
public class RecordedCall extends UniqueEntity {

	private String category;

	private String caller;

	private String receiverImei;

	private String receiverSimNo;

	private byte[] contactName;

	private Date timestamp;

	private int duration;

	@OneToOne
	private FileInfo fileInfo;

	public RecordedCall() {
	}

	@Override
	public String getUniqueIdentifier() {
		return receiverImei + "|" + getFileInfo().getUniqueIdentifier();
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getCaller() {
		return caller;
	}

	public void setCaller(String caller) {
		this.caller = caller;
	}

	public String getReceiverSimNo() {
		return receiverSimNo;
	}

	public void setReceiverSimNo(String receiverSimNo) {
		this.receiverSimNo = receiverSimNo;
	}

	public String getContactName() {
		return byteArrayToString(contactName);
	}

	public void setContactName(String contactName) {
		this.contactName = stringToByteArray(contactName);
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public Long getFileInfoId() {
		return fileInfo == null ? null : fileInfo.getId();
	}

	public void setFileInfoId(Long fileInfoId) {
		if (fileInfo != null) {
			fileInfo.setId(fileInfoId);
		}
	}

	public String getReceiverImei() {
		return receiverImei;
	}

	public void setReceiverImei(String receiverImei) {
		this.receiverImei = receiverImei;
	}

	@Override
	public String toString() {
		return "RecordedCall [id=" + getId() + ", category=" + category + ", caller=" + caller + ", receiverImei="
				+ receiverImei + ", receiverSimNo=" + receiverSimNo + ", contactName=" + getContactName() + ", timestamp="
				+ timestamp + ", duration=" + duration + ", fileInfoId=" + getFileInfoId() + "]";
	}

	public FileInfo getFileInfo() {
		return fileInfo;
	}

	public void setFileInfo(FileInfo fileInfo) {
		this.fileInfo = fileInfo;
		setFileInfoId(fileInfo == null ? null : fileInfo.getId());
	}

}
