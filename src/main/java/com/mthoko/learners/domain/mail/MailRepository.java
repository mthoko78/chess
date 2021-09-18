package com.mthoko.learners.domain.mail;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MailRepository extends JpaRepository<SimpleMail, Long> {

}
