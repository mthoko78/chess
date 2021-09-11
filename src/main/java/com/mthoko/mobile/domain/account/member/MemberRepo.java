package com.mthoko.mobile.domain.account.member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepo extends JpaRepository<Member, Long> {

	Optional<Member> findByPhone(String phone);

	Optional<Member> findByEmail(String email);

	int countById(Long id);

	List<Member> findByPhoneIn(Collection<String> phones);

}
