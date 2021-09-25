package com.mthoko.learners.domain.mail;

import com.mthoko.learners.common.controller.BaseController;
import com.mthoko.learners.common.service.BaseService;
import com.mthoko.learners.exception.ApplicationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("mail")
@ResponseBody
public class MailController extends BaseController<com.mthoko.learners.domain.mail.SimpleMail> {

    @Autowired
    private MailService service;

    @RequestMapping("send")
    public ResponseEntity<com.mthoko.learners.domain.mail.SimpleMail> sendMail(@RequestParam("subject") String subject, @RequestParam("body") String body) {
        try {
            return ResponseEntity.ok(service.sendEmail(subject, body));
        } catch (Exception e) {
            throw new ApplicationException(e);
        }
    }

    @PostMapping("send-simple-mail")
    public ResponseEntity<com.mthoko.learners.domain.mail.SimpleMail> sendMail(@RequestBody com.mthoko.learners.domain.mail.SimpleMail mail) {
        try {
            return ResponseEntity.ok(service.sendMail(mail));
        } catch (Exception e) {
            throw new ApplicationException(e);
        }
    }

    @Override
    public BaseService<com.mthoko.learners.domain.mail.SimpleMail> getService() {
        return service;
    }
}
