package com.mthoko.learners.domain.question.review;

import com.mthoko.learners.common.controller.BaseController;
import com.mthoko.learners.common.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("questionReviewRequest")
public class QuestionReviewRequestController extends BaseController<QuestionReviewRequest> {

    private final QuestionReviewRequestService requestService;

    @Autowired
    public QuestionReviewRequestController(QuestionReviewRequestService requestService) {
        this.requestService = requestService;
    }

    @PostMapping("new")
    public ResponseEntity<QuestionReviewRequest> handleRequest(@RequestBody QuestionReviewRequest reviewRequest) {
        return ResponseEntity.ok(requestService.handleNewReviewRequest(reviewRequest));
    }

    @PutMapping("mark-reviewed")
    public QuestionReviewRequest markAsReviewedRequest(@RequestBody QuestionReviewRequest reviewRequest) {
        return requestService.markAsReviewedRequest(reviewRequest);
    }

    @DeleteMapping("approve")
    public QuestionReviewRequest approveReviewedRequest(@RequestBody QuestionReviewRequest reviewRequest) {
        return requestService.approveReviewedRequest(reviewRequest);
    }

    @PutMapping("mark-all-reviewed")
    public List<QuestionReviewRequest> markAsReviewedRequests(@RequestBody List<QuestionReviewRequest> reviewRequests) {
        return requestService.markAsReviewedRequest(reviewRequests);
    }

    @PutMapping("approve-all")
    public List<QuestionReviewRequest> approveReviewedRequests(@RequestBody List<QuestionReviewRequest> reviewRequests) {
        return requestService.approveReviewedRequest(reviewRequests);
    }

    @PostMapping("ids")
    List<QuestionReviewRequest> findByIds(@RequestBody List<Long> ids) {
        return requestService.findByIds(ids);
    }

    @Override
    public BaseService<QuestionReviewRequest> getService() {
        return requestService;
    }
}
