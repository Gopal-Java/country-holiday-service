package com.sample.holiday.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.sample.holiday.exception.HolidayApiException;
import com.sample.holiday.model.Holiday;

/**
 * 
 * @author Gopal Haldar
 * Client Test class
 *
 */
public class HolidayApiClientTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private HolidayApiClient holidayApiClient;

    @Value("${holiday-api.base-url}")
    private String baseUrl = "http://example.com/api/holidays"; // Mock URL

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetHolidaysSuccess() {
        // Arrange
        int year = 2024;
        String countryCode = "US";
        Holiday[] mockHolidaysArray = {
            new Holiday(LocalDate.parse("2024-01-01"), "New Year's Day", "New Year's Day"),
            new Holiday(LocalDate.parse("2024-12-25"), "Christmas", "Christmas"),
            new Holiday(LocalDate.parse("2024-11-14"), "Thanksgiving", "Thanksgiving")
        };
        when(restTemplate.getForObject(any(String.class), eq(Holiday[].class))).thenReturn(mockHolidaysArray);

        // Act
        List<Holiday> holidays = holidayApiClient.getHolidays(year, countryCode);

        // Assert
        assertEquals(3, holidays.size());
        assertEquals("New Year's Day", holidays.get(0).getName());
        verify(restTemplate).getForObject(any(String.class), eq(Holiday[].class)); // Verify interaction
    }

    @Test
    public void testGetHolidaysApiException() {
        // Arrange
        int year = 2024;
        String countryCode = "US";
        when(restTemplate.getForObject(any(String.class), eq(Holiday[].class))).thenThrow(new RestClientException("API error"));

        // Act & Assert
        HolidayApiException exception = assertThrows(HolidayApiException.class, () -> {
            holidayApiClient.getHolidays(year, countryCode);
        });
        
        assertEquals("Failed to fetch holiday data from external API", exception.getMessage());
        verify(restTemplate).getForObject(any(String.class), eq(Holiday[].class)); // Verify interaction
    }
}
