package com.mthoko.learners.domain.question.review;

import com.mthoko.learners.common.service.BaseService;

import java.util.Optional;

public interface QuestionReviewRequestService extends BaseService<QuestionReviewRequest> {

    Optional<QuestionReviewRequest> findByMemberId(Long id);

    QuestionReviewRequest markAsReviewedRequest(QuestionReviewRequest reviewRequest);

    QuestionReviewRequest handleNewReviewRequest(QuestionReviewRequest reviewRequest);

    QuestionReviewRequest approveReviewedRequest(QuestionReviewRequest reviewRequest);
}
