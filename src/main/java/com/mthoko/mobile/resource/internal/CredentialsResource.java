package com.mthoko.mobile.resource.internal;

import android.content.Context;

import com.mthoko.mobile.entity.Credentials;
import com.mthoko.mobile.util.DatabaseWrapper;

public class CredentialsResource extends BaseResource<Credentials> {

    public CredentialsResource(Context context, DatabaseWrapper databaseWrapper) {
        super(context, Credentials.class, databaseWrapper);
    }

    public Credentials findByMemberId(Long memberId) {
        return findOneWhere(String.format("%s.memberId = %s", getEntityName(), memberId));
    }
}
