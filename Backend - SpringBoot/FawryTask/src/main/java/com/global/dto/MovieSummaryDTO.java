package com.global.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MovieSummaryDTO {
	@JsonProperty("Title")
	private String title;

	@JsonProperty("Year")
	private String year;

	@JsonProperty("imdbID")
	private String imdbID;

	@JsonProperty("Type")
	private String type;

	@JsonProperty("Poster")
	private String poster;
	
	private double userRating;  // The rating given by the current user for this movie
}
