package com.mthoko.learners.persistence.entity;

import com.mthoko.learners.persistence.entity.common.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class QuestionReviewRequest extends BaseEntity {

    public static final String REVIEW_STATUS_OPEN = "open";
    public static final String REVIEW_STATUS_PENDING_CLOSURE = "in progress";
    public static final String REVIEW_STATUS_CLOSED = "closed";

    @OneToOne
    private Member member;

    @OneToOne
    private Question question;

    private Date lastReviewed;

    private String status;

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
