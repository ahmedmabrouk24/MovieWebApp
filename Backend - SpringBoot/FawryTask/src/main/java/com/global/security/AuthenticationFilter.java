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

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
    	
        response.setHeader("Access-Control-Allow-Origin", "*"); 
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS"); 
        response.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type"); 
        response.setHeader("Access-Control-Allow-Credentials", "true"); 
        response.setHeader("Access-Control-Max-Age", "3600"); 

        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK); 
            return;
        }

    	String requestURI = request.getRequestURI();
    	if (requestURI.startsWith("/api/v1/auth/") || requestURI.startsWith("/user/api/v1/movies/unauth/")) {
            filterChain.doFilter(request, response);
            return;
        }
        
        String jwtTokenHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        Enumeration<String> headerNames = request.getHeaderNames();
        if (headerNames != null) {
            while (headerNames.hasMoreElements()) {
                String headerName = headerNames.nextElement();
                String headerValue = request.getHeader(headerName);
                log.info("Header: {} = {}", headerName, headerValue);
            }
        }


        if (jwtTokenHeader == null || !jwtTokenHeader.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Unauthorized\", \"message\": \"JWT Token is missing or invalid\"}");
            return;
        }

        String jwtToken = jwtTokenHeader.substring("Bearer ".length());

        log.info("Processing authentication for request: {}", request.getRequestURL());

        final SecurityContext securityContext = SecurityContextHolder.getContext();

        if (securityContext.getAuthentication() != null) {
            log.debug("User already authenticated, skipping authentication check.");
            filterChain.doFilter(request, response);
            return;
        }

        if (tokenUtil.validateToken(jwtToken, request)) {
            String email = tokenUtil.getEmailFromToken(jwtToken);
            if (email != null) {
                log.debug("Valid token for user: {}", email);

                AppUserDetails userDetails = (AppUserDetails) userService.loadUserByUsername(email);
                
                if (userDetails != null && tokenUtil.isTokenValid(jwtToken, userDetails)) {
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
        filterChain.doFilter(request, response);
    }

    private void setAuthenticationContext(AppUserDetails userDetails, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        log.info("Authentication successful for user: {}", userDetails.getUsername());
    }

}
