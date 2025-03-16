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
		User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

		Movie movie = movieRepository.findByImdbID(imdbID).orElseThrow(() -> new RuntimeException("Movie not found"));

		UserRating existingRating = userRatingRepository.findByUser_IdAndMovie_ImdbID(userId, imdbID);
		if (existingRating != null) {
			existingRating.setRating(ratingValue);
			return userRatingRepository.save(existingRating);
		} else {
			UserRating newRating = new UserRating();
			newRating.setUser(user);
			newRating.setMovie(movie);
			newRating.setRating(ratingValue);
			return userRatingRepository.save(newRating);
		}
	}
}
