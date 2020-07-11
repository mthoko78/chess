package com.mthoko.mobile.resource.api;

import android.content.Context;

import com.mthoko.mobile.entity.Credentials;

public class CredentialsResourceApi extends BaseResourceApi<Credentials> {

    public CredentialsResourceApi(Context context) {
        super(context, Credentials.class);
    }
}
