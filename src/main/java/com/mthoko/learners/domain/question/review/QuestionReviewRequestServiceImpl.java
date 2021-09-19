package com.mthoko.learners.domain.question.review;

import com.mthoko.learners.common.service.BaseServiceImpl;
import com.mthoko.learners.domain.question.QuestionService;
import com.mthoko.learners.domain.sms.Sms;
import com.mthoko.learners.domain.sms.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class QuestionReviewRequestServiceImpl extends BaseServiceImpl<QuestionReviewRequest> implements QuestionReviewRequestService {

    private final QuestionReviewRequestRepo repo;

    private final SmsService smsService;

    private final QuestionService questionService;

    @Autowired
    public QuestionReviewRequestServiceImpl(QuestionReviewRequestRepo repo, SmsService smsService
            , QuestionService questionService) {
        this.repo = repo;
        this.smsService = smsService;
        this.questionService = questionService;
    }

    @Override
    public Optional<QuestionReviewRequest> findByMemberId(Long id) {
        return repo.findByMember_Id(id);
    }

    @Override
    @Transactional
    public QuestionReviewRequest handleNewReviewRequest(QuestionReviewRequest reviewRequest) {
        reviewRequest.setStatus(reviewRequest.REVIEW_STATUS_OPEN);
        QuestionReviewRequest save = save(reviewRequest);
        String body = String.format(
                "New review request for question '%s' %s/question/%s from %s (%s) " +
                        "Please login to review and close the query.",
                reviewRequest.getQuestion().getText(),
                getAppProperty("app.url"),
                reviewRequest.getQuestion().getId(),
                reviewRequest.getMember().getName(),
                reviewRequest.getMember().getPhone()
        );
        sendSms(getAppProperty("clickatel.sms.defaultRecipient"), body);
        return save;
    }

    @Override
    @Transactional
    public QuestionReviewRequest markAsReviewedRequest(QuestionReviewRequest reviewRequest) {
        reviewRequest.setStatus(reviewRequest.REVIEW_STATUS_PENDING_CLOSURE);
        QuestionReviewRequest updated = update(reviewRequest);
        String body = String.format(
                "Hi, %s. Please note the question '%s' has been reviewed. " +
                        "Please login to approve.",
                updated.getMember().getName(),
                updated.getQuestion().getText()
        );
        sendSms(updated.getMember().getPhone(), body);
        return updated;
    }

    @Override
    public QuestionReviewRequest approveReviewedRequest(QuestionReviewRequest reviewRequest) {
        reviewRequest.setStatus(reviewRequest.REVIEW_STATUS_CLOSED);
        delete(reviewRequest);
        String body = String.format(
                "Hi, %s. Please note the question '%s' has been reviewed and the query has been closed. " +
                        reviewRequest.getQuestion().getText(),
                reviewRequest.getMember().getName(),
                reviewRequest.getQuestion().getText()
        );
        sendSms(reviewRequest.getMember().getPhone(), body);
        return reviewRequest;
    }

    @Override
    @Transactional
    public QuestionReviewRequest update(QuestionReviewRequest reviewRequest) {
        questionService.update(reviewRequest.getQuestion());
        QuestionReviewRequest update = super.update(setDateBeforeUpdate(reviewRequest, reviewRequest.getQuestion().getLastModified()));
        repo.save(reviewRequest);
        return update;
    }

    @Override
    public JpaRepository<QuestionReviewRequest, Long> getRepo() {
        return repo;
    }

    @Override
    @Transactional
    public QuestionReviewRequest save(QuestionReviewRequest reviewRequest) {
        return repo.save(reviewRequest);
    }

    private void sendSms(String phone, String body) {
        Sms sms = new Sms();
        sms.setRecipient(phone);
        sms.setBody(body);
        smsService.sendSms(sms);
    }
}
