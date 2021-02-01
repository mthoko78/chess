package com.mthoko.mobile.mail;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MailRepository extends JpaRepository<SimpleMail, Long> {

}
