package com.global.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class UserRating {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "user_id")
	@JsonBackReference("user-rating-movie") // Prevents infinite recursion
	private User user;

	@ManyToOne
	@JoinColumn(name = "movie_imdbID")
	@JsonBackReference("movie-user-rating") // Prevents infinite recursion
	private Movie movie;

	private double rating;

}
