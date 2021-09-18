package com.mthoko.learners.domain.simcontact;

import com.mthoko.learners.common.controller.BaseController;
import com.mthoko.learners.common.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("sim-contact")
public class SimContactController extends BaseController<SimContact> {

    @Autowired
    private SimContactService service;

    @Override
    public BaseService<SimContact> getService() {
        return service;
    }

    @GetMapping("count/phone/{phone}")
    public Integer countByPhone(@PathVariable("phone") String phone) {
        return service.countByPhone(phone);
    }

    @GetMapping("simCardId/{simCardId}")
    public List<SimContact> findBySimCardId(@PathVariable("simCardId") Long simCardId) {
        return service.findBySimCardId(simCardId);
    }

    @GetMapping("simPhone/{simPhone}")
    public List<SimContact> findBySimPhone(@PathVariable("simPhone") String phone) {
        return service.findBySimPhone(phone);
    }

    @PostMapping("excluding-ids/phone/{phone}")
    public List<SimContact> findByPhoneExcludingIds(@RequestBody List<Long> ids, @PathVariable("phone") String phone) {
        return service.findByPhoneExcludingIds(ids, phone);
    }

}
