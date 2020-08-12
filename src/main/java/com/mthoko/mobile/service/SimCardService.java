package com.mthoko.mobile.service;

import com.mthoko.mobile.entity.SimCard;

public interface SimCardService extends BaseService {

	SimCard findBySimNo(String simNo);
}
