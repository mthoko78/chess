package com.mthoko.learners.persistence.repository;

import com.mthoko.learners.persistence.entity.SimContact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SimContactRepo extends JpaRepository<SimContact, Long> {

    @Query("select count(contact.id) from SimContact contact join SimCard card on contact.simCardId = card.id where card.phone = ?1")
    Integer countByPhone(@Param("phone") String phone);

    List<SimContact> findBySimCardId(Long simCardId);

    @Query("select contact from SimContact contact join SimCard card on contact.simCardId = card.id where card.phone = ?1")
    List<SimContact> findBySimPhone(@Param("phone") String phone);

    List<SimContact> findBySimCardIdAndIdNotIn(Long simCardId, List<Long> ids);

    void deleteBySimCardIdIn(List<Long> simCardIds);

}
