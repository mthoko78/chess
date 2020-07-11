package com.mthoko.mobile.service.internal;

import android.content.Context;

import com.mthoko.mobile.entity.Credentials;
import com.mthoko.mobile.resource.internal.BaseResource;
import com.mthoko.mobile.resource.internal.CredentialsResource;
import com.mthoko.mobile.resource.remote.BaseResourceRemote;
import com.mthoko.mobile.resource.remote.CredentialsResourceRemote;
import com.mthoko.mobile.service.CredentialsService;
import com.mthoko.mobile.util.ConnectionWrapper;
import com.mthoko.mobile.util.DatabaseWrapper;

public class CredentialsServiceImpl extends BaseServiceImpl<Credentials> implements CredentialsService {

    private final CredentialsResource credentialsResource;
    private final CredentialsResourceRemote credentialsResourceRemote;

    public CredentialsServiceImpl(Context context) {
        credentialsResource = new CredentialsResource(context, new DatabaseWrapper());
        credentialsResourceRemote = new CredentialsResourceRemote(context, new ConnectionWrapper(null));
    }

    public Credentials findByMemberId(Long memberId) {
        return credentialsResource.findByMemberId(memberId);
    }

    public void updateCredentialsUsing(Credentials credentials, Credentials that) {
        credentials.setMemberId(that.getMemberId());
        credentials.setPassword(that.getPassword());
        credentials.setVerificationId(that.getVerificationId());
        update(credentials);
    }

    public void updateVerificationId(Credentials credentials, long verificationId) {
        credentials.setVerificationId(verificationId);
        update(credentials);
    }

    @Override
    public BaseResource getResource() {
        return credentialsResource;
    }

    @Override
    public Context getContext() {
        return credentialsResource.getContext();
    }

    @Override
    public BaseResourceRemote getRemoteResource() {
        return credentialsResourceRemote;
    }

    @Override
    public void setContext(Context context) {
        credentialsResource.setContext(context);
    }
}
