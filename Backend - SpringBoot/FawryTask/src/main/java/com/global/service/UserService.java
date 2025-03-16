package com.global.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.global.dto.SignupRequestDto;
import com.global.entity.Role;
import com.global.entity.User;
import com.global.repository.UserRepository;
import com.global.security.AppUserDetails;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;


@RequiredArgsConstructor
@Log4j2
@Service
public class UserService implements UserDetailsService{
	private final UserRepository userRepository;
	private final RoleService roleService;
	private final PasswordEncoder passwordEncoder;
	
	public List<User> findAll(){
		return userRepository.findAll();
	}
	
	public User findById(Long id){
		return userRepository.findById(id).orElse(null);
	}
	
	public User findByEmail(String email){
		return userRepository.findByEmail(email).orElse(null);
	}
	
	public User insert(User user){
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return userRepository.save(user);
	}
	
	@Override
	public AppUserDetails loadUserByUsername(String email){
		
	    User user = userRepository.findByEmail(email)
	        .orElseThrow(() -> new UsernameNotFoundException("This user -> " + email + " is not exiested"));
	    return new AppUserDetails(user);
	}
	
	public String signup(SignupRequestDto signupRequestDto) {
	    Optional<User> existingUser = userRepository.findByEmail(signupRequestDto.getEmail());
	    if (existingUser.isPresent()) {
	        return "Email already exists!";  
	    }

	    String hashedPassword = passwordEncoder.encode(signupRequestDto.getPassword());

	    User AppUser = new User();
	    AppUser.setEmail(signupRequestDto.getEmail());
	    AppUser.setPassword(hashedPassword);
	    AppUser.setUsername(signupRequestDto.getUserName());

	    Role userRole = roleService.findByName("ROLE_USER");
	    if (userRole == null) {
	        userRole = new Role();
	        userRole.setName("ROLE_USER");
	        roleService.insert(userRole);
	    }

	    Set<Role> userRoles = new HashSet<>();
	    userRoles.add(userRole);
	    AppUser.setRoles(userRoles);

	    userRepository.save(AppUser);
	    return "User registered successfully!";
	}

}