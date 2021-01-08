package com.mthoko.mobile.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mthoko.mobile.entity.SimContact;

@Repository
public interface SimContactRepo extends JpaRepository<SimContact, Long> {

	@Query("select count(contact.id) from SimContact contact join SimCard card on contact.simCardId = card.id where card.simNo = ?1")
	Integer countBySimNo(@Param("simNo") String simNo);

	List<SimContact> findBySimCardId(Long simCardId);

	@Query("select contact from SimContact contact join SimCard card on contact.simCardId = card.id where card.simNo = ?1")
	List<SimContact> findBySimNo(@Param("simNo") String simNo);

	List<SimContact> findBySimCardIdAndIdNotIn(Long simCardId, List<Long> ids);

	void deleteBySimCardIdIn(List<Long> simCardIds);

}
