package com.mthoko.mobile.resource.remote;

import android.content.Context;

import com.mthoko.mobile.entity.Member;
import com.mthoko.mobile.entity.SimCard;
import com.mthoko.mobile.entity.UniqueEntity;
import com.mthoko.mobile.exception.ApplicationException;
import com.mthoko.mobile.model.Account;
import com.mthoko.mobile.util.ConnectionWrapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AccountResourceRemote extends BaseResourceRemote<Account> {

    private final MemberResourceRemote memberResource;
    private final CredentialsResourceRemote credentialsResource;
    private final SimCardResourceRemote simCardResource;
    private final DeviceResourceRemote deviceResource;

    public AccountResourceRemote(Context context, ConnectionWrapper connectionWrapper) {
        super(Account.class, context, connectionWrapper);
        memberResource = new MemberResourceRemote(context, connectionWrapper);
        credentialsResource = new CredentialsResourceRemote(context, connectionWrapper);
        simCardResource = new SimCardResourceRemote(context, connectionWrapper);
        deviceResource = new DeviceResourceRemote(context, connectionWrapper);
    }

    public List<Account> findMatchingAccounts(Account targetAccount) {
        List<Account> accounts = new ArrayList<>();
        if (targetAccount.isValid()) {
            List<Member> members = findMatchingMembersByAccount(targetAccount);
            if (members.size() > 0) {
                for (int i = 0; i < members.size(); i++) {
                    accounts.add(retrieveAccountByMember(members.get(i)));
                }
            }
        }
        return accounts;
    }

    public void register(Account account) throws ApplicationException {
        Long memberId = account.getId();
        Long remoteMemberId = memberResource.save(account.getMember());
        account.setId(remoteMemberId);
        credentialsResource.save(account.getCredentials());
        simCardResource.saveAll(account.getSimCards());
        deviceResource.saveAll(account.getDevices());
        account.setId(memberId);
    }

    public Account findBySimNo(String simNo) {
        Member member = memberResource.findBySimNo(simNo);
        Account account = null;
        if (member != null) {
            account = retrieveAccountByMember(member);
        }
        return account;
    }

    public Account findByEmail(String email) {
        Member member = memberResource.findByEmail(email);
        Account account = null;
        if (member != null) {
            account = retrieveAccountByMember(member);
        }
        return account;
    }

    public Map<String, Long> retrieveVerificationByEmail(String email) {
        return extractVerification(findByEmail(email));
    }

    @Override
    public Map<String, Long> extractVerification(UniqueEntity entity) {
        Map<String, Long> verification = new HashMap<>();
        if (entity instanceof Account) {
            Account account = (Account) entity;
            verification.putAll(memberResource.extractVerification(account.getMember()));
            verification.putAll(credentialsResource.extractVerification(account.getCredentials()));
            verification.putAll(simCardResource.extractVerification(account.getSimCards()));
            verification.putAll(deviceResource.extractVerification(account.getDevices()));
        }
        return verification;
    }

    private List<Member> findMatchingMembersByAccount(Account targetAccount) {
        Set<String> phones = getAllPhones(targetAccount);
        String whereClause = String.format("%s.email = '%s'", memberResource.getEntityName(),
                targetAccount.getMember().getEmail());
        if (!phones.isEmpty()) {
            String phonesValues = phones.toString().replaceAll("[\\[\\]]", "");
            whereClause += String.format(" AND %s.phone IN (%s)", memberResource.getEntityName(), phonesValues);
        }
        List<Member> members = memberResource.findWhere(whereClause);
        return members;
    }

    private Set<String> getAllPhones(Account targetAccount) {
        Set<String> phones = new HashSet<>();
        if (targetAccount.getMember().getPhone() != null) {
            phones.add("'" + targetAccount.getMember().getPhone() + "'");
        }
        for (SimCard card : targetAccount.getSimCards()) {
            if (card.getPhone() != null) {
                phones.add("'" + card.getPhone() + "'");
            }
        }
        return phones;
    }

    private Account retrieveAccountByMember(Member member) {
        Account account = new Account();
        account.setMember(member);
        account.setSimCards(simCardResource.findByMemberId(member.getId()));
        account.setCredentials(credentialsResource.findByMemberId(member.getId()));
        account.setDevices(deviceResource.findByMemberId(member.getId()));
        return account;
    }
}
