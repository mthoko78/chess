package com.mthoko.mobile.service.internal;

import java.util.Comparator;

import org.springframework.stereotype.Service;

import com.mthoko.mobile.entity.DevContactValue;

@Service
public class ContactComparator implements Comparator<DevContactValue> {

	@Override
	public int compare(DevContactValue o1, DevContactValue o2) {
		if (o1.getId() == null) {
			return (o2.getId() == null) ? 0 : -1;
		}
		if (o2.getId() == null) {
			return 1;
		}
		return o1.getId() < o2.getId() ? -1 : 1;
	}

}
