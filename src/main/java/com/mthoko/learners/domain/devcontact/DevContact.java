package com.mthoko.learners.domain.devcontact;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mthoko.learners.common.entity.UniqueEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Entity
public class DevContact extends UniqueEntity {

	private Long devId;

	@Column(length = 1024)
	private byte[] name;

	@OneToMany
	private List<DevContactValue> values;

	public DevContact() {
		this.values = new ArrayList<>();
	}

	public Long getDevId() {
		return devId;
	}

	public void setDevId(Long devId) {
		this.devId = devId;
	}

	public String getName() {
		return byteArrayToString(name);
	}

	public void setName(String name) {
		this.name = stringToByteArray(name);
	}

	@JsonIgnore
	public Map<Integer, String> getPhones() {
		Map<Integer, String> phones = new LinkedHashMap<>();
		for (DevContactValue value : values) {
			if (value.getSource() < 100) {
				phones.put(value.getSource(), value.getValue());
			}
		}
		return phones;
	}

	@JsonIgnore
	public Map<Integer, String> getEmails() {
		Map<Integer, String> phones = new LinkedHashMap<>();
		for (DevContactValue value : values) {
			if (value.getSource() >= 100) {
				phones.put(value.getSource(), value.getValue());
			}
		}
		return phones;
	}

	public List<DevContactValue> getValues() {
		return values;
	}

	public void setValues(List<DevContactValue> values) {
		if (values == null) {
			return;
		}
		this.values.clear();
		this.values.addAll(values);
	}

	public void addContactValue(DevContactValue value) {
		if (value != null) {
			this.values.add(value);
		}
	}

	@Override
	public String getUniqueIdentifier() {
		return getUniqueIdentifierByList(getValues());
	}

	@Override
	public String toString() {
		return "DevContact [id=" + getId() + ", devId=" + devId + ", name=" + getName() + ", values=" + values + "]";
	}

}
