package com.mthoko.learners.domain.file;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mthoko.learners.common.entity.UniqueEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class FileInfo extends UniqueEntity {

    private String ownerImei;

    private String fileName;

    private String localDirectory;

    private String remoteDirectory;

    private Date dateCreated;

    @Override
    public String getUniqueIdentifier() {
        return absolutePath();
    }

    @JsonIgnore
    public String absolutePath() {
        return getLocalDirectory() + "/" + getFileName();
    }

    @Override
    public String toString() {
        return "FileInfo [id=" + getId() + ", ownerImei=" + ownerImei + ", fileName=" + fileName + ", localDirectory="
                + localDirectory + ", remoteDirectory=" + remoteDirectory + ", dateCreated=" + dateCreated + "]";
    }

}
