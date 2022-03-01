package com.mthoko.learners.service;

import com.mthoko.learners.persistence.entity.QuestionReviewRequest;

import java.util.List;
import java.util.Optional;

public interface QuestionReviewRequestService extends BaseService<QuestionReviewRequest> {

    Optional<QuestionReviewRequest> findByMemberId(Long id);

    QuestionReviewRequest markAsReviewedRequest(QuestionReviewRequest reviewRequest);

    QuestionReviewRequest handleNewReviewRequest(QuestionReviewRequest reviewRequest);

    QuestionReviewRequest approveReviewedRequest(QuestionReviewRequest reviewRequest);

    List<QuestionReviewRequest> markAsReviewedRequest(List<QuestionReviewRequest> reviewRequests);

    List<QuestionReviewRequest> approveReviewedRequest(List<QuestionReviewRequest> reviewRequests);

    List<QuestionReviewRequest> findByIds(List<Long> ids);
}
