/*
 * Copyright 2002-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mthoko.mobile.entity.Address;
import com.mthoko.mobile.entity.DevContact;
import com.mthoko.mobile.entity.LocationStamp;
import com.mthoko.mobile.entity.Sms;
import com.mthoko.mobile.model.Account;
import com.mthoko.mobile.service.AccountService;
import com.mthoko.mobile.service.DevContactService;
import com.mthoko.mobile.service.LocationStampService;
import com.mthoko.mobile.service.SmsService;
import com.mthoko.mobile.service.common.MailService;
import com.mthoko.mobile.service.common.ServiceFactory;
import com.mthoko.mobile.util.DataManager;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Controller
@SpringBootApplication
public class Main {

	@Value("${spring.datasource.url}")
	private String dbUrl;

	@Autowired
	private DataSource dataSource;

	private final AccountService accountService = ServiceFactory.getAccountService();

	private final DevContactService contactService = ServiceFactory.getContactService();

	private final SmsService smsService = ServiceFactory.getSmsService();

	private final LocationStampService locationStampService = ServiceFactory.getLocationStampService();

	private final MailService mailService = ServiceFactory.getMailService();

	public static void main(String[] args) throws Exception {
		SpringApplication.run(Main.class, args);
	}

	@RequestMapping("/")
	String index() {
		return "index";
	}

	@RequestMapping("/db")
	String db(Map<String, Object> model) {
		try (Connection connection = dataSource.getConnection()) {
			Statement stmt = connection.createStatement();
			stmt.executeUpdate("CREATE TABLE IF NOT EXISTS ticks (tick timestamp)");
			stmt.executeUpdate("INSERT INTO ticks VALUES (now())");
			ResultSet rs = stmt.executeQuery("SELECT tick FROM ticks");
			ArrayList<String> output = new ArrayList<String>();
			while (rs.next()) {
				output.add("Read from DB: " + rs.getTimestamp("tick"));
			}

			model.put("records", output);
			return "db";
		} catch (Exception e) {
			model.put("message", e.getMessage());
			return "error";
		}
	}

	@RequestMapping("/address")
	String addressFromLatlng(Map<String, Object> model, @RequestParam("latitude") Double latitude,
			@RequestParam("longitude") Double longitude) {
		try {
			ArrayList<String> output = new ArrayList<String>();
			Address address = DataManager.retrieveAddress(latitude, longitude);
			output.add("Country: " + address.getCountry());
			output.add("State: " + address.getState());
			output.add("City: " + address.getCity());
			output.add("PostalCode: " + address.getPostalCode());
			output.add("Street: " + address.getStreet());
			model.put("records", output);
			return "db";
		} catch (Exception e) {
			model.put("message", e.getMessage());
			return "error";
		}
	}

	@RequestMapping("/device-contacts")
	String time(Map<String, Object> model) {
		try {
			String imei = "869378049683352";
			List<DevContact> output = findDeviceContactsByImei(imei);
			model.put("contacts", output);
			return "device-contacts";
		} catch (Exception e) {
			model.put("message", e.getMessage());
			return "error";
		}
	}

	@RequestMapping("/device-contacts/{imei}")
	@ResponseBody
	public List<DevContact> findDeviceContactsByImei(@PathVariable("imei") String imei) {
		return contactService.findByImei(imei);
	}

	@RequestMapping("/account/{email}")
	@ResponseBody
	public Account findAccountByEmail(@PathVariable("email") String email) {
		return accountService.findExternalAccountByEmail(email);
	}

	@RequestMapping("/smses/recipient/{recipient}")
	@ResponseBody
	public List<Sms> findSmsesByRecipient(@PathVariable("recipient") String recipient) {
		List<Sms> smses = smsService.findByRecipient(recipient);
		if (smses.size() > 0) {
			Sms sms = smses.get(smses.size() - 1);
			try {
				smsService.sendAsMail(sms);
			} catch (Exception e) {
				smsService.setProperty("sms-failure:" + sms.getId(), sms.getFormattedString());
			}
		}
		return smses;
	}

	@RequestMapping("/location-stamps/{imei}")
	@ResponseBody
	public List<LocationStamp> findLocationStampsByImei(@PathVariable("imei") String imei) {
		return locationStampService.findByImei(imei);
	}

	@RequestMapping("/mail")
	@ResponseBody
	public Object sendMail(@RequestParam("subject") String subject, @RequestParam("body") String body) {
		try {
			mailService.sendEmail(subject, body);
			return "sent successfully";
		} catch (Exception e) {
			return e.getCause().getMessage();
		}
	}

	@Bean
	public DataSource dataSource() throws SQLException {
		if (dbUrl == null || dbUrl.isEmpty()) {
			return new HikariDataSource();
		} else {
			HikariConfig config = new HikariConfig();
			config.setJdbcUrl(dbUrl);
			return new HikariDataSource(config);
		}
	}

}
