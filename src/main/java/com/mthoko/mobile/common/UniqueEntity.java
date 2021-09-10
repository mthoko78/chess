package com.mthoko.mobile.common;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.*;

@MappedSuperclass
public abstract class UniqueEntity implements Comparable<UniqueEntity> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date dateCreated;

    private Date lastModified;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @JsonIgnore
    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    @JsonIgnore
    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    @JsonIgnore
    public abstract String getUniqueIdentifier();

    @JsonIgnore
    public boolean isValid() {
        return getId() != null;
    }

    @JsonIgnore
    public boolean isVerified() {
        return isValid() && getId() > 0;
    }

    public void putVerification(Map<String, Long> verification) {
        putVerification(this, verification);
    }

    public static <T extends UniqueEntity> void putVerification(T entity, Map<String, Long> verification) {
        if (entity.isValid()) {
            String uniqueIdentifier = entity.getUniqueIdentifier();
            Long id = entity.getId();
            verification.put(uniqueIdentifier, id);
        }
    }

    public static <T extends UniqueEntity> void putVerification(List<T> entities, Map<String, Long> verification) {
        for (UniqueEntity uniqueEntity : entities) {
            putVerification(uniqueEntity, verification);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (getClass().equals(o.getClass())) {
            return getUniqueIdentifier().equals(((UniqueEntity) o).getUniqueIdentifier());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return getUniqueIdentifier().hashCode();
    }

    @Override
    public int compareTo(UniqueEntity that) {
        if (that == null) {
            return -1;
        }
        return getUniqueIdentifier().compareTo(that.getUniqueIdentifier());
    }

    public <T extends UniqueEntity> String getUniqueIdentifierByList(List<T> entities) {
        Set<String> sortedIdentifiers = new TreeSet<>();
        for (T entity : entities) {
            sortedIdentifiers.add(entity.getUniqueIdentifier());
        }
        String result = "";
        for (String identifier : sortedIdentifiers) {
            result += "|" + identifier;
        }
        return result;
    }

    public String byteArrayToString(byte[] array) {
        return array != null ? new String(array) : null;
    }

    public byte[] stringToByteArray(String string) {
        return string != null ? string.getBytes() : null;
    }

}
