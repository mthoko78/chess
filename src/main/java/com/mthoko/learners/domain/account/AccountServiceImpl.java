package com.mthoko.learners.domain.account;

import com.mthoko.learners.common.entity.UniqueEntity;
import com.mthoko.learners.common.service.BaseServiceImpl;
import com.mthoko.learners.domain.account.credentials.Credentials;
import com.mthoko.learners.domain.account.credentials.CredentialsService;
import com.mthoko.learners.domain.account.member.Member;
import com.mthoko.learners.domain.account.member.MemberService;
import com.mthoko.learners.domain.devcontact.DevContactService;
import com.mthoko.learners.domain.device.Device;
import com.mthoko.learners.domain.device.DeviceService;
import com.mthoko.learners.domain.simcard.SimCard;
import com.mthoko.learners.domain.simcard.SimCardService;
import com.mthoko.learners.domain.simcontact.SimContactService;
import com.mthoko.learners.domain.sms.SmsService;
import com.mthoko.learners.exception.ApplicationException;
import com.mthoko.learners.exception.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AccountServiceImpl extends BaseServiceImpl<Account> implements AccountService {

    private final AccountRepo accountRepo;

    private final PhoneVerificationRepo phoneVerificationRepo;

    private final MemberService memberService;

    private final CredentialsService credentialsService;

    private final SimCardService simCardService;

    private final SimContactService simContactService;

    private final DeviceService deviceService;

    private final DevContactService devContactService;

    private final SmsService smsService;

    @Autowired
    public AccountServiceImpl(AccountRepo accountRepo, PhoneVerificationRepo phoneVerificationRepo, MemberService memberService, CredentialsService credentialsService,
                              SimCardService simCardService, SimContactService simContactService, DeviceService deviceService,
                              DevContactService devContactService, SmsService smsService) {
        this.accountRepo = accountRepo;
        this.phoneVerificationRepo = phoneVerificationRepo;
        this.memberService = memberService;
        this.credentialsService = credentialsService;
        this.simCardService = simCardService;
        this.simContactService = simContactService;
        this.deviceService = deviceService;
        this.devContactService = devContactService;
        this.smsService = smsService;
    }

    @Override
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
    public Optional<Account> findByImei(String imei) {
        return accountRepo.findByImei(imei);
    }

    @Override
    public Optional<PhoneVerification> findPhoneVerificationByPhoneNumber(String phoneNumber) {
        return phoneVerificationRepo.findByPhoneNumber(phoneNumber);
    }

    @Override
    public PhoneVerification savePhoneVerification(PhoneVerification verification) {
        return phoneVerificationRepo.save(verification);
    }

    @Override
    public String generateVerificationCode() {
        String code = String.valueOf(new Date().getTime());
        int codeLength = 5;
        code = code.substring(code.length() - codeLength);
        return code;
    }

    @Override
    public PhoneVerification generateAndSavePhoneVerification(String phoneNumber) {
        String verificationCode = generateVerificationCode();
        Optional<PhoneVerification> optionalPhoneVerification = findPhoneVerificationByPhoneNumber(phoneNumber);
        PhoneVerification verification;
        if (optionalPhoneVerification.isPresent()) {
            verification = optionalPhoneVerification.get();
            verification.setExpiryDate(new Date());
            verification.setVerificationCode(verificationCode);
        } else {
            verification = new PhoneVerification(phoneNumber, verificationCode, new Date(), null);
        }
        savePhoneVerification(verification);
        return verification;
    }

    @Override
    public Optional<PhoneVerification> findPhoneVerificationByCode(String verificationCode) {
        return phoneVerificationRepo.findByVerificationCode(verificationCode);
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
    public <V extends UniqueEntity> V setDateBeforeSave(V entity, Date date) {
        Account account = (Account) entity;
        memberService.setDateBeforeSave(account.getMember(), date);
        credentialsService.setDateBeforeSave(account.getCredentials(), date);
        deviceService.setDateBeforeSave(account.getDevices(), date);
        simCardService.setDateBeforeSave(account.getSimCards(), date);
        return super.setDateBeforeSave(entity, date);
    }

    @Override
    public List<Account> findMatchingAccounts(Account targetAccount) {
        List<Account> accounts = new ArrayList<>();
        List<Member> members = findMatchingMembersByAccount(targetAccount);
        if (members.size() > 0) {
            for (int i = 0; i < members.size(); i++) {
                accounts.add(findByMemberId(members.get(i).getId()));
            }
        }
        return accounts;
    }

    @Override
    public void loadMatchingEntries(Account first, Account second, Set<String> duplicateEntries) {
        if (first.getEmail().equals(second.getEmail()))
            duplicateEntries.add("email");
        if (first.getMember().getPhone().equals(second.getMember().getPhone()))
            duplicateEntries.add("phone");
        for (SimCard firstSim : first.getSimCards()) {
            for (SimCard secondSim : second.getSimCards()) {
                if (firstSim.getPhone().equals(secondSim.getPhone()))
                    duplicateEntries.add("phone : " + firstSim.getPhone());
            }
        }
    }

    private ErrorCode handleDuplicates(Account account, List<Account> matchingAccounts) {
        boolean phoneAlreadyInUse = false;
        boolean emailAlreadyInUse = false;
        // this is the only sim card for this account since unverified accounts cannot
        // have more than one sim card
        for (Account matchingAccount : matchingAccounts) {
            for (SimCard simCard : matchingAccount.getSimCards()) {
                if (!phoneAlreadyInUse && simCard.getPhone().equals(account.getPrimaryPhone())) {
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

    private Account retrieveAccountByMemberId(Long memberId) {
        if (memberId == null) {
            return null;
        }
        return findByMemberId(memberId);
    }

    @Override
    public Account findByMemberId(Long memberId) {
        return accountRepo.findByMemberId(memberId);
    }

    private List<Member> findMatchingMembersByAccount(Account targetAccount) {
        Set<String> phones = getAllPhones(targetAccount);
        List<Member> members = new ArrayList<>();
        Optional<Member> optionalMember = memberService.findByEmail(targetAccount.getEmail());
        if (optionalMember.isPresent()) {
            members.add(optionalMember.get());
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

    @Override
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

        credentialsService.deleteById(account.getCredentials().getId());
        super.deleteById(account.getId());
        memberService.deleteById(account.getMember().getId());
        return account;
    }

    private void deleteDeviceContacts(Account account) {
        List<Long> deviceIds = new ArrayList<>();
        for (Device device : account.getDevices()) {
            deviceIds.add(device.getId());
        }
        devContactService.deleteByDevIdIn(deviceIds);
    }

    private void deleteSimContacts(Account account) {
        List<Long> simCardIds = new ArrayList<>();
        for (SimCard simCard : account.getSimCards()) {
            simCardIds.add(simCard.getId());
        }
        simContactService.deleteBySimCardIdIn(simCardIds);
    }

    private Object deleteSmses(Account account) {
        List<String> phones = new ArrayList<>();
        for (SimCard simCard : account.getSimCards()) {
            phones.add(simCard.getPhone());
        }
        return smsService.deleteByRecipientIn(phones);
    }

    @Override
    public Account update(Account entity) {
        memberService.save(entity.getMember());
        credentialsService.save(entity.getCredentials());
        simCardService.saveAll(entity.getSimCards());
        deviceService.saveAll(entity.getDevices());
        return entity;
    }

    @Override
    public List<Account> deleteAll(List<Account> accounts) {
        List<Account> results = new ArrayList<>();
        for (Account account : accounts) {
            results.add(delete(account));
        }
        return results;
    }

    @Override
    public List<Account> updateAll(List<Account> accounts) {
        for (Account account : accounts) {
            update(account);
        }
        return accounts;
    }

    private Map<String, Long> extractVerification(Account account, Map<String, Long> verification) {
        UniqueEntity.putVerification(account.getMember(), verification);
        UniqueEntity.putVerification(account.getCredentials(), verification);
        UniqueEntity.putVerification(account.getSimCards(), verification);
        UniqueEntity.putVerification(account.getDevices(), verification);
        return verification;
    }

    public Optional<Account> findByEmail(String email) {
        return accountRepo.findByEmail(email);
    }

    public Optional<Account> findByPhone(String phone) {
        return accountRepo.findByPhone(phone);
    }

    @Override
    public void deleteById(Long id) {
        Optional<Account> accountOptional = findById(id);
        if (accountOptional.isPresent()) {
            delete(accountOptional.get());
        }
    }
}
