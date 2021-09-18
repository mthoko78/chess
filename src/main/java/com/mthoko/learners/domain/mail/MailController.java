package com.mthoko.learners.domain.mail;

import com.mthoko.learners.common.controller.BaseController;
import com.mthoko.learners.common.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("mail")
@ResponseBody
public class MailController extends BaseController<SimpleMail> {

	@Autowired
	private MailService service;

	@RequestMapping("send")
	public Object sendMail(@RequestParam("subject") String subject, @RequestParam("body") String body) {
		try {
			service.sendEmail(subject, body);
			return "sent successfully";
		} catch (Exception e) {
			return e.getCause().getMessage();
		}
	}

	@PostMapping("send-simple-mail")
	public Object sendMail(@RequestBody SimpleMail mail) {
		try {
			service.sendEmail(mail);
			return "sent successfully";
		} catch (Exception e) {
			return e.getCause().getMessage();
		}
	}

	@Override
	public BaseService<SimpleMail> getService() {
		return service;
	}
}
