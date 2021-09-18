package com.mthoko.learners.domain.file;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FileInfoRepo extends JpaRepository<FileInfo, Long> {
}
