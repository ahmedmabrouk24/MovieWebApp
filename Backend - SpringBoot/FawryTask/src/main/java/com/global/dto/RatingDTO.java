package com.global.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RatingDTO {
	@JsonProperty("Source")
	private String source;

	@JsonProperty("Value")
	private String value;
}
