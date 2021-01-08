package com.mthoko.mobile.repo;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mthoko.mobile.entity.Member;

@Repository
public interface MemberRepo extends JpaRepository<Member, Long> {

	@Query("select m from Member m join SimCard s on s.memberId = m.id where s.simNo = ?1")
	Member findBySimNo(@Param("simNo") String simNo);

	Member findByPhone(String phone);

	Member findByEmail(String email);

	int countById(Long id);

	List<Member> findByPhoneIn(Collection<String> phones);

}
