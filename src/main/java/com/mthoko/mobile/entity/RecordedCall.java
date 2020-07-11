package com.mthoko.mobile.entity;

import com.mthoko.mobile.annotations.Entity;
import com.mthoko.mobile.annotations.ForeignKey;
import com.mthoko.mobile.annotations.PrimaryKey;

import java.util.Date;

/**
 * Created by Mthoko on 02 Jun 2018.
 */

@Entity
public class RecordedCall extends UniqueEntity {
    @PrimaryKey
    private Long id;
    private Long verificationId;
    private String category;
    private String caller;
    private String receiverImei;
    private String receiverSimNo;
    private String contactName;
    private Date timestamp;
    private int duration;
    @ForeignKey(referencedEntity = FileInfo.class)
    private Long fileInfoId;

    public RecordedCall() {
    }

    public RecordedCall(Long id, String category, String caller, String contactName, Date timestamp, int duration, Long fileInfoId) {
        this.id = id;
        this.category = category;
        this.caller = caller;
        this.contactName = contactName;
        this.timestamp = timestamp;
        this.duration = duration;
        this.fileInfoId = fileInfoId;
    }

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

    @Override
    public String getUniqueIdentifier() {
        return caller + "|" + timestamp.getTime();
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
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
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
        return fileInfoId;
    }

    public void setFileInfoId(Long fileInfoId) {
        this.fileInfoId = fileInfoId;
    }

    public String getReceiverImei() {
        return receiverImei;
    }

    public void setReceiverImei(String receiverImei) {
        this.receiverImei = receiverImei;
    }

    @Override
    public String toString() {
        return "RecordedCall{" +
                "id=" + id +
                ", verificationId=" + verificationId +
                ", category='" + category + '\'' +
                ", caller='" + caller + '\'' +
                ", receiver='" + receiverSimNo + '\'' +
                ", contactName='" + contactName + '\'' +
                ", timestamp=" + timestamp +
                ", duration=" + duration +
                ", fileInfoId=" + fileInfoId +
                '}';
    }
}
