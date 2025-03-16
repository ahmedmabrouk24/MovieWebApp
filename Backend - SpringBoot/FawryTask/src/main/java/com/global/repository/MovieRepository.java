package com.global.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.global.entity.Movie;

import jakarta.transaction.Transactional;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long>{
	
	Page<Movie> findByTitleContainingIgnoreCase(String Title, Pageable pageable);
	
	Page<Movie> findAll(Pageable pageable);
	
    Optional<Movie> findByImdbID(String imdbID);
    
    @Transactional
    void deleteByimdbID(String imdbID);
    
    @Modifying
    @Transactional
    @Query("DELETE FROM Movie m WHERE m.imdbID IN :imdbIDs")
    void deleteAllByImdbIDs(@Param("imdbIDs") List<String> imdbIDs);
}
