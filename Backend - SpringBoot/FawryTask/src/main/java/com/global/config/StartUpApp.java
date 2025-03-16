package com.global.config;

import java.util.HashSet;
import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.global.entity.Role;
import com.global.entity.User;
import com.global.service.RoleService;
import com.global.service.UserService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class StartUpApp implements CommandLineRunner {

	private final UserService userService;
	private final RoleService roleService;

	@Override
	public void run(String... args) throws Exception {
		
		// Create admin account internally 
		if (userService.findByEmail("ahmed@gmail.com") == null) {
			Role admin = new Role();
			admin.setName("ROLE_ADMIN");
			roleService.insert(admin);
			
			Set<Role> adminRole = new HashSet<>();
			adminRole.add(roleService.findByName("ROLE_ADMIN"));
			User adminUser = new User();
			adminUser.setUsername("Ahmed Mabrouk");
			adminUser.setEmail("admin@gmail.com");
			adminUser.setPassword("123456");
			adminUser.setRoles(adminRole);
			userService.insert(adminUser);
		}
	}

}