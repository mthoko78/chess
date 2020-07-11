package com.mthoko.mobile.resource.internal;

import android.content.Context;

import com.mthoko.mobile.entity.FileInfo;
import com.mthoko.mobile.util.DatabaseWrapper;

public class FTPResource extends BaseResource<FileInfo> {

    public FTPResource(Context context) {
        super(context, FileInfo.class, new DatabaseWrapper());

    }
}
