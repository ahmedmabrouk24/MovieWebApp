package com.global.service;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.global.dto.AccessTokenDto;
import com.global.dto.JWTResponseDto;
import com.global.dto.RefreshTokenDto;
import com.global.entity.TokenInfo;
import com.global.entity.User;
import com.global.security.AppUserDetails;
import com.global.security.JwtTokenUtils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final HttpServletRequest httpRequest;
    private final TokenInfoService tokenInfoService;
    private final UserService userService;
    private final JwtTokenUtils jwtTokenUtils;

    // Handles user login, authenticates the user, and generates JWT tokens.
    public JWTResponseDto login(String email, String password) {
        Authentication authentication = authenticateUser(email, password);
        AppUserDetails userDetails = (AppUserDetails) authentication.getPrincipal();
        
        TokenInfo tokenInfo = createLoginToken(email, userDetails.getId());
        
        return buildJWTResponse(tokenInfo);
    }

    // Authenticates the user based on username and password.
    private Authentication authenticateUser(String email, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password));
        
        log.info("Valid user credentials");
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return authentication;
    }

    // Builds the JWT response DTO from TokenInfo object.
    private JWTResponseDto buildJWTResponse(TokenInfo tokenInfo) {
        return JWTResponseDto.builder()
                .accessToken(tokenInfo.getAccessToken())
                .refreshToken(tokenInfo.getRefreshToken())
                .build();
    }

    // Creates and saves the JWT tokens (access token and refresh token) for the user.
    public TokenInfo createLoginToken(String email, long userId) {
        try {
            log.info("Creating login token...");

            String userAgent = httpRequest.getHeader(HttpHeaders.USER_AGENT);
            InetAddress ip = InetAddress.getLocalHost();
            User user = userService.findById(userId);

            String accessToken = generateToken(email, false);
            String refreshToken = generateToken(email, true);

            TokenInfo tokenInfo = new TokenInfo(
                    accessToken, refreshToken, userAgent, ip.getHostAddress(),
                    httpRequest.getRemoteAddr(), user);

            return tokenInfoService.save(tokenInfo);
        } catch (UnknownHostException e) {
            log.error("Error generating token: ", e);
            throw new RuntimeException("Error generating token", e); // Re-throw or handle accordingly
        }
    }

    // Generates a JWT token with a unique identifier.
    private String generateToken(String email, boolean isRefreshToken) {
        String tokenId = UUID.randomUUID().toString();
        return JwtTokenUtils.generateToken(email, tokenId, isRefreshToken);
    }

    // Refreshes the access token using the provided refresh token.
    public AccessTokenDto refreshAccessToken(RefreshTokenDto refreshTokenDto) {
    	
    	String refreshToken = refreshTokenDto.getRefreshToken();
        // Check if the refresh token is expired
        if (jwtTokenUtils.isTokenExpired(refreshToken)) {
            return null;
        }

        //String userName = jwtTokenUtils.getUserNameFromToken(refreshToken);
        String email = jwtTokenUtils.getEmailFromToken(refreshToken);
        
        Optional<TokenInfo> refresh = tokenInfoService.findByRefreshToken(refreshToken);

        // If refresh token is valid, generate a new access token
        return refresh.map(tokenInfo -> new AccessTokenDto(generateToken(email, false))).orElse(null);
    }

    // Logs out the user by invalidating the refresh token.
    public void logoutUser(String refreshToken) {
    	// Extract the refresh token from the Authorization header
        String token = refreshToken.startsWith("Bearer ") ? refreshToken.substring(7) : refreshToken;
        Optional<TokenInfo> tokenInfo = tokenInfoService.findByRefreshToken(token);
        // If the refresh token exists, delete it from the database
        tokenInfo.ifPresent(info -> tokenInfoService.deleteById(info.getId()));
    }
}
