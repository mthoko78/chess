package com.mthoko.mobile.domain.simcontact;

import com.mthoko.mobile.common.service.BaseService;

import java.util.List;

public interface SimContactService extends BaseService<SimContact> {

    List<SimContact> findBySimCardId(Long simCardId);

    List<SimContact> findByPhoneExcludingIds(List<Long> ids, String phone);

    List<SimContact> optimizeBySimPhone(String phone);

    void deleteBySimCardIdIn(List<Long> simCardIds);

    Integer countByPhone(String phone);

    List<SimContact> findBySimPhone(String phone);
}
