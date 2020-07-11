package com.mthoko.mobile.service;

import com.mthoko.mobile.entity.SimCard;
import com.mthoko.mobile.entity.SimContact;

import java.util.List;

public interface SimContactService extends BaseService<SimContact> {
    void integrateContactsExternally(SimCard simCard);

    void integrateContactsInternally(SimCard simCard);

    List<SimContact> retrieveUnverifiedContacts(SimCard simCard);

    List<SimContact> findBySimCardId(Long simCardId);

    List<SimContact> getActualSimContacts();

    List<SimContact> findBySimNo(String simNo);
}
