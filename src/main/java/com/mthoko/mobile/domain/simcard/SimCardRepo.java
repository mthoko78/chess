package com.mthoko.mobile.domain.simcard;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SimCardRepo extends JpaRepository<SimCard, Long> {

	List<SimCard> findByMemberId(Long id);

	Optional<SimCard> findByPhone(String phone);
}