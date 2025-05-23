package com.global.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.global.entity.UserRating;

public interface UserRatingRepository extends JpaRepository<UserRating, Long> {
    UserRating findByUser_IdAndMovie_ImdbID(Long userId, String movieImdbID);
}
