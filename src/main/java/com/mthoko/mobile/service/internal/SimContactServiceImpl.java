package com.mthoko.mobile.service.internal;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.mthoko.mobile.entity.SimContact;
import com.mthoko.mobile.repo.SimCardRepo;
import com.mthoko.mobile.repo.SimContactRepo;
import com.mthoko.mobile.service.SimContactService;

@Service
public class SimContactServiceImpl extends BaseServiceImpl<SimContact> implements SimContactService {

	@Autowired
	private SimContactRepo simContactRepo;

	@Autowired
	private SimCardRepo simCardRepo;

	@Override
	public JpaRepository<SimContact, Long> getRepo() {
		return simContactRepo;
	}

	@Override
	public Integer countBySimNo(String simNo) {
		return simContactRepo.countBySimNo(simNo);
	}

	@Override
	public List<SimContact> findBySimCardId(Long simCardId) {
		return simContactRepo.findBySimCardId(simCardId);
	}

	@Override
	public List<SimContact> findBySimNo(String simNo) {
		return simContactRepo.findBySimNo(simNo);
	}

	@Override
	public List<SimContact> findBySimNoExcludingIds(List<Long> ids, String simNo) {
		return simContactRepo.findBySimCardIdAndIdNotIn(simCardRepo.findBySimNo(simNo).getId(), ids);
	}

	@Override
	public List<SimContact> optimizeBySimNo(String simNo) {
		List<SimContact> contacts = findBySimNo(simNo);
		List<SimContact> redundantContacts = extractRedundant(contacts);
		removeAll(contacts, redundantContacts);
		deleteAll(redundantContacts);
		return contacts;
	}

	private List<SimContact> extractRedundant(List<SimContact> contacts) {
		List<String> uniqueIds = new ArrayList<>();
		List<SimContact> duplicateContacts = new ArrayList<>();
		for (SimContact contact : contacts) {
			String uniqueIdentifier = contact.getUniqueIdentifier();
			if (!uniqueIds.contains(uniqueIdentifier)) {
				uniqueIds.add(uniqueIdentifier);
			} else {
				duplicateContacts.add(contact);
			}
		}
		return duplicateContacts;
	}

	@Override
	public void deleteBySimCardIdIn(List<Long> simCardIds) {
		simContactRepo.deleteBySimCardIdIn(simCardIds);
	}
}
