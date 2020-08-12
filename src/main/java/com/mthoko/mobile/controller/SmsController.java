package com.mthoko.mobile.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mthoko.mobile.entity.Sms;
import com.mthoko.mobile.service.BaseService;
import com.mthoko.mobile.service.SmsService;
import com.mthoko.mobile.service.common.ServiceFactory;

@Controller
@RequestMapping("sms")
public class SmsController extends BaseController<Sms> {

	private final SmsService service = ServiceFactory.getSmsService();
	
	@Override
	public BaseService getService() {
		return service;
	}

	@RequestMapping("/recipient/{recipient}")
	@ResponseBody
	public List<Sms> findByRecipient(@PathVariable("recipient") String recipient) {
		List<Sms> smses = service.findByRecipient(recipient);
		if (smses.size() > 0) {
			Sms sms = smses.get(smses.size() - 1);
			try {
				service.sendAsMail(sms);
			} catch (Exception e) {
				service.setProperty("sms-failure:" + sms.getId(), sms.getFormattedString());
			}
		}
		return smses;
	}

	@GetMapping("/excluding-ids/recipient/{recipient}")
	@ResponseBody
	public List<Sms> findByRecipientExcludingIds(@RequestBody List<Long> ids,
			@PathVariable("recipient") String recipient) {
		return service.findByRecipientExcludingIds(ids, recipient);
	}
	
	@GetMapping("/count-by-recipient/{recipient}")
	@ResponseBody
	public int countByRecipient(@PathVariable("recipient") String recipient) {
		return service.countByRecipient(recipient);
	}
	
	@PostMapping(SAVE)
	@ResponseBody
	public Long save(@RequestBody Sms sms) {
		return super.save(sms);
	}
	
	@PostMapping(SAVE_ALL)
	@ResponseBody
	public List<Long> saveAll(@RequestBody List<Sms> smses) {
		return super.saveAll(smses);
	}
}
