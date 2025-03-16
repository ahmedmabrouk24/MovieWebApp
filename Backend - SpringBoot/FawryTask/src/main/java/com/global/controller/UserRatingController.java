package com.global.controller;

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

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping("/user/api/v1")
@CrossOrigin(origins = "*")
@Log4j2
@RequiredArgsConstructor
public class UserRatingController {

    private final UserRatingService userRatingService;

    private final JwtTokenUtils jwtTokenUtils;

    @PostMapping("/rate")
    public ResponseEntity<?> rateMovie(@RequestParam("imdbID") String imdbID, @RequestParam("rating") double rating, 
    		@RequestHeader("Authorization") String authorizationHeader) {
        try {
        	String token = authorizationHeader.substring(7); 
            Long userId = jwtTokenUtils.getUserIdFromToken(token);
            UserRating ratingResponse = userRatingService.rateMovie(userId, imdbID, rating);
            log.info("Rating Done Successfully!");
            return ResponseEntity.ok(ratingResponse);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body("Error rating the movie: " + ex.getMessage());
        }
    }
}
