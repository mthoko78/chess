package com.mthoko.mobile.account;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

import com.mthoko.mobile.common.UniqueEntity;

@Entity
public class Account extends UniqueEntity {

	@OneToOne
	private Member member;

	@OneToOne
	private Credentials credentials;

	public Account() {
		this(new Member(), new Credentials());
	}

	public Account(Member member, Credentials credentials) {
		this.credentials = credentials;
		this.member = member;
	}

	public Member getMember() {
		return this.member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public Credentials getCredentials() {
		return credentials;
	}

	public void setCredentials(Credentials credentials) {
		this.credentials = credentials;
	}

	public String getPassword() {
		return getCredentials() == null ? null : getCredentials().getPassword();
	}

	public void setPassword(String password) {
		if (credentials != null) {
			getCredentials().setPassword(password);
		}
	}

	public String getEmail() {
		return getMember() == null ? null : getMember().getEmail();
	}

	public void setEmail(String email) {
		if (member != null) {
			member.setEmail(email);
		}
	}

	@Override
	public String getUniqueIdentifier() {
		if (member != null) {
			return member.getUniqueIdentifier();
		}
		return getClass().getName() + ":" + getId();
	}

	public void setMemberId(Long memberId) {
		setId(memberId);
		if (member != null) {
			member.setId(memberId);
		}
		if (credentials != null) {
			credentials.setMemberId(memberId);
		}
	}

	@Override
	public String toString() {
		return "Account [id=" + getId() + ", member=" + member + ", credentials=" + credentials + "]";
	}

}