package com.mthoko.mobile.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mthoko.mobile.entity.FileInfo;

public interface FileInfoRepo extends JpaRepository<FileInfo, Long> {
}
