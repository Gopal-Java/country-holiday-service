package com.sample.holiday.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.sample.holiday.model.Holiday;

/**
 * 
 * @author Gopal Haldar
 * Service test class
 *
 */
public class HolidayServiceTest {

    @Mock
    private HolidayApiClient holidayApiClient;

    @InjectMocks
    private HolidayService holidayService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetLastThreeHolidays() {
        // Arrange
        String countryCode = "US";
        Integer year = 2024;

        List<Holiday> mockHolidays = Arrays.asList(
            new Holiday(LocalDate.parse("2023-12-25"), "Christmas", "Christmas"),
            new Holiday(LocalDate.parse("2023-11-11"), "Veterans Day", "Veterans Day"),
            new Holiday(LocalDate.parse("2023-07-04"), "Independence Day", "Independence Day"),
            new Holiday(LocalDate.parse("2023-01-01"), "New Year's Day", "New Year's Day")
        );

        // Mock behavior
        when(holidayApiClient.getHolidays(anyInt(), any(String.class))).thenReturn(mockHolidays);

        // Act
        List<Holiday> lastThreeHolidays = holidayService.getLastThreeHolidays(countryCode, year);

        // Assert
        assertEquals(3, lastThreeHolidays.size());
        assertEquals("Christmas", lastThreeHolidays.get(0).getName());
        assertEquals("Veterans Day", lastThreeHolidays.get(1).getName());
        assertEquals("Independence Day", lastThreeHolidays.get(2).getName());
    }

    @Test
    public void testGetHolidaysNotOnWeekends() {
        // Arrange
        Integer year = 2024;
        List<String> countryCodes = Arrays.asList("US", "CA");

        List<Holiday> holidaysUS = Arrays.asList(
            new Holiday(LocalDate.parse("2024-01-01"), "New Year's Day", "New Year's Day"),
            new Holiday(LocalDate.parse("2024-07-04"), "Independence Day", "Independence Day"),
            new Holiday(LocalDate.parse("2024-12-25"), "Christmas", "Christmas"),
            new Holiday(LocalDate.parse("2024-01-14"), "Martin Luther King Jr. Day", "Martin Luther King Jr. Day") // Monday
        );

        List<Holiday> holidaysCA = Arrays.asList(
            new Holiday(LocalDate.parse("2024-07-01"), "Canada Day", "Canada Day"),
            new Holiday(LocalDate.parse("2024-12-25"), "Christmas", "Christmas"),
            new Holiday(LocalDate.parse("2024-01-01"), "New Year's Day", "New Year's Day"),
            new Holiday(LocalDate.parse("2024-02-19"), "Family Day", "Family Day") // Monday
        );

        when(holidayApiClient.getHolidays(year, "US")).thenReturn(holidaysUS);
        when(holidayApiClient.getHolidays(year, "CA")).thenReturn(holidaysCA);

        // Act
        Map<String, Long> holidayCounts = holidayService.getHolidaysNotOnWeekends(year, countryCodes);

        // Assert
        assertEquals(3L, holidayCounts.get("US"));
        assertEquals(4L, holidayCounts.get("CA"));
    }

    @Test
    public void testGetSharedHolidays() {
        // Arrange
        Integer year = 2024;
        String countryCode1 = "US";
        String countryCode2 = "CA";

        List<Holiday> holidaysUS = Arrays.asList(
            new Holiday(LocalDate.parse("2024-01-01"), "New Year's Day", "New Year's Day"),
            new Holiday(LocalDate.parse("2024-07-04"), "Independence Day", "Independence Day"),
            new Holiday(LocalDate.parse("2024-12-25"), "Christmas", "Christmas")
        );

        List<Holiday> holidaysCA = Arrays.asList(
            new Holiday(LocalDate.parse("2024-01-01"), "New Year's Day", "New Year's Day"),
            new Holiday(LocalDate.parse("2024-07-01"), "Canada Day", "Canada Day"),
            new Holiday(LocalDate.parse("2024-12-25"), "Christmas", "Christmas")
        );

        when(holidayApiClient.getHolidays(year, countryCode1)).thenReturn(holidaysUS);
        when(holidayApiClient.getHolidays(year, countryCode2)).thenReturn(holidaysCA);

        // Act
        List<Holiday> sharedHolidays = holidayService.getSharedHolidays(year, countryCode1, countryCode2);

        // Assert
        assertEquals(2, sharedHolidays.size());
        assertIterableEquals(
            Arrays.asList("New Year's Day", "Christmas"),
            sharedHolidays.stream().map(Holiday::getName).collect(Collectors.toList())
        );
    }
}
