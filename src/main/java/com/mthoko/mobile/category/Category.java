package com.mthoko.mobile.category;

import javax.persistence.Entity;

import com.mthoko.mobile.common.UniqueEntity;

/**
 *
 * @author Mthoko
 */
@Entity
public final class Category extends UniqueEntity {

	private String name;

	private Integer number;

	private int totalQuestions;

	public Category() {
	}

	public Category(String name, Integer number, int totalQuestions) {
		this.name = name;
		this.number = number;
		this.totalQuestions = totalQuestions;
	}

	public int getTotalQuestions() {
		return totalQuestions;
	}

	public void setTotalQuestions(int totalQuestions) {
		this.totalQuestions = totalQuestions;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	@Override
	public int compareTo(UniqueEntity that) {
		if (that.getClass() != getClass()) {
			return -1;
		}
		return this.name.compareTo(((Category) that).getName());
	}

	@Override
	public boolean equals(Object that) {
		return this.compareTo((Category) that) == 0;
	}

	@Override
	public String getUniqueIdentifier() {
		return String.valueOf(name);
	}

	@Override
	public String toString() {
		return "Category [name=" + name + ", number=" + number + ", totalQuestions=" + totalQuestions + ", id="
				+ getId() + "]";
	}

}