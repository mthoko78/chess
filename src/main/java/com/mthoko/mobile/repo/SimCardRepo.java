package com.mthoko.mobile.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mthoko.mobile.entity.SimCard;

@Repository
public interface SimCardRepo extends JpaRepository<SimCard, Long> {

	SimCard findBySimNo(String simNo);

	List<SimCard> findByMemberId(Long id);

}