package com.mthoko.mobile.question.answer;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;

import com.mthoko.mobile.common.UniqueEntity;

/**
 *
 * @author Mthoko
 */
@Entity
public class Answer extends UniqueEntity {

	@ElementCollection(targetClass = Character.class)
	private List<Character> selection;

	public Answer() {
	}

	public Answer(List<Character> selection) {
		this.selection = selection;
	}

	public List<Character> getSelection() {
		return selection;
	}

	public void setSelection(List<Character> selection) {
		this.selection = selection;
	}

	@Override
	public String getUniqueIdentifier() {
		String map = selection.stream().map((Function<Character, String>) (c) -> String.valueOf(c))
				.collect(Collectors.joining("|"));
		return map;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (getClass() != obj.getClass())
			return false;
		Answer other = (Answer) obj;
		if (selection == null) {
			return other.selection == null;
		} else if (other.selection == null)
			return false;
		else if (selection.size() != other.selection.size()) {
			return false;
		}
		for (int i = 0; i < selection.size(); i++) {
			if (selection.get(i) != other.selection.get(i))
				return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "Answer [selection=" + selection + ", id=" + getId() + "]";
	}

}
