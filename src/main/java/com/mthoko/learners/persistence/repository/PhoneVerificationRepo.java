package com.mthoko.learners.persistence.repository;

import com.mthoko.learners.persistence.entity.PhoneVerification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PhoneVerificationRepo extends JpaRepository<PhoneVerification, Long> {

    Optional<PhoneVerification> findByPhoneNumber(String phone);

    Optional<PhoneVerification> findByVerificationCode(String verificationCode);

    Optional<PhoneVerification> findByVerificationSms_Id(Long id);
}
