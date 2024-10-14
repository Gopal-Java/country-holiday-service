package com.sample.holiday.rest;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.sample.holiday.model.Holiday;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;

public interface HolidayApi {

	@RequestMapping(value = "/last-three/{countryCode}/{year}", produces = {
			"application/json" }, method = RequestMethod.GET)
	List<Holiday> lastThreeHolidaysForCountry(
			@Parameter(in = ParameterIn.PATH, description = "", required = true, schema = @Schema()) @PathVariable("countryCode") String countryCode,
			@Parameter(in = ParameterIn.PATH, description = "", required = true, schema = @Schema()) @PathVariable("year") Integer year,
			@Parameter(in = ParameterIn.HEADER, description = "", schema = @Schema()) @RequestHeader(value = "transactionId", required = false) String transactionId);


	@RequestMapping(value = "/no-weekends/{year}", produces = { "application/json" }, method = RequestMethod.GET)
	Map<String, Long> getHolidaysNotOnWeekends(
			@Parameter(in = ParameterIn.PATH, description = "", required = true, schema = @Schema()) @PathVariable("year") Integer year,
			@NotNull @Parameter(in = ParameterIn.QUERY, description = "", required = true, schema = @Schema()) @Valid @RequestParam(value = "countryCodes", required = true) List<String> countryCodes,
			@Parameter(in = ParameterIn.HEADER, description = "", schema = @Schema()) @RequestHeader(value = "transactionId", required = false) String transactionId);

	@RequestMapping(value = "/shared/{year}", produces = { "application/json" }, method = RequestMethod.GET)
	List<Holiday> getCommonHolidaysForTwoCountries(
			@Parameter(in = ParameterIn.PATH, description = "", required = true, schema = @Schema()) @PathVariable("year") Integer year,
			@NotNull @Parameter(in = ParameterIn.QUERY, description = "", required = true, schema = @Schema()) @Valid @RequestParam(value = "countryCode1", required = true) String countryCode1,
			@NotNull @Parameter(in = ParameterIn.QUERY, description = "", required = true, schema = @Schema()) @Valid @RequestParam(value = "countryCode2", required = true) String countryCode2,
			@Parameter(in = ParameterIn.HEADER, description = "", schema = @Schema()) @RequestHeader(value = "transactionId", required = false) String transactionId);

}
