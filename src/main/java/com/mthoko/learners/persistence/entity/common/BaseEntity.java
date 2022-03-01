package com.mthoko.learners.persistence.entity.common;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.*;

@MappedSuperclass
public abstract class BaseEntity implements Comparable<BaseEntity> {

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

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    @JsonIgnore
    public abstract String getUniqueIdentifier();

    public boolean isVerified() {
        return id != null && id > 0;
    }

    public void putVerification(Map<String, Long> verification) {
        putVerification(this, verification);
    }

    public static <T extends BaseEntity> void putVerification(T entity, Map<String, Long> verification) {
        if (entity.getId() != null && entity.getId() > 0) {
            String uniqueIdentifier = entity.getUniqueIdentifier();
            Long id = entity.getId();
            verification.put(uniqueIdentifier, id);
        }
    }

    public static <T extends BaseEntity> void putVerification(List<T> entities, Map<String, Long> verification) {
        for (BaseEntity baseEntity : entities) {
            putVerification(baseEntity, verification);
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
            return getUniqueIdentifier().equals(((BaseEntity) o).getUniqueIdentifier());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return getUniqueIdentifier().hashCode();
    }

    @Override
    public int compareTo(BaseEntity that) {
        if (that == null) {
            return -1;
        }
        return getUniqueIdentifier().compareTo(that.getUniqueIdentifier());
    }

    @JsonIgnore
    public <T extends BaseEntity> String getUniqueIdentifierByList(List<T> entities) {
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
