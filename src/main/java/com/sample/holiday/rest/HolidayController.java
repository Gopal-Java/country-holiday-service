package com.sample.holiday.rest;

import java.util.List;
import java.util.Map;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sample.holiday.model.Holiday;
import com.sample.holiday.service.HolidayService;

@RestController
@RequestMapping("/api/holidays/v1")
@Validated
public class HolidayController implements HolidayApi {
	private static final Logger log = LoggerFactory.getLogger(HolidayController.class);

	private HolidayService holidayService;

	public HolidayController(HolidayService holidayService) {
		this.holidayService = holidayService;
	}

	public List<Holiday> lastThreeHolidaysForCountry(@Pattern(regexp = "^[A-Z]{2}$") String countryCode,
			@PathVariable Integer year, String transactionId) {
		log.info("Get Last Three Holidays for Country Request Txn UUID : {} Year :{} Country :{}", transactionId, year,
				countryCode);
		return holidayService.getLastThreeHolidays(countryCode, year);
	}

	public Map<String, Long> getHolidaysNotOnWeekends(@PathVariable Integer year,
			@RequestParam @Size(min = 1) List<@Pattern(regexp = "^[A-Z]{2}$") String> countryCodes,
			String transactionId) {
		log.info("Get Holidays for countries(Non Weekends) Request Txn UUID : {} Year :{} Country :{}", transactionId,
				year, countryCodes);
		return holidayService.getHolidaysNotOnWeekends(year, countryCodes);
	}

	public List<Holiday> getCommonHolidaysForTwoCountries(@PathVariable Integer year,
			@RequestParam @Pattern(regexp = "^[A-Z]{2}$") String countryCode1,
			@RequestParam @Pattern(regexp = "^[A-Z]{2}$") String countryCode2, String transactionId) {
		log.info("Get common Holidays for two Country Request Txn UUID : {} Year :{} Country1 :{} and Country2 :{} ",
				transactionId, year, countryCode1, countryCode2);
		return holidayService.getSharedHolidays(year, countryCode1, countryCode2);
	}

	
	/**
	 * Utility Rest API to delete the cache so it will clean up the old data and
	 * refresh the Holidays for countries.
	 */

	/*
	 * Endpoint to purge the cache for all holidays
	 */
	@DeleteMapping("/cache/all")
	@CacheEvict(value = "holidays", allEntries = true)
	public String purgeAllHolidaysCache() {
		return "All holiday cache entries have been purged.";
	}

	/*
	 * Optional: Endpoint to purge cache for a specific year and country code
	 */
	@DeleteMapping("/cache/{year}/{countryCode}")
	@CacheEvict(value = "holidays", key = "#year + '-' + #countryCode")
	public String purgeSpecificHolidaysCache(@PathVariable int year, @PathVariable String countryCode) {
		return String.format("Holiday cache entry for %d-%s has been purged.", year, countryCode);
	}
}
