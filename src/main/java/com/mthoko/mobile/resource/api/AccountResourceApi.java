package com.mthoko.mobile.resource.api;

import android.content.Context;

import com.mthoko.mobile.entity.Credentials;
import com.mthoko.mobile.entity.Device;
import com.mthoko.mobile.entity.Member;
import com.mthoko.mobile.entity.SimCard;
import com.mthoko.mobile.entity.UniqueEntity;
import com.mthoko.mobile.exception.ApplicationException;
import com.mthoko.mobile.model.Account;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AccountResourceApi extends BaseResourceApi<Account> {

    private final MemberResourceApi memberResource;
    private final CredentialsResourceApi credentialsResource;
    private final SimCardResourceApi simCardResource;
    private final DeviceResourceApi deviceResource;

    public AccountResourceApi(Context context) {
        super(context, Account.class);
        memberResource = new MemberResourceApi(context);
        credentialsResource = new CredentialsResourceApi(context);
        simCardResource = new SimCardResourceApi(context);
        deviceResource = new DeviceResourceApi(context);
    }

    public List<Account> findMatchingAccounts(Account targetAccount) {
        if (!targetAccount.isValid()) {
            return new ArrayList<>();
        }
        HashMap<String, String> params = new HashMap<>();
        params.put("key", FIND_MATCHING_ACCOUNTS);
        params.put("value", (toJson(targetAccount)).toString());
        return retrieveByRequest(params);
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
        HashMap<String, String> params = new HashMap<>();
        params.put("key", FIND_ACCOUNT_BY_SIM_NO);
        params.put("value", '"' + simNo + '"');
        return retrieveOneByRequest(params);
    }

    public Account findByEmail(String email) {
        HashMap<String, String> params = new HashMap<>();
        params.put("key", FIND_ACCOUNT_BY_EMAIL);
        params.put("value", '"' + email + '"');
        return retrieveOneByRequest(params);
    }

    @Override
    protected Account retrieveOneByRequest(Map<String, String> params) {
        try {
            String responseText = getResponseText(params);
            if (!String.valueOf(responseText).equals("null")) {
                JSONObject jsonObject = new JSONObject(responseText);
                if (jsonObject.length() > 0) {
                    return extractFromJsonObject(jsonObject);
                }
            }
            return null;
        } catch (JSONException e) {
            throw new ApplicationException(e);
        }
    }

    @Override
    protected List<Account> retrieveByRequest(Map<String, String> params) {
        try {
            return extractFromJsonArray(new JSONArray(getResponseText(params)));
        } catch (JSONException e) {
            throw new ApplicationException(e);
        }
    }

    @Override
    public JSONArray toJsonArray(List<Account> objects) {
        return super.toJsonArray(objects);
    }

    @Override
    public JSONObject toJson(Account account) {
        try {
            JSONObject jsonAccount = super.toJson(account);
            jsonAccount.put("member", memberResource.toJson(account.getMember()));
            jsonAccount.put("credentials", credentialsResource.toJson(account.getCredentials()));
            jsonAccount.put("simCards", simCardResource.toJsonArray(account.getSimCards()));
            jsonAccount.put("devices", deviceResource.toJsonArray(account.getDevices()));
            return jsonAccount;
        } catch (JSONException e) {
            throw new ApplicationException(e);
        }
    }


    @Override
    protected Account extractFromJsonObject(JSONObject jsonObject) {
        try {
            Member member = memberResource.extractFromJsonObject(jsonObject.getJSONObject("member"));
            Credentials credentials = credentialsResource.extractFromJsonObject(jsonObject.getJSONObject("credentials"));
            List<SimCard> simCards = simCardResource.extractFromJsonArray(jsonObject.getJSONArray("simCards"));
            List<Device> devices = deviceResource.extractFromJsonArray(jsonObject.getJSONArray("devices"));
            return new Account(member, credentials, simCards, devices);
        } catch (JSONException e) {
            throw new ApplicationException(e);
        }
    }

    @Override
    protected List<Account> extractFromJsonArray(JSONArray array) {
        try {
            List<Account> accounts = new ArrayList<>();
            for (int i = 0; i < array.length(); i++) {
                accounts.add(extractFromJsonObject(array.getJSONObject(i)));
            }
            return accounts;
        } catch (JSONException e) {
            throw new ApplicationException(e);
        }
    }

    public Map<String, Long> retrieveVerificationByEmail(String email) {
        Map<String, String> params = new HashMap<>();
        params.put("key", RETRIEVE_VERIFICATION_BY_EMAIL);
        params.put("value", '"' + email + '"');
        try {
            String responseText = super.getResponseText(params);
            if (responseText != null && !responseText.equalsIgnoreCase("null")) {
                JSONObject jsonObject = new JSONObject(responseText);
                if (!jsonObject.has("errorMessage")) {
                    return extractVerificationFromJson(jsonObject);
                }
            }
        } catch (JSONException e) {
            throw new ApplicationException(e);
        }
        return new HashMap<>();

    }

    public Map<String, Long> extractVerificationFromJson(JSONObject jsonObject) {
        try {
            Map<String, Long> result = new HashMap<>();
            JSONObject member = jsonObject.getJSONObject("member");
            JSONObject credentials = jsonObject.getJSONObject("credentials");
            JSONObject simCards = jsonObject.getJSONObject("simCards");
            JSONObject devices = jsonObject.getJSONObject("devices");
            result.putAll(memberResource.extractVerificationFromJson(member));
            result.putAll(credentialsResource.extractVerificationFromJson(credentials));
            result.putAll(simCardResource.extractVerificationFromJson(simCards));
            result.putAll(deviceResource.extractVerificationFromJson(devices));
            return result;
        } catch (JSONException e) {
            throw new ApplicationException(e);
        }

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
}
