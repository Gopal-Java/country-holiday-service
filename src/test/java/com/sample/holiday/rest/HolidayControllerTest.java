package com.sample.holiday.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.sample.holiday.model.Holiday;
import com.sample.holiday.service.HolidayService;

@MockitoSettings(strictness = Strictness.LENIENT)
public class HolidayControllerTest {

	@Mock
	private HolidayService holidayService;

	@InjectMocks
	private HolidayController holidayController;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void testLastThreeHolidaysForCountry() {
		// Arrange
		String countryCode = "US";
		Integer year = 2024;
		String transactionId = "txn-123";

		// Mock data
		List<Holiday> mockHolidays = Arrays.asList(
				new Holiday(LocalDate.parse("2024-01-01"), "New Year's Day", "New Year's Day"),
				new Holiday(LocalDate.parse("2024-12-25"), "Christmas", "Christmas"),
				new Holiday(LocalDate.parse("2024-11-14"), "Thanksgiving", "Thanksgiving")
				);

		// Setting up mock behavior
		when(holidayService.getLastThreeHolidays(countryCode, year)).thenReturn(mockHolidays);

		// Act - call the controller method directly
		List<Holiday> holidays = holidayController.lastThreeHolidaysForCountry(countryCode, year, transactionId);

		// Assert
		verify(holidayService).getLastThreeHolidays(countryCode, year); // Ensure the service method was called
		assertEquals(3, holidays.size()); // Check the returned size
		assertEquals("New Year's Day", holidays.get(0).getName());
	}

	@Test
	public void testGetHolidaysNotOnWeekends() {
		// Mock data
		Map<String, Long> mockHolidayCounts = new HashMap<>();
		mockHolidayCounts.put("US", 5L);
		mockHolidayCounts.put("CA", 6L);
		List<String> countryCodes = Arrays.asList("US", "CA");

		when(holidayService.getHolidaysNotOnWeekends(2024, countryCodes)).thenReturn(mockHolidayCounts);

		// Call the method
		Map<String, Long> holidayCounts = holidayController.getHolidaysNotOnWeekends(2024, countryCodes, "txn-456");

		// Verify and assert
		verify(holidayService).getHolidaysNotOnWeekends(2024, countryCodes);
		assertEquals(5L, holidayCounts.get("US"));
		assertEquals(6L, holidayCounts.get("CA"));
	}

	@Test
	public void testGetCommonHolidaysForTwoCountries() {

		String countryCode1 = "US";
		String countryCode2 = "CA";

		// Mock data
		List<Holiday> mockCommonHolidays = Arrays.asList(
				new Holiday(LocalDate.parse("2024-07-04"), "Independence Day", "Independence Day"),
				new Holiday(LocalDate.parse("2024-12-25"), "Christmas", "Christmas")
				);

		when(holidayService.getSharedHolidays(2024, countryCode1, countryCode2)).thenReturn(mockCommonHolidays);

		// Call the method
		List<Holiday> commonHolidays = holidayController.getCommonHolidaysForTwoCountries(2024, "US", "CA", "txn-789");

		// Verify and assert
		verify(holidayService).getSharedHolidays(2024, "US", "CA");
		assertEquals(2, commonHolidays.size());
		assertEquals("Independence Day", commonHolidays.get(0).getName());
	}
}
