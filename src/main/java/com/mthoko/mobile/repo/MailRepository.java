package com.mthoko.mobile.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mthoko.mobile.entity.SimpleMail;

public interface MailRepository extends JpaRepository<SimpleMail, Long> {

}
