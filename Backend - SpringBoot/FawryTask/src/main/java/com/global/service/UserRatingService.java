package com.global.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.global.entity.Movie;
import com.global.entity.User;
import com.global.entity.UserRating;
import com.global.repository.MovieRepository;
import com.global.repository.UserRatingRepository;
import com.global.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserRatingService {

	@Autowired
	private final UserRatingRepository userRatingRepository;

	@Autowired
	private final UserRepository userRepository;

	@Autowired
	private final MovieRepository movieRepository;

	@Transactional
	public UserRating rateMovie(Long userId, String imdbID, double ratingValue) {
		// Fetch the user by ID
		User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

		// If the movie does not exist, throw an exception
		Movie movie = movieRepository.findByImdbID(imdbID).orElseThrow(() -> new RuntimeException("Movie not found"));

		// Check if the user has already rated the movie
		UserRating existingRating = userRatingRepository.findByUser_IdAndMovie_ImdbID(userId, imdbID);
		if (existingRating != null) {
			// Update the existing rating
			existingRating.setRating(ratingValue);
			return userRatingRepository.save(existingRating);
		} else {
			// If no existing rating, create a new one
			UserRating newRating = new UserRating();
			newRating.setUser(user);
			newRating.setMovie(movie);
			newRating.setRating(ratingValue);
			return userRatingRepository.save(newRating);
		}
	}
}
