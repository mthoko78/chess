package com.mthoko.mobile.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mthoko.mobile.service.common.MailService;
import com.mthoko.mobile.service.common.ServiceFactory;

@Controller
@RequestMapping("mail")
public class MailController {

	private final MailService service = ServiceFactory.getMailService();

	@RequestMapping("/send")
	@ResponseBody
	public Object sendMail(@RequestParam("subject") String subject, @RequestParam("body") String body) {
		try {
			service.sendEmail(subject, body);
			return "sent successfully";
		} catch (Exception e) {
			return e.getCause().getMessage();
		}
	}
}
