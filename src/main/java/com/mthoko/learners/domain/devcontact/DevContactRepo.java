package com.mthoko.learners.domain.devcontact;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DevContactRepo extends JpaRepository<DevContact, Long> {

	@Query("select contact from DevContact contact join Device device on contact.devId = device.id where device.imei = ?1")
	List<DevContact> findByImei(@Param("imei") String imei);

	@Query("select contact from DevContact contact join Device device on contact.devId = device.id where device.imei = ?1 and contact.id not in (?2)")
	List<DevContact> findByImeiAndIdNotIn(String imei, List<Long> ids);

	@Query("select count(contact) from DevContact contact join Device device on contact.devId = device.id where device.imei = ?1")
	int countByImei(String imei);

	@Modifying
	void deleteByDevIdIn(List<Long> deviceIds);

	List<DevContact> findByDevId(Long devId);

	List<DevContact> findByDevIdAndIdNotIn(Long devId, List<Long> excludeIds);

}