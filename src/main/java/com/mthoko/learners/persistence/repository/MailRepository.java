package com.mthoko.learners.persistence.repository;

import com.mthoko.learners.persistence.entity.SimpleMail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MailRepository extends JpaRepository<SimpleMail, Long> {

}
