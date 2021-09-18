package com.mthoko.learners.domain.question.review;

import com.mthoko.learners.common.entity.UniqueEntity;
import com.mthoko.learners.domain.account.member.Member;
import com.mthoko.learners.domain.question.Question;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.Date;
import java.util.List;

@Entity
public class QuestionReviewRequest extends UniqueEntity {

    public final String REVIEW_STATUS_OPEN = "open";
    public final String REVIEW_STATUS_IN_PROGRESS = "closed";
    public final String REVIEW_STATUS_CLOSED = "closed";

    @OneToOne
    private Member member;

    @OneToMany
    private List<Question> questions;

    private Date lastReviewDate;

    private String status;

    @Override
    public String getUniqueIdentifier() {
        return null;
    }
}
