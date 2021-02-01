package com.mthoko.mobile.property;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mthoko.mobile.common.BaseService;
import com.mthoko.mobile.controller.BaseController;

@RestController
@RequestMapping("property")
public class PropertyController extends BaseController<Property> {

	@Autowired
	private PropertyService service;

	@Override
	public BaseService<Property> getService() {
		return service;
	}

	@GetMapping("key/{key}")
	public Property findByKey(@PathVariable("key") String key) {
		return service.findByKey(key);
	}

	@PutMapping("set")
	public Property setProperty(@PathParam("key") String key, @PathParam("value") String value) {
		return service.setProperty(key, value);
	}

	@PutMapping("unset/key/{key}")
	public String unsetProperty(@PathVariable("key") String key) {
		return service.unsetProperty(key);
	}

}
