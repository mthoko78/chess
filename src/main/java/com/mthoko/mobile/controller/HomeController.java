package com.mthoko.mobile.controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mthoko.mobile.entity.Address;
import com.mthoko.mobile.entity.LocationStamp;
import com.mthoko.mobile.service.LocationStampService;
import com.mthoko.mobile.service.common.ServiceFactory;
import com.mthoko.mobile.util.DataManager;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Controller
public class HomeController {

	private final LocationStampService service = ServiceFactory.getLocationStampService();
	
	@Value("${spring.datasource.url}")
	private String dbUrl;
	
	@Autowired
	private DataSource dataSource;

	@RequestMapping("/address/{imei}")
	public String addressByImeiFromLatlng(Map<String, Object> model, @PathVariable("imei") String imei) {
		try {
			ArrayList<String> output = new ArrayList<String>();
			LocationStamp locationStamp = service.findMostRecentByImei(imei);
			if (locationStamp != null) {
				double latitude = Double.parseDouble(locationStamp.getLatitude());
				double longitude = Double.parseDouble(locationStamp.getLongitude());
				Address address = DataManager.retrieveAddress(latitude, longitude);
				output.add("Device: " + locationStamp.getDeviceRequested());
				output.add("Imei: " + locationStamp.getImei());
				Date timeCaptured = locationStamp.getTimeCaptured();
				output.add("Time: " + timeCaptured);
				output.add("Country: " + address.getCountry());
				output.add("State: " + address.getState());
				output.add("City: " + address.getCity());
				output.add("PostalCode: " + address.getPostalCode());
				output.add("Street: " + address.getStreet());
				output.add("Latitude: " + latitude);
				output.add("Longitude: " + longitude);
				model.put("records", output);
				model.put("title", "Location Trace");
				model.put("link", getGoogleMapsLink(latitude, longitude));
			}
			return "db";
		} catch (Exception e) {
			model.put("message", e.getMessage());
			return "error";
		}
	}

	public String getGoogleMapsLink(Double latitude, Double longitude) {
		return String.format("https://www.google.co.za/maps/search/%s,%s", latitude, longitude);
	}

	@RequestMapping("/latlng")
	public String addressFromLatlng(Map<String, Object> model, @RequestParam("latitude") Double latitude,
			@RequestParam("longitude") Double longitude) {
		try {
			ArrayList<String> output = new ArrayList<String>();
			Address address = DataManager.retrieveAddress(latitude, longitude);
			output.add("Time: " + new Date(new Date().getTime() + 2 * (1000 * 60 * 60)));
			output.add("Country: " + address.getCountry());
			output.add("State: " + address.getState());
			output.add("City: " + address.getCity());
			output.add("PostalCode: " + address.getPostalCode());
			output.add("Street: " + address.getStreet());
			output.add("Latitude: " + latitude);
			output.add("Longitude: " + longitude);
			model.put("records", output);
			model.put("title", "Location Trace");
			model.put("link", getGoogleMapsLink(latitude, longitude));
			return "db";
		} catch (Exception e) {
			model.put("message", e.getMessage());
			return "error";
		}
	}

	@RequestMapping("/")
	public String index() {
		return "index";
	}

	@RequestMapping("/db")
	public String db(Map<String, Object> model) {
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
