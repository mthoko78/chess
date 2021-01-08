package com.mthoko.mobile.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mthoko.mobile.entity.Sms;
import com.mthoko.mobile.service.BaseService;
import com.mthoko.mobile.service.SmsService;

@RestController
@RequestMapping("sms")
public class SmsController extends BaseController<Sms> {

	public static final String SUCCESS_STATUS = "success";

	@Autowired
	private SmsService service;

	@Override
	public BaseService<Sms> getService() {
		return service;
	}

	@RequestMapping("recipient/{recipient}")
	public List<Sms> findByRecipient(@PathVariable("recipient") String recipient) {
		return service.findByRecipient(recipient);
	}

	@PostMapping("excluding-ids/recipient/{recipient}")
	public List<Sms> findByRecipientExcludingIds(@PathVariable("recipient") String recipient,
			@RequestBody List<Long> ids) {
		return service.findByRecipientExcludingIds(recipient, ids);
	}

	@GetMapping("count-by-recipient/{recipient}")
	public int countByRecipient(@PathVariable("recipient") String recipient) {
		return service.countByRecipient(recipient);
	}

	@PostMapping("send-as-mail")
	public Object sendAsMail(@RequestBody Sms sms) {
		return service.sendAsMail(sms);
	}

	@PostMapping("send-all-as-mail")
	public String sendAllAsMail(@RequestBody List<Sms> smses) {
		Object sendAllAsMail = service.sendAllAsMail(smses);
		if (sendAllAsMail instanceof List) {
			return SUCCESS_STATUS;
		}
		return String.valueOf(sendAllAsMail);
	}

	@PostMapping("forward/to/{to}")
	public String sendForward(@RequestBody Sms sms, @PathVariable("to") String to) {
		return service.sendSms(sms, to);
	}

}
