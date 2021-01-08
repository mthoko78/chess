package com.mthoko.mobile.service.internal;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.mthoko.mobile.entity.Account;
import com.mthoko.mobile.entity.Credentials;
import com.mthoko.mobile.entity.Device;
import com.mthoko.mobile.entity.Member;
import com.mthoko.mobile.entity.SimCard;
import com.mthoko.mobile.entity.UniqueEntity;
import com.mthoko.mobile.exception.ApplicationException;
import com.mthoko.mobile.exception.ErrorCode;
import com.mthoko.mobile.repo.AccountRepo;
import com.mthoko.mobile.service.AccountService;
import com.mthoko.mobile.service.CredentialsService;
import com.mthoko.mobile.service.DevContactService;
import com.mthoko.mobile.service.DeviceService;
import com.mthoko.mobile.service.MemberService;
import com.mthoko.mobile.service.SimCardService;
import com.mthoko.mobile.service.SimContactService;
import com.mthoko.mobile.service.SmsService;

@Service
public class AccountServiceImpl extends BaseServiceImpl<Account> implements AccountService {

	@Autowired
	private AccountRepo accountRepo;

	@Autowired
	private MemberService memberService;

	@Autowired
	private CredentialsService credentialsService;

	@Autowired
	private SimCardService simCardService;

	@Autowired
	private SimContactService simContactService;

	@Autowired
	private DeviceService deviceService;

	@Autowired
	private DevContactService devContactService;

	@Autowired
	private SmsService smsService;

	public Account register(Account account) {
		List<Account> matchingAccounts = findMatchingAccounts(account);
		if (matchingAccounts.isEmpty()) {
			return save(account);
		} else {
			ErrorCode errorCode = handleDuplicates(account, matchingAccounts);
			throw new ApplicationException(errorCode);
		}
	}

	@Override
	public Account save(Account account) {
		setDateBeforeSave(account, new Date());
		memberService.save(account.getMember());
		account.setMemberId(account.getMember().getId());
		credentialsService.save(account.getCredentials());
		deviceService.saveAll(account.getDevices());
		simCardService.saveAll(account.getSimCards());
		return super.save(account);
	}
	
	@Override
	public Account setDateBeforeSave(Account account, Date date) {
		memberService.setDateBeforeSave(account.getMember(), date);
		credentialsService.setDateBeforeSave(account.getCredentials(), date);
		deviceService.setDateBeforeSave(account.getDevices(), date);
		simCardService.setDateBeforeSave(account.getSimCards(), date);
		return super.setDateBeforeSave(account, date);
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

	public void loadMatchingEntries(Account first, Account second, Set<String> duplicateEntries) {
		if (first.getEmail().equals(second.getEmail()))
			duplicateEntries.add("email");
		if (first.getMember().getPhone().equals(second.getMember().getPhone()))
			duplicateEntries.add("phone");
		for (SimCard firstSim : first.getSimCards()) {
			for (SimCard secondSim : second.getSimCards()) {
				if (firstSim.getSimNo().equals(secondSim.getSimNo()))
					duplicateEntries.add("sim number : " + firstSim.getSimNo());
				if (firstSim.getPhone().equals(secondSim.getPhone()))
					duplicateEntries.add("phone : " + firstSim.getPhone());
			}
		}
	}

	private ErrorCode handleDuplicates(Account account, List<Account> matchingAccounts) {
		SimCard primarySimCard = account.getPrimarySimCard();
		boolean phoneAlreadyInUse = false;
		boolean emailAlreadyInUse = false;
		// this is the only sim card for this account since unverified accounts cannot
		// have more than one sim card
		for (Account matchingAccount : matchingAccounts) {
			for (SimCard simCard : matchingAccount.getSimCards()) {
				if (!phoneAlreadyInUse && simCard.getPhone().equals(primarySimCard.getPhone())) {
					phoneAlreadyInUse = true;
				}
			}
			if (!emailAlreadyInUse && account.getEmail().equals(matchingAccount.getEmail())) {
				emailAlreadyInUse = true;
			}
			if (!phoneAlreadyInUse && account.getMember().getPhone().equals(matchingAccount.getMember().getPhone())) {
				phoneAlreadyInUse = true;
			}
		}
		ErrorCode errorCode;
		if (emailAlreadyInUse && phoneAlreadyInUse) {
			errorCode = ErrorCode.EMAIL_AND_PHONE_ALREADY_IN_USE;
		} else if (phoneAlreadyInUse) {
			errorCode = ErrorCode.PHONE_ALREADY_IN_USE;
		} else {
			errorCode = ErrorCode.EMAIL_ALREADY_IN_USE;
		}
		return errorCode;
	}

	@Override
	public JpaRepository<Account, Long> getRepo() {
		return accountRepo;
	}

	private Account retrieveAccountByMember(Member member) {
		if (member == null) {
			return null;
		}
		Account account = new Account();
		account.setMember(member);
		account.setSimCards(simCardService.findByMemberId(member.getId()));
		account.setCredentials(credentialsService.findByMemberId(member.getId()));
		account.setDevices(deviceService.findByMemberId(member.getId()));
		return account;
	}

	private List<Member> findMatchingMembersByAccount(Account targetAccount) {
		Set<String> phones = getAllPhones(targetAccount);
		Member member = memberService.findByEmail(targetAccount.getEmail());
		List<Member> members = new ArrayList<>();
		if (member != null) {
			members.add(member);
		}
		if (!phones.isEmpty()) {
			members.addAll(memberService.findByPhoneIn(phones));
		}
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

	public List<Account> saveAll(List<Account> accounts) {
		List<Member> members = new ArrayList<>();
		List<Credentials> credentialsList = new ArrayList<>();
		List<SimCard> simCards = new ArrayList<>();
		List<Device> devices = new ArrayList<>();
		for (Account account : accounts) {
			members.add(account.getMember());
			credentialsList.add(account.getCredentials());
			simCards.addAll(account.getSimCards());
			devices.addAll(account.getDevices());
		}
		memberService.saveAll(members);
		for (Account account : accounts) {
			account.setMemberId(account.getMember().getId());
		}
		credentialsService.saveAll(credentialsList);
		simCardService.saveAll(simCards);
		deviceService.saveAll(devices);
		return super.saveAll(accounts);
	}

	@Override
	public Account delete(Account account) {
		deleteSimContacts(account);
		deleteDeviceContacts(account);
		deleteSmses(account);
		simCardService.deleteAll(account.getSimCards());
		deviceService.deleteAll(account.getDevices());
		credentialsService.delete(account.getCredentials());
		memberService.delete(account.getMember());
		return super.delete(account);
	}

	public void deleteDeviceContacts(Account account) {
		List<Long> deviceIds = new ArrayList<>();
		for (Device device : account.getDevices()) {
			deviceIds.add(device.getId());
		}
		devContactService.deleteByDevIdIn(deviceIds);
	}

	public void deleteSimContacts(Account account) {
		List<Long> simCardIds = new ArrayList<>();
		for (SimCard simCard : account.getSimCards()) {
			simCardIds.add(simCard.getId());
		}
		simContactService.deleteBySimCardIdIn(simCardIds);
	}

	public Object deleteSmses(Account account) {
		List<String> phones = new ArrayList<>();
		for (SimCard simCard : account.getSimCards()) {
			phones.add(simCard.getPhone());
		}
		return smsService.deleteByRecipientIn(phones);
	}

	public Account update(Account entity) {
		memberService.save(entity.getMember());
		credentialsService.save(entity.getCredentials());
		simCardService.saveAll(entity.getSimCards());
		deviceService.saveAll(entity.getDevices());
		return entity;
	}

	public Account findByMember(Member member) {
		if (member == null) {
			return null;
		}
		Credentials credentials = credentialsService.findByMemberId(member.getId());
		List<SimCard> simCards = simCardService.findByMemberId(member.getId());
		List<Device> devices = deviceService.findByMemberId(member.getId());
		return new Account(member, credentials, simCards, devices);
	}

	public List<Account> deleteAll(List<Account> accounts) {
		List<Account> results = new ArrayList<>();
		for (Account account : accounts) {
			results.add(delete(account));
		}
		return results;
	}

	public List<Account> updateAll(List<Account> accounts) {
		for (Account account : accounts) {
			update(account);
		}
		return accounts;
	}

	public Map<String, Long> retrieveVerificationByEmail(String email) {
		return extractVerification(findByEmail(email));
	}

	public Map<String, Long> extractVerification(UniqueEntity entity) {
		Map<String, Long> verification = new HashMap<>();
		if (entity instanceof Account) {
			Account account = (Account) entity;
			return extractVerification(account, verification);
		}
		return verification;
	}

	private Map<String, Long> extractVerification(Account account, Map<String, Long> verification) {
		UniqueEntity.putVerification(account.getMember(), verification);
		UniqueEntity.putVerification(account.getCredentials(), verification);
		UniqueEntity.putVerification(account.getSimCards(), verification);
		UniqueEntity.putVerification(account.getDevices(), verification);
		return verification;
	}

	public Account findBySimNo(String simNo) {
		return accountRepo.findBySimNo(simNo);
	}

	public Account findByEmail(String email) {
		return accountRepo.findByEmail(email);
	}

	public Account findByPhone(String phone) {
		return accountRepo.findByPhone(phone);
	}

}
