package com.mthoko.learners.service;

import com.mthoko.learners.persistence.entity.SimContact;

import java.util.List;

public interface SimContactService extends BaseService<SimContact> {

    List<SimContact> findBySimCardId(Long simCardId);

    List<SimContact> findByPhoneExcludingIds(List<Long> ids, String phone);

    List<SimContact> optimizeBySimPhone(String phone);

    void deleteBySimCardIdIn(List<Long> simCardIds);

    Integer countByPhone(String phone);

    List<SimContact> findBySimPhone(String phone);
}
