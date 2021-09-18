package com.mthoko.learners.domain.question.review;

import com.mthoko.learners.common.entity.UniqueEntity;
import com.mthoko.learners.domain.account.member.Member;
import com.mthoko.learners.domain.question.Question;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import java.util.Date;

@Entity
public class QuestionReviewRequest extends UniqueEntity {

    public static final String REVIEW_STATUS_OPEN = "open";
    public static final String REVIEW_STATUS_PENDING_CLOSURE = "in progress";
    public static final String REVIEW_STATUS_CLOSED = "closed";

    @OneToOne
    private Member member;

    @OneToOne
    private Question question;

    private Date lastReviewed;

    private String status;

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public Date getLastReviewed() {
        return lastReviewed;
    }

    public void setLastReviewed(Date lastReviewed) {
        this.lastReviewed = lastReviewed;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String getUniqueIdentifier() {
        return member.getUniqueIdentifier();
    }

    @Override
    public String toString() {
        return "QuestionReviewRequest{" +
                "member=" + member +
                ", questions=" + question +
                ", lastReviewed=" + lastReviewed +
                ", status='" + status + '\'' +
                '}';
    }
}
