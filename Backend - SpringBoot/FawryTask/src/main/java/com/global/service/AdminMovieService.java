package com.global.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.global.dto.MovieDTO;
import com.global.dto.SearchResultDTO;
import com.global.entity.Movie;
import com.global.mapper.MovieMapper;
import com.global.repository.MovieRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class AdminMovieService {

	@Autowired
	private final MovieRepository movieRepository;

	@Autowired
	private final RestTemplate restTemplate;

	public MovieDTO getMovieById(String imdbID) {
		return getMovieByIdFromOmdbApi(imdbID);
	}

	public SearchResultDTO getMoviesByTitle(int page_no, String movieName) {
		return fetchMoviesFromOmdbApiByTitle(page_no + 1, movieName);
	}

	public String addMovie(String imdbID) {
		Optional<Movie> existingMovie = movieRepository.findByImdbID(imdbID);

		if (existingMovie.isPresent()) {
			return "Movie with IMDb ID " + imdbID + " already exists!";
		}

		try {
			MovieDTO movieDTO = getMovieById(imdbID);
			movieRepository.save(MovieMapper.toMovieEntity(movieDTO));
			return "Movie added successfully!";
		} catch (Exception e) {
			return "Error adding movie: " + e.getMessage();
		}
	}

	public String deleteMovie(String imdbID) {
		Optional<Movie> existingMovie = movieRepository.findByImdbID(imdbID);

		if (!existingMovie.isPresent()) {
			return "Movie with IMDb ID " + imdbID + " doesn't exists!";
		}

		try {
			movieRepository.deleteByimdbID(imdbID);
			return "Movie deleted successfully!";
		} catch (Exception e) {
			return "Error deleteing movie: " + e.getMessage();
		}
	}
	
    public List<String> addMoviesBatch(List<String> imdbIDs) {
        for (String imdbID : imdbIDs) {
            MovieDTO movieDto = getMovieById(imdbID);
            Optional<Movie> existingMovie = movieRepository.findByImdbID(imdbID);
            if (!existingMovie.isPresent()) {
            	movieRepository.save(MovieMapper.toMovieEntity(movieDto));
            } else {
                log.info("Movie with imdbID: " + imdbID + " is already exists!");
            }
        }
        return imdbIDs;
    }

    public void removeMoviesBatch(List<String> imdbIDs) {
    	for (String imdbID : imdbIDs) {
            Optional<Movie> existingMovie = movieRepository.findByImdbID(imdbID);
            if (existingMovie.isPresent()) {
            	movieRepository.deleteByimdbID(imdbID);
            } else {
                log.info("Movie with imdbID: " + imdbID + " is NOT exists!");
            }
        }
    }

	private MovieDTO getMovieByIdFromOmdbApi(String imdbID) {
		String url = "https://www.omdbapi.com/?i=" + imdbID + "&apikey=9b0bcb0f";
		ResponseEntity<Movie> response = restTemplate.getForEntity(url, Movie.class);

		if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
			Movie movieResponse = response.getBody();
			return MovieMapper.toMovieDTO(movieResponse);
		} else {
			throw new RuntimeException("Movie not found in OMDb API");
		}
	}

	private SearchResultDTO fetchMoviesFromOmdbApiByTitle(int page_no, String movieTitle) {
		String url = "https://www.omdbapi.com/?s=" + movieTitle + "&page=" + page_no + "&apikey=9b0bcb0f";
		ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

		if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
			ObjectMapper objectMapper = new ObjectMapper();
			try {
				SearchResultDTO searchResultDTO = objectMapper.readValue(response.getBody(), SearchResultDTO.class);
				if (searchResultDTO.getMovies() != null && !searchResultDTO.getMovies().isEmpty()) {
					return searchResultDTO;
				} else {
					throw new RuntimeException("No movies found for title: " + movieTitle);
				}
			} catch (JsonProcessingException ex) {
				throw new RuntimeException("Failed to parse OMDb API response", ex);
			}
		} else {
			throw new RuntimeException("Failed to fetch movies from OMDb API");
		}
	}

}
