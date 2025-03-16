package com.global.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.global.entity.Role;
import com.global.repository.RoleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoleService {
	private final RoleRepository roleRepository;
	
	public Role findByName(String name){
		return roleRepository.findByName(name);
	}
	
	public List<Role> findAll(){
		return roleRepository.findAll();
	}
	
	public Role findById(Long id){
		return roleRepository.findById(id).orElse(null);
	}
	
	public Role insert(Role user){
		return roleRepository.save(user);
	}
}