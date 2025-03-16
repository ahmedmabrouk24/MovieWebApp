package com.global.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import com.global.dto.MovieDTO;
import com.global.dto.MovieSummaryDTO;
import com.global.dto.SearchResultDTO;
import com.global.entity.Movie;
import com.global.entity.UserRating;
import com.global.repository.UserRatingRepository;
import com.global.security.JwtTokenUtils;

public class MovieMapper {
	
	@Autowired
	private static JwtTokenUtils jwtTokenUtils;
	
	@Autowired
	private static UserRatingRepository userRatingRepository;
	
	 // Method to map a list of Movie entities to SearchResultDTO
	public static SearchResultDTO toSearchResultDTO(List<Movie> movies, String authorizationHeader) {
	    // Extract the token from the authorization header
	    String token = authorizationHeader.substring(7); // Remove "Bearer " prefix
	    Long userId = jwtTokenUtils.getUserIdFromToken(token); // Get the user ID from the JWT token

	    // Map the list of Movie entities to MovieSummaryDTO
	    List<MovieSummaryDTO> movieSummaryDTOS = movies.stream()
            .map(movie -> {
            // Fetch the user's rating for this movie
            UserRating userRating = userRatingRepository.findByUser_IdAndMovie_ImdbID(userId, movie.getImdbID());
            
            // If the user has rated this movie, use that rating, otherwise set it to 0
            double userRatingValue = userRating != null ? userRating.getRating() : 0;
            
            // Create MovieSummaryDTO and set the user rating
            MovieSummaryDTO movieSummaryDTO = new MovieSummaryDTO(
                    movie.getTitle(),
                    movie.getYear(),
                    movie.getImdbID(),
                    movie.getType(),
                    movie.getPoster(),
                    userRatingValue);
                    return movieSummaryDTO; }).collect(Collectors.toList());

	    // Return SearchResultDTO with the list of MovieSummaryDTOs
	    return new SearchResultDTO(movieSummaryDTOS, String.valueOf(movies.size()), "True");
	}
    public static MovieDTO toMovieDTO(Movie movie) {
        MovieDTO movieDTO = new MovieDTO();
        
        movieDTO.setId(movie.getId());
        movieDTO.setTitle(movie.getTitle());
        movieDTO.setYear(movie.getYear());
        movieDTO.setRated(movie.getRated());
        movieDTO.setReleased(movie.getReleased());
        movieDTO.setRuntime(movie.getRuntime());
        movieDTO.setGenre(movie.getGenre());
        movieDTO.setDirector(movie.getDirector());
        movieDTO.setWriter(movie.getWriter());
        movieDTO.setActors(movie.getActors());
        movieDTO.setPlot(movie.getPlot());
        movieDTO.setLanguage(movie.getLanguage());
        movieDTO.setCountry(movie.getCountry());
        movieDTO.setAwards(movie.getAwards());
        movieDTO.setPoster(movie.getPoster());
        movieDTO.setMetascore(movie.getMetascore());
        movieDTO.setImdbRating(movie.getImdbRating());
        movieDTO.setImdbVotes(movie.getImdbVotes());
        movieDTO.setImdbID(movie.getImdbID());
        movieDTO.setType(movie.getType());
        movieDTO.setDVD(movie.getDvd());
        movieDTO.setBoxOffice(movie.getBoxOffice());
        movieDTO.setProduction(movie.getProduction());
        movieDTO.setWebsite(movie.getWebsite());
        movieDTO.setResponse(movie.getResponse());
        movieDTO.setRatings(movie.getRatings());
        
        return movieDTO;
    }
    
 // Method to map a MovieDTO to a Movie entity
    public static Movie toMovieEntity(MovieDTO movieDTO) {
        Movie movie = new Movie();
        
        movie.setId(movieDTO.getId());
        movie.setTitle(movieDTO.getTitle());
        movie.setYear(movieDTO.getYear());
        movie.setRated(movieDTO.getRated());
        movie.setReleased(movieDTO.getReleased());
        movie.setRuntime(movieDTO.getRuntime());
        movie.setGenre(movieDTO.getGenre());
        movie.setDirector(movieDTO.getDirector());
        movie.setWriter(movieDTO.getWriter());
        movie.setActors(movieDTO.getActors());
        movie.setPlot(movieDTO.getPlot());
        movie.setLanguage(movieDTO.getLanguage());
        movie.setCountry(movieDTO.getCountry());
        movie.setAwards(movieDTO.getAwards());
        movie.setPoster(movieDTO.getPoster());
        movie.setMetascore(movieDTO.getMetascore());
        movie.setImdbRating(movieDTO.getImdbRating());
        movie.setImdbVotes(movieDTO.getImdbVotes());
        movie.setImdbID(movieDTO.getImdbID());
        movie.setType(movieDTO.getType());
        movie.setDvd(movieDTO.getDVD());
        movie.setBoxOffice(movieDTO.getBoxOffice());
        movie.setProduction(movieDTO.getProduction());
        movie.setWebsite(movieDTO.getWebsite());
        movie.setResponse(movieDTO.getResponse());
        movie.setRatings(movieDTO.getRatings());
        
        return movie;
    }

}
