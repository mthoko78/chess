package com.mthoko.mobile.service;

import com.mthoko.mobile.entity.SimCard;

public interface SimCardService extends BaseService<SimCard> {
    SimCard findBySimNo(String currentSimNo);
}
