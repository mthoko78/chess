package com.mthoko.mobile.account;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.mthoko.mobile.account.credentials.Credentials;
import com.mthoko.mobile.account.credentials.CredentialsService;
import com.mthoko.mobile.account.member.Member;
import com.mthoko.mobile.account.member.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.mthoko.mobile.common.BaseServiceImpl;
import com.mthoko.mobile.common.UniqueEntity;
import com.mthoko.mobile.exception.ApplicationException;
import com.mthoko.mobile.exception.ErrorCode;
import com.mthoko.mobile.sms.SmsService;

@Service
public class AccountServiceImpl extends BaseServiceImpl<Account> implements AccountService {

	@Autowired
	private AccountRepo accountRepo;

	@Autowired
	private MemberService memberService;

	@Autowired
	private CredentialsService credentialsService;

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
		return super.save(account);
	}

	@Override
	public Account setDateBeforeSave(Account account, Date date) {
		memberService.setDateBeforeSave(account.getMember(), date);
		credentialsService.setDateBeforeSave(account.getCredentials(), date);
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
	}

	private ErrorCode handleDuplicates(Account account, List<Account> matchingAccounts) {
		boolean phoneAlreadyInUse = false;
		boolean emailAlreadyInUse = false;
		// this is the only sim card for this account since unverified accounts cannot
		// have more than one sim card
		for (Account matchingAccount : matchingAccounts) {
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
		account.setCredentials(credentialsService.findByMemberId(member.getId()));
		return account;
	}

	private List<Member> findMatchingMembersByAccount(Account targetAccount) {
		Member member = memberService.findByEmail(targetAccount.getEmail());
		Member memberByPhone = memberService.findByPhone(targetAccount.getMember().getPhone());
		List<Member> members = new ArrayList<>();
		if (member != null) {
			members.add(member);
		}
		if (!(memberByPhone == null || memberByPhone.equals(member))) {
			members.add(member);
		}
		members.add(memberByPhone);
		return members;
	}

	public List<Account> saveAll(List<Account> accounts) {
		List<Member> members = new ArrayList<>();
		List<Credentials> credentialsList = new ArrayList<>();
		for (Account account : accounts) {
			members.add(account.getMember());
			credentialsList.add(account.getCredentials());
		}
		memberService.saveAll(members);
		for (Account account : accounts) {
			account.setMemberId(account.getMember().getId());
		}
		credentialsService.saveAll(credentialsList);
		return super.saveAll(accounts);
	}

	@Override
	public Account delete(Account account) {
		deleteSmses(account);
		credentialsService.delete(account.getCredentials());
		memberService.delete(account.getMember());
		return super.delete(account);
	}

	public Object deleteSmses(Account account) {
		return smsService.deleteByRecipientIn(Arrays.asList(account.getMember().getPhone()));
	}

	public Account update(Account entity) {
		memberService.save(entity.getMember());
		credentialsService.save(entity.getCredentials());
		return entity;
	}

	public Account findByMember(Member member) {
		if (member == null) {
			return null;
		}
		Credentials credentials = credentialsService.findByMemberId(member.getId());
		return new Account(member, credentials);
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
		return verification;
	}

	public Account findByEmail(String email) {
		return accountRepo.findByEmail(email);
	}

	public Account findByPhone(String phone) {
		return accountRepo.findByPhone(phone);
	}

}
