package com.mthoko.learners.domain.account;

import com.mthoko.learners.common.controller.BaseController;
import com.mthoko.learners.common.service.BaseService;
import com.mthoko.learners.domain.sms.Sms;
import com.mthoko.learners.domain.sms.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("account")
public class AccountController extends BaseController<Account> {

    private final AccountService service;

    private final SmsService smsService;

    @Autowired
    public AccountController(AccountService service, SmsService smsService) {
        this.service = service;
        this.smsService = smsService;
    }

    @Override
    public BaseService<Account> getService() {
        return service;
    }

    @RequestMapping("email/{email}")
    public Optional<Account> findByEmail(@PathVariable("email") String email) {
        return service.findByEmail(email);
    }

    @PostMapping("match")
    public List<Account> findMatchingAccounts(@RequestBody Account account) {
        return service.findMatchingAccounts(account);
    }

    @GetMapping("phone/{phone}")
    public Optional<Account> findByPhone(@PathVariable("phone") String phone) {
        return service.findByPhone(phone);
    }


    @GetMapping("imei/{imei}")
    public Optional<Account> findByImei(@PathVariable("imei") String imei) {
        return service.findByImei(imei);
    }

    @PostMapping("authenticate")
    public ResponseEntity<Account> authenticate(@RequestBody User user) {
        Optional<Account> optionalAccount = service.findByEmail(user.getName());
        if (optionalAccount.isPresent()) {
            Account account = optionalAccount.get();
            if (account.getPassword().equals(user.getPassword())) {
                return ResponseEntity.ok(account);
            }
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("verify-phone/{phoneNumber}")
    public ResponseEntity<PhoneVerification> verifyPhone(@PathVariable("phoneNumber") String phoneNumber) {
        Optional<Account> optionalAccount = service.findByPhone(phoneNumber);
        if (optionalAccount.isPresent()) {
            PhoneVerification verification = service.generateAndSavePhoneVerification(phoneNumber);
            Sms sms = smsService.sendPhoneVerificationSms(phoneNumber, verification);
            verification.setVerificationSms(sms);
            service.savePhoneVerification(verification);
            return ResponseEntity.ok(verification);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("approve-verification/{verificationCode}")
    public ResponseEntity<Account> approveVerification(@PathVariable("verificationCode") String verificationCode) {
        Optional<PhoneVerification> phoneVerificationByCode = service.findPhoneVerificationByCode(verificationCode);
        if (phoneVerificationByCode.isPresent()) {
            PhoneVerification phoneVerification = phoneVerificationByCode.get();
            Optional<Account> optionalAccount = service.findByPhone(phoneVerification.getPhoneNumber());
            if (optionalAccount.isPresent()) {
                Account account = optionalAccount.get();
                if (phoneVerification.getPhoneNumber().equals(account.getMember().getPhone())) {
                    account.setPhoneVerified(true);
                    service.update(account);
                    return ResponseEntity.ok(account);
                }
            }
        }
        return ResponseEntity.notFound().build();
    }


    @PostMapping("register")
    public ResponseEntity<Account> register(@RequestBody Account account) {
        return ResponseEntity.ok(service.register(account));
    }

}
