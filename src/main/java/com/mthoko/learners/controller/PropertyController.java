package com.mthoko.learners.controller;

import com.mthoko.learners.common.controller.BaseController;
import com.mthoko.learners.service.BaseService;
import com.mthoko.learners.persistence.entity.Property;
import com.mthoko.learners.service.PropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.Optional;

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
	public Optional<Property> findByKey(@PathVariable("key") String key) {
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
