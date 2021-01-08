package com.mthoko.mobile.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.mthoko.mobile.entity.RecordedCall;

@Repository
public interface RecordedCallRepo extends JpaRepository<RecordedCall, Long> {

	@Query("select call from RecordedCall call where (call.caller = ?2 or call.receiverSimNo = ?3 or call.receiverImei = ?4) and call.id not in (?1)")
	List<RecordedCall> findByCallerOrSimNoOrImeiExcludingIds(List<Long> ids, String caller, String simNo,
			String imei);

	@Query("select call from RecordedCall call where call.caller = ?1 or call.receiverSimNo = ?2 or call.receiverImei = ?3")
	List<RecordedCall> findByCallerOrSimNoOrImei(String caller, String simNo,
			String imei);
}