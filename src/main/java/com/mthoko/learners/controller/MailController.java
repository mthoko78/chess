package com.mthoko.learners.controller;

import com.mthoko.learners.common.controller.BaseController;
import com.mthoko.learners.service.BaseService;
import com.mthoko.learners.service.MailService;
import com.mthoko.learners.persistence.entity.SimpleMail;
import com.mthoko.learners.exception.ApplicationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("mail")
@ResponseBody
public class MailController extends BaseController<SimpleMail> {

    @Autowired
    private MailService service;

    @RequestMapping("send")
    public ResponseEntity<SimpleMail> sendMail(@RequestParam("subject") String subject, @RequestParam("body") String body) {
        try {
            return ResponseEntity.ok(service.sendEmail(subject, body));
        } catch (Exception e) {
            throw new ApplicationException(e);
        }
    }

    @PostMapping("send-simple-mail")
    public ResponseEntity<SimpleMail> sendMail(@RequestBody SimpleMail mail) {
        try {
            return ResponseEntity.ok(service.sendMail(mail));
        } catch (Exception e) {
            throw new ApplicationException(e);
        }
    }

    @Override
    public BaseService<SimpleMail> getService() {
        return service;
    }
}
