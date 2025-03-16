package com.global.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;

import com.global.dto.MovieDTO;
import com.global.dto.MovieSummaryDTO;
import com.global.dto.SearchResultDTO;
import com.global.entity.Movie;
import com.global.entity.UserRating;
import com.global.mapper.MovieMapper;
import com.global.repository.MovieRepository;
import com.global.repository.UserRatingRepository;
import com.global.security.JwtTokenUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserMoviesService {
	@Autowired
    private final MovieRepository movieRepository;

    @Autowired
    private final UserRatingRepository userRatingRepository;
    
    @Autowired
    private final JwtTokenUtils jwtTokenUtils; 

    public Boolean isUser(@RequestHeader("Authorization") String authorizationHeader) {
    	authorizationHeader = authorizationHeader.substring(7);
    	String email = jwtTokenUtils.getEmailFromToken(authorizationHeader);
		return (!email.equals("admin@gmail.com"));
	}
    
    public MovieDTO getMovieById(String imdbID, String authorizationHeader) {
        Movie movie = movieRepository.findByImdbID(imdbID)
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        String token = authorizationHeader.substring(7); 
        Long userId = jwtTokenUtils.getUserIdFromToken(token);
        
        UserRating userRating = userRatingRepository.findByUser_IdAndMovie_ImdbID(userId, imdbID);

        MovieDTO movieDTO = MovieMapper.toMovieDTO(movie);
        if (userRating != null) {
            movieDTO.setUserRating(userRating.getRating());
        }

        return movieDTO;
    }
    
    public MovieDTO getMovieById(String imdbID) {
        Movie movie = movieRepository.findByImdbID(imdbID)
                .orElseThrow(() -> new RuntimeException("Movie not found"));
        
        UserRating userRating = new UserRating();
        userRating.setRating(0);

        MovieDTO movieDTO = MovieMapper.toMovieDTO(movie);
        if (userRating != null) {
            movieDTO.setUserRating(userRating.getRating());
        }

        return movieDTO;
    }

    public SearchResultDTO getMoviesByTitle(int page_no, String movieName, String authorizationHeader) {
        
        PageRequest pageable = PageRequest.of(page_no - 1, 12); 

        Page<Movie> moviePage = movieRepository.findByTitleContainingIgnoreCase(movieName, pageable);
        return searchResultDTO(moviePage, authorizationHeader);
    }
    
    public SearchResultDTO getMoviesByTitle(int page_no, String movieName) {
        
        PageRequest pageable = PageRequest.of(page_no - 1, 12); 

        Page<Movie> moviePage = movieRepository.findByTitleContainingIgnoreCase(movieName, pageable);
        List<MovieSummaryDTO> movieSummaryDTOs = moviePage.getContent().stream()
            .map(movie -> {
                MovieSummaryDTO movieSummaryDTO = new MovieSummaryDTO();
                movieSummaryDTO.setTitle(movie.getTitle());
                movieSummaryDTO.setYear(movie.getYear());
                movieSummaryDTO.setImdbID(movie.getImdbID());
                movieSummaryDTO.setType(movie.getType());
                movieSummaryDTO.setPoster(movie.getPoster());
                return movieSummaryDTO;
            })
            .collect(Collectors.toList());

        return new SearchResultDTO(
            movieSummaryDTOs, 
            String.valueOf(moviePage.getTotalElements()), 
            "True" 
        );
    }

    public SearchResultDTO getAllMovies(int page_no, String authorizationHeader) {
        PageRequest pageable = PageRequest.of(page_no - 1, 12); 
        
        Page<Movie> moviePage = movieRepository.findAll(pageable);
        return searchResultDTO(moviePage, authorizationHeader);
    }
    
    public SearchResultDTO getAllMovies(int page_no) {
        
        PageRequest pageable = PageRequest.of(page_no - 1, 12); 

        Page<Movie> moviePage = movieRepository.findAll( pageable);
        List<MovieSummaryDTO> movieSummaryDTOs = moviePage.getContent().stream()
            .map(movie -> {
                MovieSummaryDTO movieSummaryDTO = new MovieSummaryDTO();
                movieSummaryDTO.setTitle(movie.getTitle());
                movieSummaryDTO.setYear(movie.getYear());
                movieSummaryDTO.setImdbID(movie.getImdbID());
                movieSummaryDTO.setType(movie.getType());
                movieSummaryDTO.setPoster(movie.getPoster());
                return movieSummaryDTO;
            })
            .collect(Collectors.toList());

        return new SearchResultDTO(
            movieSummaryDTOs, 
            String.valueOf(moviePage.getTotalElements()), 
            "True" 
        );
    }
    
    private SearchResultDTO searchResultDTO(Page<Movie> moviePage, String authorizationHeader) {
    	
        String token = authorizationHeader.substring(7); 
        Long userId = jwtTokenUtils.getUserIdFromToken(token);
        
        List<MovieSummaryDTO> movieSummaryDTOs = moviePage.getContent().stream()
            .map(movie -> {
                MovieSummaryDTO movieSummaryDTO = new MovieSummaryDTO();
                movieSummaryDTO.setTitle(movie.getTitle());
                movieSummaryDTO.setYear(movie.getYear());
                movieSummaryDTO.setImdbID(movie.getImdbID());
                movieSummaryDTO.setType(movie.getType());
                movieSummaryDTO.setPoster(movie.getPoster());
                
                UserRating userRating = userRatingRepository.findByUser_IdAndMovie_ImdbID(userId, movie.getImdbID());
                if (userRating != null) {
                    movieSummaryDTO.setUserRating(userRating.getRating());
                } else {
                    movieSummaryDTO.setUserRating(0); 
                }
                return movieSummaryDTO;
            })
            .collect(Collectors.toList());

        return new SearchResultDTO(
            movieSummaryDTOs, 
            String.valueOf(moviePage.getTotalElements()), 
            "True" 
        );
    }
    
}
