package com.mthoko.learners.persistence.repository;

import com.mthoko.learners.persistence.entity.FileInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileInfoRepo extends JpaRepository<FileInfo, Long> {
}
