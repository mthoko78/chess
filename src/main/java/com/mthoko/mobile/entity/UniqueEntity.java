package com.mthoko.mobile.entity;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public abstract class UniqueEntity implements Comparable<UniqueEntity> {

    public abstract Long getId();

    public abstract void setId(Long id);

    public abstract Long getVerificationId();

    public abstract void setVerificationId(Long verificationId);

    public abstract String getUniqueIdentifier();

    public boolean isValid() {
        return getId() != null;
    }

    public boolean isVerified() {
        return getVerificationId() != null;
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
}
