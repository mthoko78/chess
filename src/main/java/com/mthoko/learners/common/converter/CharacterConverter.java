package com.mthoko.learners.common.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class CharacterConverter implements AttributeConverter<Character, Integer> {

	@Override
	public Integer convertToDatabaseColumn(Character attribute) {
		if (attribute == null) {
			return null;
		}
		return new Integer(attribute);
	}

	@Override
	public Character convertToEntityAttribute(Integer dbData) {
		if (dbData == null) {
			return null;
		}
		return new Character((char) dbData.intValue());
	}

}
