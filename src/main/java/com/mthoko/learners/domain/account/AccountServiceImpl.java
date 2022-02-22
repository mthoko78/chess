package com.mthoko.learners.domain.account;

import com.mthoko.learners.common.entity.UniqueEntity;
import com.mthoko.learners.common.service.BaseServiceImpl;
import com.mthoko.learners.domain.account.credentials.Credentials;
import com.mthoko.learners.domain.account.credentials.CredentialsRepo;
import com.mthoko.learners.domain.account.member.Member;
import com.mthoko.learners.domain.account.member.MemberRepo;
import com.mthoko.learners.domain.devcontact.DevContactRepo;
import com.mthoko.learners.domain.device.DeviceRepo;
import com.mthoko.learners.domain.simcard.SimCard;
import com.mthoko.learners.domain.simcard.SimCardRepo;
import com.mthoko.learners.domain.simcontact.SimContactRepo;
import com.mthoko.learners.domain.sms.SmsRepo;
import com.mthoko.learners.exception.ApplicationException;
import com.mthoko.learners.exception.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl extends BaseServiceImpl<Account> implements AccountService {

    private final AccountRepo accountRepo;

    private final PhoneVerificationRepo phoneVerificationRepo;

    private final MemberRepo memberRepo;

    private final CredentialsRepo credentialsRepo;

    private final SimCardRepo simCardRepo;

    private final SimContactRepo simContactRepo;

    private final DeviceRepo deviceRepo;

    private final DevContactRepo devContactRepo;

    private final SmsRepo smsRepo;

    @Autowired
    public AccountServiceImpl(AccountRepo accountRepo, PhoneVerificationRepo phoneVerificationRepo, MemberRepo memberRepo, CredentialsRepo credentialsRepo,
                              SimCardRepo simCardRepo, SimContactRepo simContactRepo, DeviceRepo deviceRepo,
                              DevContactRepo devContactRepo, SmsRepo smsRepo) {
        this.accountRepo = accountRepo;
        this.phoneVerificationRepo = phoneVerificationRepo;
        this.memberRepo = memberRepo;
        this.credentialsRepo = credentialsRepo;
        this.simCardRepo = simCardRepo;
        this.simContactRepo = simContactRepo;
        this.deviceRepo = deviceRepo;
        this.devContactRepo = devContactRepo;
        this.smsRepo = smsRepo;
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
        memberRepo.save(account.getMember());
        Long memberId = account.getMember().getId();
        account.getCredentials().setMemberId(memberId);
        if (account.getDevices() != null) {
            account.getDevices().forEach(device -> device.setMemberId(memberId));
        }
        if (account.getSimCards() != null) {
            account.getSimCards().forEach(simCard -> simCard.setMemberId(memberId));
        }
        credentialsRepo.save(account.getCredentials());
        deviceRepo.saveAll(account.getDevices());
        simCardRepo.saveAll(account.getSimCards());
        return super.save(account);
    }

    public static <V extends UniqueEntity> V setDateBeforeSave(V entity, Date date) {
        Account account = (Account) entity;
        BaseServiceImpl.setDateBeforeSave(account.getMember(), date);
        BaseServiceImpl.setDateBeforeSave(account.getCredentials(), date);
        BaseServiceImpl.setDateBeforeSave(account.getDevices(), date);
        BaseServiceImpl.setDateBeforeSave(account.getSimCards(), date);
        return BaseServiceImpl.setDateBeforeSave(entity, date);
    }

    @Override
    public List<Account> findMatchingAccounts(Account targetAccount) {
        List<Account> accounts = new ArrayList<>();
        List<Member> members = findMatchingMembersByAccount(targetAccount);
        if (members.size() > 0) {
            members.forEach(member -> accounts.add(findByMemberId(member.getId())));
        }
        return accounts;
    }

    @Override
    public void loadMatchingEntries(Account first, Account second, Set<String> duplicateEntries) {
        if (first.getMember().getEmail().equals(second.getMember().getEmail()))
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
            if (!emailAlreadyInUse && account.getMember().getEmail().equals(matchingAccount.getMember().getEmail())) {
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

    @Override
    public Account findByMemberId(Long memberId) {
        return accountRepo.findByMemberId(memberId);
    }

    private List<Member> findMatchingMembersByAccount(Account targetAccount) {
        Set<String> phones = getAllPhones(targetAccount);
        List<Member> members = new ArrayList<>();
        Optional<Member> optionalMember = memberRepo.findByEmail(targetAccount.getMember().getEmail());
        if (optionalMember.isPresent()) {
            members.add(optionalMember.get());
        }
        if (!phones.isEmpty()) {
            members.addAll(memberRepo.findByPhoneIn(phones));
        }
        return members;
    }

    private Set<String> getAllPhones(Account targetAccount) {
        Set<String> phones = new HashSet<>();
        if (targetAccount.getMember().getPhone() != null) {
            phones.add("'" + targetAccount.getMember().getPhone() + "'");
        }
        if (targetAccount.getSimCards() != null) {
            List<String> additionalPhones = targetAccount
                    .getSimCards()
                    .stream()
                    .map(simCard -> simCard.getPhone())
                    .filter(phone -> phone != null)
                    .collect(Collectors.toList());
            phones.addAll(additionalPhones);
        }
        return phones;
    }

    @Override
    public List<Account> saveAll(List<Account> accounts) {
        memberRepo.saveAll(accounts
                .stream()
                .map(account -> account.getMember())
                .collect(Collectors.toList()));
        accounts
                .stream()
                .filter(account -> account.getDevices() != null)
                .forEach(account -> {
                    account.getDevices().forEach(device -> device.setMemberId(account.getMember().getId()));
                });
        accounts
                .stream()
                .filter(account -> account.getSimCards() != null)
                .forEach(account -> {
                    account.getSimCards().forEach(simCard -> simCard.setMemberId(account.getMember().getId()));
                });
        List<Credentials> credentialsList = accounts
                .stream()
                .map(account -> account.getCredentials().setMemberId(account.getMember().getId()))
                .collect(Collectors.toList());
        credentialsRepo.saveAll(credentialsList);
        simCardRepo.saveAll(accounts
                .stream()
                .filter(account -> account.getSimCards() != null)
                .map(account -> account.getSimCards()
                        .stream()
                        .map(simCard -> simCard.setMemberId(account.getMember().getId()))
                        .collect(Collectors.toList()))
                .reduce(new ArrayList<>(), (simCards, simCards2) -> {
                    simCards.addAll(simCards2);
                    return simCards;
                })
        );
        deviceRepo.saveAll(accounts
                .stream()
                .filter(account -> account.getDevices() != null)
                .map(account -> account.getDevices()
                        .stream()
                        .map(device -> device.setMemberId(account.getMember().getId()))
                        .collect(Collectors.toList()))
                .reduce(new ArrayList<>(), (devices, devices2) -> {
                    devices.addAll(devices2);
                    return devices;
                })
        );
        return super.saveAll(accounts);
    }

    @Override
    public void delete(Account account) {
        deleteSimContacts(account);
        deleteDeviceContacts(account);
        deleteSmses(account);
        simCardRepo.deleteAll(account.getSimCards());
        deviceRepo.deleteAll(account.getDevices());
        credentialsRepo.deleteById(account.getCredentials().getId());
        super.deleteById(account.getId());
        memberRepo.deleteById(account.getMember().getId());
    }

    private void deleteDeviceContacts(Account account) {
        devContactRepo.deleteByDevIdIn(account.getDevices()
                .stream()
                .map(device -> device.getId())
                .collect(Collectors.toList()));
    }

    private void deleteSimContacts(Account account) {
        simContactRepo.deleteBySimCardIdIn(account.getSimCards()
                .stream()
                .map(simCard -> simCard.getId())
                .collect(Collectors.toList()));
    }

    private Object deleteSmses(Account account) {
        return smsRepo.deleteByRecipientIn(account.getSimCards()
                .stream()
                .map(simCard -> simCard.getPhone())
                .collect(Collectors.toList()));
    }

    @Override
    public Account update(Account entity) {
        memberRepo.save(entity.getMember());
        credentialsRepo.save(entity.getCredentials());
        simCardRepo.saveAll(entity.getSimCards());
        deviceRepo.saveAll(entity.getDevices());
        return entity;
    }

    @Override
    public void deleteAll(List<Account> accounts) {
        accounts.forEach(account -> delete(account));
    }

    @Override
    public List<Account> updateAll(List<Account> accounts) {
        return accounts
                .stream()
                .map(account -> update(account))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Account> findByEmail(String email) {
        return accountRepo.findByEmail(email);
    }

    @Override
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
