package com.global.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.global.entity.TokenInfo;
import com.global.repository.TokenInfoRepo;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class TokenInfoService{
	
	private final TokenInfoRepo tokenInfoRepo;
	
	public List<TokenInfo> findAll(){
		return tokenInfoRepo.findAll();
	}
	
	public TokenInfo findById(Long id){
		return tokenInfoRepo.findById(id).orElseThrow();
	}
	
	public void deleteById(Long id){
		tokenInfoRepo.deleteById(id);
	}
	
	public Optional<TokenInfo> findByRefreshToken(String refreshToken) {
		return Optional.of(tokenInfoRepo.findByRefreshToken(refreshToken));
	}
	
	public TokenInfo save(TokenInfo tokenInfo){
		return tokenInfoRepo.save(tokenInfo);
	}	
}