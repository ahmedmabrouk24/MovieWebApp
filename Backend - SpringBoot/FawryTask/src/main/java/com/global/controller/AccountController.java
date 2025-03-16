package com.global.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.global.dto.AccessTokenDto;
import com.global.dto.JWTResponseDto;
import com.global.dto.LoginRequestDto;
import com.global.dto.RefreshTokenDto;
import com.global.dto.SignupRequestDto;
import com.global.service.AuthenticationService;
import com.global.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Log4j2
@Validated
public class AccountController {

	private final AuthenticationService authenticationService;
	private final UserService userService;

	@PostMapping("/login")
	public ResponseEntity<JWTResponseDto> login(@RequestBody LoginRequestDto loginRequest) {
		JWTResponseDto jwtResponseDto = authenticationService.login(loginRequest.getEmail(),
				loginRequest.getPassword());
		return ResponseEntity.ok(jwtResponseDto);
	}

	@PostMapping("/logout")
	public ResponseEntity<String> logout(@RequestHeader("Authorization") String refreshToken) {
		authenticationService.logoutUser(refreshToken);
		return ResponseEntity.ok("User logged out successfully");
	}

	@PostMapping("/signup")
	public ResponseEntity<String> signup(@Validated @RequestBody SignupRequestDto signupRequestDto) {
		String responseMessage = userService.signup(signupRequestDto);

		if (responseMessage.equals("User registered successfully!")) {
			return ResponseEntity.status(HttpStatus.CREATED).body(responseMessage);
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseMessage);
	}

	@PostMapping("/refresh-token")
	public AccessTokenDto refreshAccessToken(@RequestBody RefreshTokenDto refreshTokenDto) {
		return authenticationService.refreshAccessToken(refreshTokenDto);
	}
}