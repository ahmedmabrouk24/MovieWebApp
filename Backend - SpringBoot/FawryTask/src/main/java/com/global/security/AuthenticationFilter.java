package com.global.security;

import java.io.IOException;
import java.util.Enumeration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.global.service.UserService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class AuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenUtils tokenUtil;

    /**
     * Filters incoming HTTP requests, validating the JWT token, and setting the
     * authentication context if the token is valid.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
    	
    	 // Set CORS headers to avoid blocking cross-origin requests
        response.setHeader("Access-Control-Allow-Origin", "*"); // Allow all origins or specify one
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS"); // Allowed methods
        response.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type"); // Allowed headers
        response.setHeader("Access-Control-Allow-Credentials", "true"); // Allow credentials (cookies, auth headers)
        response.setHeader("Access-Control-Max-Age", "3600"); // Cache preflight responses for 1 hour

        // Skip authentication for CORS preflight requests (OPTIONS method)
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK); // Respond with 200 OK for OPTIONS requests
            return;
        }
        
    	// Skip this filter for AccountController (Login - Logout - SignUp)
    	String requestURI = request.getRequestURI();
    	if (requestURI.startsWith("/api/v1/auth/") || requestURI.startsWith("/user/api/v1/movies/unauth/")) {
            filterChain.doFilter(request, response);
            return;
        }
        
        // Extract the Authorization header (which contains the JWT token)
        String jwtTokenHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        // Log Request Headers
        Enumeration<String> headerNames = request.getHeaderNames();
        if (headerNames != null) {
            while (headerNames.hasMoreElements()) {
                String headerName = headerNames.nextElement();
                String headerValue = request.getHeader(headerName);
                log.info("Header: {} = {}", headerName, headerValue);
            }
        }


        // If the Authorization header is missing or doesn't contain a valid token, respond with 401
        if (jwtTokenHeader == null || !jwtTokenHeader.startsWith("Bearer ")) {
            // Respond with a 401 Unauthorized if the token is missing or invalid
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Unauthorized\", \"message\": \"JWT Token is missing or invalid\"}");
            return;
        }

        // Extract the JWT token from the header (after "Bearer " prefix)
        String jwtToken = jwtTokenHeader.substring("Bearer ".length());

        // Log the request URL for monitoring or debugging purposes
        log.info("Processing authentication for request: {}", request.getRequestURL());

        final SecurityContext securityContext = SecurityContextHolder.getContext();

        // If the user is already authenticated, skip further processing
        if (securityContext.getAuthentication() != null) {
            log.debug("User already authenticated, skipping authentication check.");
            filterChain.doFilter(request, response);
            return;
        }

        // Validate the JWT token and set authentication context if token is valid
        if (tokenUtil.validateToken(jwtToken, request)) {
            String email = tokenUtil.getEmailFromToken(jwtToken);
            if (email != null) {
                log.debug("Valid token for user: {}", email);

                // Load user details from the database using the user email
                AppUserDetails userDetails = (AppUserDetails) userService.loadUserByUsername(email);
                
                if (userDetails != null && tokenUtil.isTokenValid(jwtToken, userDetails)) {
                    // If the token is valid and the user exists, set the authentication
                    setAuthenticationContext(userDetails, request);
                } 
            } 
        } 
        else {
        	response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Unauthorized\", \"message\": \"JWT Token is invalid\"}");
            return;
        }
        // Continue processing the request in the filter chain
        filterChain.doFilter(request, response);
    }

    // Sets the authentication context in Spring Security using the user details.
    private void setAuthenticationContext(AppUserDetails userDetails, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        log.info("Authentication successful for user: {}", userDetails.getUsername());
    }

    // Helper method to check if the URL corresponds to Swagger-related endpoints or actuator.
    protected boolean isSwaggerUrl(String url) {
        String[] swaggerPaths = { "swagger", "api-docs", "configuration/ui", "webjars/", "swagger-resources", 
                                   "configuration/security", "actuator" };
        for (String path : swaggerPaths) {
            if (url.contains(path)) {
                return true;  
            }
        }
        return false;  
    }
}
