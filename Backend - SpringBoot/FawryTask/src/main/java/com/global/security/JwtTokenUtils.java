package com.global.security;

import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.global.entity.User;
import com.global.repository.UserRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class JwtTokenUtils {

	@Autowired
	private UserRepository userRepository;
	// Static variables to hold the secret key and validity durations for access and refresh tokens
	private static String TOKEN_SECRET;
	private static Long ACCESS_TOKEN_VALIDITY;
	private static Long REFRESH_TOKEN_VALIDITY;

	// Constructor with @Value annotations to inject values from application.properties
	public JwtTokenUtils(@Value("${auth.secret}") String secret, @Value("${auth.access.expiration}") Long accessValidity,
			@Value("${auth.refresh.expiration}") Long refreshValidity) {

		// Assert to ensure that the accessValidity is not null and secret is not empty
		Assert.notNull(accessValidity, "Validity must not be null");
		Assert.hasText(secret, "Secret must not be null or empty");

		// Assign the injected values to static variables
		TOKEN_SECRET = secret;
		ACCESS_TOKEN_VALIDITY = accessValidity;
		REFRESH_TOKEN_VALIDITY = refreshValidity;
	}

	public static String generateToken(final String email, final String tokenId, boolean isRefresh) {
		return Jwts.builder()
				.setId(tokenId)
				.setIssuedAt(new Date())
				.setIssuer("app-Service")
				.setExpiration(calcTokenExpirationDate(isRefresh))
				.claim("created", Calendar.getInstance().getTime())
				.claim("email", email)
				.signWith(SignatureAlgorithm.HS512, TOKEN_SECRET).compact();
	}

	// Method to calculate the expiration date of the token based on whether it's a refresh token
	private static Date calcTokenExpirationDate(boolean isRefresh) {
		return new Date(
				System.currentTimeMillis() + (isRefresh ? REFRESH_TOKEN_VALIDITY : ACCESS_TOKEN_VALIDITY) * 1000);
	}
	
	public String getEmailFromToken(String token) {
		Claims claims = getClaims(token);
		return claims.get("email", String.class);
	}
	
	public Long getUserIdFromToken(String token) {
		String userEmail = getEmailFromToken(token);
        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new RuntimeException("User not found"));
        return user.getId();
	}

	public String getTokenIdFromToken(String token) {
		Claims claims = getClaims(token);
		return claims.getId();
	}

	// Method to check if the token is valid by comparing the username and checking if the token is expired
	public boolean isTokenValid(String token, AppUserDetails userDetails) {
		log.info("isTokenExpired: " + isTokenExpired(token));
		String email = getEmailFromToken(token);
		log.info("User email from token: " + email);
		log.info("userDetails email: " + userDetails.getEmail());
		log.info("emails match: " + email.equals(userDetails.getEmail()));
		Boolean isUserEmailEqual = email.equalsIgnoreCase(userDetails.getEmail());
		return (isUserEmailEqual && !isTokenExpired(token));
	}

	// Method to check if the token is expired
	public boolean isTokenExpired(String token) {
		Date expiration = getClaims(token).getExpiration();
		return expiration.before(new Date());
	}

	// Method to parse the JWT token and extract the claims
	private Claims getClaims(String token) {
		// Parse the JWT and return the claims using the secret key to verify the signature
		return Jwts.parser().setSigningKey(TOKEN_SECRET).parseClaimsJws(token).getBody();
	}

	// Method to validate the token, handling different exceptions during parsing
    public boolean validateToken(String token, HttpServletRequest httpServletRequest) {
        try {
            // Try parsing the token using the signing key (TOKEN_SECRET)
            Jwts.parser().setSigningKey(TOKEN_SECRET).parseClaimsJws(token);
            return true;  // Token is valid
        } catch (SignatureException ex) {
            log.error("Invalid JWT Signature: {}", ex.getMessage());
            httpServletRequest.setAttribute("error", "Invalid JWT signature");
            
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token: {}", ex.getMessage());
            httpServletRequest.setAttribute("error", "Invalid JWT token");
            
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token: {}", ex.getMessage());
            httpServletRequest.setAttribute("error", "Expired JWT token");
            
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token: {}", ex.getMessage());
            httpServletRequest.setAttribute("error", "Unsupported JWT token");
            
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty: {}", ex.getMessage());
            httpServletRequest.setAttribute("error", "JWT claims string is empty");
        }
        return false;  // Token is invalid
    }
}
