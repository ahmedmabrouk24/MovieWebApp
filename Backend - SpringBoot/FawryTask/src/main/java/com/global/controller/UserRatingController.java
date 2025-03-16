package com.global.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.global.entity.UserRating;
import com.global.security.JwtTokenUtils;
import com.global.service.UserRatingService;

@RestController
@RequestMapping("/user/api/v1")
@CrossOrigin(origins = "*")
public class UserRatingController {

    @Autowired
    private UserRatingService userRatingService;

    @Autowired
    private JwtTokenUtils jwtTokenUtils;

    @PostMapping("/rate")
    public ResponseEntity<?> rateMovie(@RequestParam("imdbID") String imdbID, @RequestParam("rating") double rating, 
    		@RequestHeader("Authorization") String authorizationHeader) {
    	System.out.println("Ratingggggg");
    	System.out.println("imdbID " + imdbID);
    	System.out.println("rating " + rating);
    	
        try {
            // Extract the token from the Authorization header
        	String token = authorizationHeader.substring(7); // Remove "Bearer " prefix

            // Extract userId from the token
            Long userId = jwtTokenUtils.getUserIdFromToken(token);

            // Call the RatingService to save the rating
            UserRating ratingResponse = userRatingService.rateMovie(userId, imdbID, rating);

            return ResponseEntity.ok(ratingResponse);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body("Error rating the movie: " + ex.getMessage());
        }
    }
}
