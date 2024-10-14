package com.sample.holiday.service;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.sample.holiday.exception.HolidayApiException;
import com.sample.holiday.model.Holiday;

/**
 * 
 * @author Gopal Haldar Write a program that retrieves the following information
 *         given the data from the API as described on
 *         holiday-api.base-url = https://date.nager.at/api:
 */
@Service
public class HolidayApiClient {
	private static final Logger logger = LoggerFactory.getLogger(HolidayApiClient.class);

	private final RestTemplate restTemplate;
	private final String baseUrl;

	public HolidayApiClient(RestTemplate restTemplate, @Value("${holiday-api.base-url}") String baseUrl) {
		this.restTemplate = restTemplate;
		this.baseUrl = baseUrl;
	}

	/*
	 * In memory cache use to avoid multiple class
	 */
	@Cacheable(value = "holidays", key = "#year + '-' + #countryCode")
	public List<Holiday> getHolidays(int year, String countryCode) {
		String url = String.format("%s/%d/%s", baseUrl, year, countryCode);
		try {
			logger.info("Fetching holidays for {} in {}", countryCode, year);
			Holiday[] holidays = restTemplate.getForObject(url, Holiday[].class);
			return Arrays.asList(holidays);
		} catch (RestClientException ex) {
			logger.error("Error calling holiday API: {}", ex.getMessage());
			throw new HolidayApiException("Failed to fetch holiday data from external API", ex);
		}
	}
}
