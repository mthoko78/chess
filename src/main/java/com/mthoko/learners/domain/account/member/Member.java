package com.mthoko.learners.domain.account.member;

import com.mthoko.learners.common.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Member extends BaseEntity {

	private String name;

	private String surname;

	private String phone;

	private String email;

	@Override
	public String getUniqueIdentifier() {
		return email + "|" + phone;
	}

	@Override
	public String toString() {
		return "Member{" +
				"name='" + name + '\'' +
				", surname='" + surname + '\'' +
				", phone='" + phone + '\'' +
				", email='" + email + '\'' +
				'}';
	}
}