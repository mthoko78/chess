package com.mthoko.learners.persistence.repository;

import com.mthoko.learners.persistence.entity.QuestionReviewRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QuestionReviewRequestRepo extends JpaRepository<QuestionReviewRequest, Long> {


    Optional<QuestionReviewRequest> findByMember_Id(Long id);
}
