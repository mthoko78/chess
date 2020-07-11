package com.mthoko.mobile.resource.internal;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.mthoko.mobile.entity.Member;
import com.mthoko.mobile.model.Account;
import com.mthoko.mobile.util.DatabaseWrapper;

public class AccountResource extends BaseResource<Account> {

    public final CredentialsResource credentialsResource;
    public final SimCardResource simCardResource;
    public final MemberResource memberResource;
    public final DeviceResource deviceResource;

    public AccountResource(Context context, DatabaseWrapper databaseWrapper) {
        super(context, Account.class, databaseWrapper);
        credentialsResource = new CredentialsResource(getContext(), databaseWrapper);
        simCardResource = new SimCardResource(getContext(), databaseWrapper);
        memberResource = new MemberResource(getContext(), databaseWrapper);
        deviceResource = new DeviceResource(context, databaseWrapper);
    }

    @Override
    public void setDatabase(SQLiteDatabase database) {
        memberResource.setDatabase(database);
        credentialsResource.setDatabase(database);
        deviceResource.setDatabase(database);
        simCardResource.setDatabase(database);
    }

    @Override
    public SQLiteDatabase getDatabase() {
        return memberResource.getDatabase();
    }

    public Account findByMemberId(long memberId) {
        Account account = new Account();
        Member member = memberResource.findById(memberId);
        if (member == null) {
            return null;
        }
        account.setMember(member);
        account.setSimCards(simCardResource.findByMemberId(memberId));
        account.setCredentials(credentialsResource.findByMemberId(member.getId()));
        account.setDevices(deviceResource.findByMemberId(member.getId()));
        return account;
    }
}
