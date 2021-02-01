package com.mthoko.mobile.choice;

import javax.persistence.Entity;

import com.mthoko.mobile.common.UniqueEntity;

/**
 *
 * @author Mthoko
 */
@Entity
public final class Choice extends UniqueEntity {

	private char letter;

	private String text;

	public Choice() {
	}

	public Choice(char letter, String text) {
		this.letter = letter;
		this.text = text;
	}

	public char getLetter() {
		return letter;
	}

	public void setLetter(char letter) {
		this.letter = letter;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public String getUniqueIdentifier() {
		return letter + "|" + text;
	}

	@Override
	public String toString() {
		return "Choice [letter=" + letter + ", text=" + text + ", id=" + getId() + "]";
	}

}
