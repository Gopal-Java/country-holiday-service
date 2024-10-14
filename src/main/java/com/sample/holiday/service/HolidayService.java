package com.sample.holiday.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.sample.holiday.model.Holiday;

/**
 * 
 * @author Gopal Haldar This is service class for all three requirement, after
 *         getting the data it filters.
 * 
 *         Write a program that retrieves the following information given the
 *         data from the API as described on https://date.nager.at/Api:
 *
 */
@Service
public class HolidayService {
	private static final Logger logger = LoggerFactory.getLogger(HolidayService.class);
	private final HolidayApiClient holidayApiClient;

	public HolidayService(HolidayApiClient holidayApiClient) {
		this.holidayApiClient = holidayApiClient;
	}

	public List<Holiday> getLastThreeHolidays(String countryCode, Integer year) {
		logger.info("Getting last three holidays for {} in {}", countryCode, year);
		List<Holiday> holidays = holidayApiClient.getHolidays(year, countryCode);
		return holidays.stream().filter(holiday -> holiday.getDate().isBefore(LocalDate.now()))
				.sorted(Comparator.comparing(Holiday::getDate).reversed()).limit(3).collect(Collectors.toList());
	}

	public Map<String, Long> getHolidaysNotOnWeekends(Integer year, List<String> countryCodes) {
		logger.info("Calculating holidays not on weekends for countries: {} in {}", countryCodes, year);
		Map<String, Long> result = new HashMap<>();
		for (String countryCode : countryCodes) {
			List<Holiday> holidays = holidayApiClient.getHolidays(year, countryCode);
			long count = holidays.stream().filter(holiday -> holiday.getDate().getDayOfWeek() != DayOfWeek.SATURDAY
					&& holiday.getDate().getDayOfWeek() != DayOfWeek.SUNDAY).count();
			result.put(countryCode, count);
		}
		return result.entrySet().stream().sorted(Map.Entry.<String, Long>comparingByValue().reversed())
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
	}

	public List<Holiday> getSharedHolidays(Integer year, String countryCode1, String countryCode2) {
		logger.info("Finding shared holidays for {} and {} in {}", countryCode1, countryCode2, year);
		List<Holiday> holidays1 = holidayApiClient.getHolidays(year, countryCode1);
		List<Holiday> holidays2 = holidayApiClient.getHolidays(year, countryCode2);

		Set<LocalDate> datesInCountry1 = holidays1.stream().map(Holiday::getDate).collect(Collectors.toSet());

		return holidays2.stream().filter(holiday -> datesInCountry1.contains(holiday.getDate())).distinct()
				.collect(Collectors.toList());
	}
}
