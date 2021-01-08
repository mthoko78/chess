package com.mthoko.mobile.service.internal;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.mthoko.mobile.entity.FileInfo;
import com.mthoko.mobile.entity.RecordedCall;
import com.mthoko.mobile.repo.FileInfoRepo;
import com.mthoko.mobile.repo.RecordedCallRepo;
import com.mthoko.mobile.service.RecordedCallService;

@Service
public class RecordedCallServiceImpl extends BaseServiceImpl<RecordedCall> implements RecordedCallService {

	public static final String INCOMING = "i";
	public static final String OUTGOING = "o";
	public static final String END_OF_INCOMING_CALL = "end-" + INCOMING;
	public static final String END_OF_OUTGOING_CALL = "end-" + OUTGOING;

	@Autowired
	private RecordedCallRepo recordedCallRepo;

	@Autowired
	private FileInfoRepo fileInfoRepo;

	public String getTimestampFromFile(File file) {
		String timeStamp = file.getAbsolutePath();
		String left = timeStamp.substring(0, timeStamp.indexOf("~"));
		left = left.substring(left.lastIndexOf("-") + 1);
		timeStamp = left + timeStamp.substring(timeStamp.indexOf("~"));
		timeStamp = timeStamp.substring(0, timeStamp.indexOf("~")).replaceAll("~", ":");
		return timeStamp;
	}

	public String getDurationToString(int duration) {
		long startMin = TimeUnit.MILLISECONDS.toMinutes((long) duration);
		long startSec = TimeUnit.MILLISECONDS.toSeconds((long) duration)
				- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) duration));
		return (String.format("%s:%s", (startMin < 10 ? "0" : "") + startMin, (startSec < 10 ? "0" : "") + startSec));
	}

	@Override
	public JpaRepository<RecordedCall, Long> getRepo() {
		return this.recordedCallRepo;
	}

	@Override
	public List<RecordedCall> findByCallerOrSimNoOrImeiExcludingIds(List<Long> ids, String caller, String simNo,
			String imei) {
		return recordedCallRepo.findByCallerOrSimNoOrImei(caller, simNo, imei).stream()
				.filter((contact) -> !ids.contains(contact.getId())).collect(Collectors.toList());
	}

	@Override
	public List<RecordedCall> findByCallerOrSimNoOrImei(String caller, String simNo, String imei) {
		return recordedCallRepo.findByCallerOrSimNoOrImei(caller, simNo, imei);
	}

	@Override
	public RecordedCall save(RecordedCall entity) {
		if (entity.getFileInfo() != null) {
			fileInfoRepo.save(entity.getFileInfo());
		}
		return super.save(entity);
	}

	@Override
	public List<RecordedCall> saveAll(List<RecordedCall> entities) {
		fileInfoRepo.saveAll(extractFileInfos(entities));
		return super.saveAll(entities);
	}

	private List<FileInfo> extractFileInfos(List<RecordedCall> entities) {
		List<FileInfo> fileInfos = new ArrayList<>();
		entities.forEach((call) -> {
			if (call.getFileInfo() != null) {
				fileInfos.add(call.getFileInfo());
			}
		});
		return fileInfos;
	}

}