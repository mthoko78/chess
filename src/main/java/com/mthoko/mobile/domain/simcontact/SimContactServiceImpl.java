package com.mthoko.mobile.domain.simcontact;

import com.mthoko.mobile.common.service.BaseServiceImpl;
import com.mthoko.mobile.domain.simcard.SimCard;
import com.mthoko.mobile.domain.simcard.SimCardRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
	public Integer countByPhone(String phone) {
		return simContactRepo.countByPhone(phone);
	}

	@Override
	public List<SimContact> findBySimCardId(Long simCardId) {
		return simContactRepo.findBySimCardId(simCardId);
	}

	@Override
	public List<SimContact> findBySimPhone(String phone) {
		return simContactRepo.findBySimPhone(phone);
	}

	@Override
	public List<SimContact> findByPhoneExcludingIds(List<Long> ids, String phone) {
		Optional<SimCard> optionalSimCard = simCardRepo.findByPhone(phone);
		if (!optionalSimCard.isPresent()) {
			return new ArrayList<>();
		}
		return simContactRepo.findBySimCardIdAndIdNotIn(optionalSimCard.get().getId(), ids);
	}

	@Override
	public List<SimContact> optimizeBySimPhone(String phone) {
		List<SimContact> contacts = findBySimPhone(phone);
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
