package com.mthoko.mobile.service.internal;

import android.content.Context;

import com.mthoko.mobile.entity.SimCard;
import com.mthoko.mobile.entity.SimContact;
import com.mthoko.mobile.resource.internal.BaseResource;
import com.mthoko.mobile.resource.internal.SimContactResource;
import com.mthoko.mobile.resource.remote.BaseResourceRemote;
import com.mthoko.mobile.resource.remote.SimContactResourceRemote;
import com.mthoko.mobile.service.SimContactService;
import com.mthoko.mobile.util.ConnectionWrapper;
import com.mthoko.mobile.util.DatabaseWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SimContactServiceImpl extends BaseServiceImpl<SimContact> implements SimContactService {

    private final SimContactResource simContactResource;
    private final SimContactResourceRemote simContactResourceRemote;

    public SimContactServiceImpl(Context context) {
        simContactResource = new SimContactResource(context, new DatabaseWrapper());
        simContactResourceRemote = new SimContactResourceRemote(context, new ConnectionWrapper(null));
    }

    @Override
    public BaseResource getResource() {
        return simContactResource;
    }

    @Override
    public BaseResourceRemote getRemoteResource() {
        return simContactResourceRemote;
    }

    public List<SimContact> getActualSimContacts() {
        return simContactResource.getActualSimContacts();
    }

    public List<SimContact> findBySimCardId(Long simCardId) {
        List<SimContact> contacts = simContactResource.findBySimCardId(simCardId);

        return contacts;
    }

    @Override
    public void setContext(Context context) {
        simContactResource.setContext(context);
    }

    public List<SimContact> findBySimNo(String simNo) {
        List<SimContact> contacts = simContactResource.findBySimNo(simNo);
        return contacts;
    }

    public List<SimContact> retrieveUnverifiedContacts(SimCard simCard) {
        List<SimContact> contacts = findBySimCardId(simCard.getId());
        List<SimContact> unverified = new ArrayList<>();
        for (SimContact contact : contacts) {
            if (!contact.isVerified())
                unverified.add(contact);
        }
        return unverified;
    }

    public void integrateContactsInternally(SimCard simCard) {
        if (simCard == null || !simCard.getSimNo().equals(getCurrentSimNo())) {
            return;
        }
        List<SimContact> savedInternalContacts = simContactResource.findBySimNo(simCard.getSimNo());
        List<SimContact> unsavedActualContacts = getActualSimContacts();
        List<SimContact> contactsNotInSim = new ArrayList<>(savedInternalContacts);
        contactsNotInSim.removeAll(unsavedActualContacts);
        unsavedActualContacts.removeAll(savedInternalContacts);
        if (!unsavedActualContacts.isEmpty()) {
            for (SimContact simContact : unsavedActualContacts) {
                simContact.setSimCardId(simCard.getId());
            }
            simContactResource.saveAll(unsavedActualContacts);
        }
        if (!contactsNotInSim.isEmpty()) {
            simContactResource.saveToCurrentSim(contactsNotInSim);
        }

    }

    public void integrateContactsExternally(SimCard simCard) {
        if (simCard == null || !simCard.getSimNo().equals(getCurrentSimNo())) {
            return;
        }
        String simNo = simCard.getSimNo();
        List<SimContact> unverified = simContactResource.findUnverifiedBySimNo(simNo);

        if (!unverified.isEmpty()) {
            verify(simCard, unverified);
        }
        List<SimContact> remoteContacts = findRemoteContactsMissingInternally(simNo);
        if (!remoteContacts.isEmpty()) {
            simContactResource.saveAll(remoteContacts);
            if (simCard.getSimNo().equals(getCurrentSimNo())) {
                // TODO: check if current user owns sim card before the operation below
                simContactResource.saveToCurrentSim(remoteContacts);
            }
        }
    }

    public void verify(SimCard simCard, List<SimContact> contacts) {
        List<SimContact>  unverified = new ArrayList<>(contacts);
        removeVerified(unverified);
        if (unverified.isEmpty()) {
            return;
        }
        String simNo = simCard.getSimNo();

        int count = simContactResourceRemote.countSimContactsBySimNo(simNo);
        if (count > 0) {
            Map<String, Long> verification = simContactResourceRemote.retrieveVerificationBySimNo(simNo);
            verifyAll(unverified, verification);
            removeVerified(unverified);
        }
        if (!unverified.isEmpty()) {
            simContactResourceRemote.saveAll(unverified);
            updateAll(unverified);
        }

    }

    private List<SimContact> findRemoteContactsMissingInternally(String simNo) {
        List<Long> remoteIds = simContactResource.retrieveVerificationIdsBySimNo(simNo);
        List<SimContact> contacts = simContactResourceRemote.findBySimNoWithIdsNotIn(remoteIds, simNo);
        return contacts;
    }

}
