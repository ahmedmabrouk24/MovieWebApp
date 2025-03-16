package com.global.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.global.dto.MovieDTO;
import com.global.dto.SearchResultDTO;
import com.global.service.UserMoviesService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/user/api/v1/movies")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class UserMoviesController {

	private final UserMoviesService userMovieService;
	
	// Authenticated users APIs
	
	@GetMapping("/get-by-id/{imdbID}")
	public ResponseEntity<?> getMovieById(@PathVariable("imdbID") String imdbID,
			@RequestHeader("Authorization") String authorizationHeader) {
        try {
            MovieDTO movieDTO = userMovieService.getMovieById(imdbID, authorizationHeader);
        	return ResponseEntity.ok(movieDTO);
        } catch (RuntimeException ex) {
            // Handle case where movie is not found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Movie with id : " + imdbID + " not found");
        }
    }
	
	@GetMapping("/{page_no}/get-all")
	public ResponseEntity<SearchResultDTO> getAllMovies(@PathVariable("page_no") int page_no,
			@RequestHeader("Authorization") String authorizationHeader) {
		SearchResultDTO searchResultDTO = userMovieService.getAllMovies(page_no, authorizationHeader);
		return ResponseEntity.ok(searchResultDTO);
	}

	@GetMapping("/get-by-title/{page_no}/{movieName}")
	public ResponseEntity<SearchResultDTO> getMoviesByTitle(@PathVariable("page_no") int page_no,
			@PathVariable("movieName") String movieName, @RequestHeader("Authorization") String authorizationHeader) {
		SearchResultDTO searchResultDTO = userMovieService.getMoviesByTitle(page_no, movieName, authorizationHeader);
		return ResponseEntity.ok(searchResultDTO);
	}
	
	// Unauthenticated users APIs
	
	@GetMapping("/unauth/get-by-id/{imdbID}")
	public ResponseEntity<?> getMovieById(@PathVariable("imdbID") String imdbID) {
        try {
            MovieDTO movieDTO = userMovieService.getMovieById(imdbID);
        	return ResponseEntity.ok(movieDTO);
        } catch (RuntimeException ex) {
            // Handle case where movie is not found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Movie with id : " + imdbID + " not found");
        }
    }
	
	@GetMapping("/unauth/get-by-title/{page_no}/{movieName}")
	public ResponseEntity<SearchResultDTO> getMoviesByTitle(@PathVariable("page_no") int page_no,
			@PathVariable("movieName") String movieName) {
		SearchResultDTO searchResultDTO = userMovieService.getMoviesByTitle(page_no, movieName);
		return ResponseEntity.ok(searchResultDTO);
	}

	@GetMapping("/unauth/{page_no}/get-all")
	public ResponseEntity<SearchResultDTO> getAllMovies(@PathVariable("page_no") int page_no) {
		SearchResultDTO searchResultDTO = userMovieService.getAllMovies(page_no);
		return ResponseEntity.ok(searchResultDTO);
	}
	
	@CrossOrigin(origins = "*")
	@GetMapping("/unauth/isUser")
	public Boolean isUser(@RequestHeader("Authorization") String authorizationHeader) {
		return userMovieService.isUser(authorizationHeader);
	}
}