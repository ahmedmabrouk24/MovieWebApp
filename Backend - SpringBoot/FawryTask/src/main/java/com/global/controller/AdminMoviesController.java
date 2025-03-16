package com.global.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.global.dto.MovieDTO;
import com.global.dto.SearchResultDTO;
import com.global.service.AdminMovieService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin/api/v1/movies")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AdminMoviesController {

	private final AdminMovieService adminMovieService;

	@GetMapping("/get-by-id/{imdbID}")
	public ResponseEntity<?> getMovieById(@PathVariable("imdbID") String imdbID) {
		try {
			MovieDTO movieDTO = adminMovieService.getMovieById(imdbID);
			return ResponseEntity.ok(movieDTO);
		} catch (RuntimeException ex) {
			return new ResponseEntity<>("Movie with imdbID: " + imdbID + " not found", HttpStatus.NOT_FOUND);
		}
	}

	@CrossOrigin(origins = "*")
	@GetMapping("/get-by-title/{page_no}/{movieTitle}")
	public ResponseEntity<?> getMoviesByTitle(@PathVariable("page_no") int page_no,
			@PathVariable("movieTitle") String movieTitle) {
		try {
			SearchResultDTO searchResultDTO = adminMovieService.getMoviesByTitle(page_no, movieTitle);
			return ResponseEntity.ok(searchResultDTO);
		} catch (RuntimeException ex) {
			return new ResponseEntity<>("Movies with title: " + movieTitle + " not found", HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping("/add-movie/{imdbID}")
	public ResponseEntity<String> addMovie(@PathVariable("imdbID") String imdbID) {

		String responseMessage = adminMovieService.addMovie(imdbID);
		if (responseMessage.contains("added successfully")) {
			return new ResponseEntity<>(responseMessage, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(responseMessage, HttpStatus.BAD_REQUEST);
		}
	}

	@DeleteMapping("/delete-movie/{imdbID}")
	public ResponseEntity<String> deleteMovie(@PathVariable("imdbID") String imdbID) {
		String responseMessage = adminMovieService.deleteMovie(imdbID);
		if (responseMessage.contains("deleted successfully")) {
			return new ResponseEntity<>(responseMessage, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(responseMessage, HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/addBatch")
	public ResponseEntity<String> addMoviesBatch(@RequestBody List<String> imdbIDs) {
		adminMovieService.addMoviesBatch(imdbIDs);
		return ResponseEntity.ok("Movies added successfully");
	}

	@DeleteMapping("/removeBatch")
	public ResponseEntity<String> removeMoviesBatch(@RequestBody List<String> imdbIDs) {
		adminMovieService.removeMoviesBatch(imdbIDs);
		return ResponseEntity.ok("Movies removed successfully");
	}

}
