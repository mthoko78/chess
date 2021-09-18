package com.mthoko.learners.domain.question.imagematch;

import com.mthoko.learners.common.entity.UniqueEntity;
import com.mthoko.learners.domain.question.image.QuestionImage;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

/**
 *
 * @author Mthoko
 */
@Entity
public final class QuestionImageMatch extends UniqueEntity {

	private int imageNo;

	@OneToOne
	private QuestionImage questionImage;

	public QuestionImageMatch() {
	}

	public QuestionImageMatch(int imageNo, QuestionImage questionImage) {
		this.imageNo = imageNo;
		this.questionImage = questionImage;
	}

	public int getImageNo() {
		return imageNo;
	}

	public void setImageNo(int imageNo) {
		this.imageNo = imageNo;
	}

	public QuestionImage getQuestionImage() {
		return questionImage;
	}

	public void setQuestionImage(QuestionImage questionImage) {
		this.questionImage = questionImage;
	}

	@Override
	public String getUniqueIdentifier() {
		return questionImage + "|" + imageNo;
	}

	@Override
	public String toString() {
		return "QuestionImageMatch [imageNo=" + imageNo + ", questionImage=" + questionImage + ", id=" + getId() + "]";
	}

	@Override
	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}
		if (getClass().equals(o.getClass())) {
			QuestionImageMatch that = (QuestionImageMatch) o;
			if (questionImage == null) {
				return that.questionImage == null;
			}
			return imageNo == that.imageNo && questionImage.equals(that.getQuestionImage());
		}
		return false;
	}

}
