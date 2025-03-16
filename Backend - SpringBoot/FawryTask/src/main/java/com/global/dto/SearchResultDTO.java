package com.global.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SearchResultDTO {
	@JsonProperty("Search")
	private List<MovieSummaryDTO> movies;

	@JsonProperty("totalResults")
	private String totalResults;

	@JsonProperty("Response")
	private String response;
}
