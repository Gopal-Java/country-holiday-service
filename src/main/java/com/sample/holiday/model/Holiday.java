package com.sample.holiday.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;


public class Holiday {
	
	private LocalDate date;
	
	private String name;

	@JsonProperty("localName")
	private String localName;

	public Holiday() {
	}
	public Holiday(LocalDate date, String name, String localName) {
		this.date = date;
		this.name = name;
		this.localName = localName;
	}

	public LocalDate getDate() {
		return date;
	}

	public String getName() {
		return name;
	}

	public String getLocalName() {
		return localName;
	}
}

