package com.mthoko.mobile.resource.remote;

import com.mthoko.mobile.entity.Sms;
import com.mthoko.mobile.util.ConnectionWrapper;

import java.util.List;
import java.util.Map;

public class SmsResourceRemote extends BaseResourceRemote<Sms> {

    public SmsResourceRemote(ConnectionWrapper connectionWrapper) {
        super(Sms.class, connectionWrapper);
    }

    public int countSmsesByRecipient(String recipient) {
		return super.countWhere(String.format("recipient = '%s'", recipient));
    }

    public Map<String, Long> retrieveVerificationByRecipient(String recipient) {
        return extractVerification(findByRecipient(recipient));
    }

    public List<Sms> findByRecipientWithIdsNotIn(List<Long> ids, String recipient) {
        if (ids.isEmpty()) {
            return findByRecipient(recipient);
        }
        String idsStr = ids.toString().replaceAll("[\\[\\]]", "");
        String whereClause = String.format("%s.recipient = '%s' AND %s.id NOT IN (%s)", getEntityName(),
                recipient, getEntityName(), idsStr);
        return super.findWhere(whereClause);
    }

    public List<Sms> findByRecipient(String recipient) {
        return findWhere(String.format("recipient = '%s'", recipient));
    }
}
