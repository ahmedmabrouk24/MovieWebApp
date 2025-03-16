package com.global.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.global.entity.User;

import lombok.Getter;
import lombok.Setter;

@Component
@Getter
@Setter
public class AppUserDetails implements UserDetails{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long id;
	private String userName;
	private String password;
	private String email;
	private List<GrantedAuthority> grantedAuthorities;
	
	public AppUserDetails() {
		super();
	}
	
	public AppUserDetails(User user) {
		super();
		this.id = user.getId();
		this.userName = user.getUsername();
		this.password = user.getPassword();
		this.email = user.getEmail();
		List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
	    if (user.getRoles() != null && user.getRoles().isEmpty() == false) {
	        user.getRoles().forEach(role -> {
	            grantedAuthorities.add(new SimpleGrantedAuthority(role.getName()));
	        });
	    }
	    this.grantedAuthorities = grantedAuthorities;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return grantedAuthorities;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return password;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return userName;
	}
	
	public boolean isAccountNonExpired() {
		return true;
	}
	
	public boolean isAccountNonLocked() {
		return true;
	}
	
	public boolean isCredentialsNonExpired() {
		return true;
	}
	
	public boolean isEnabled() {
		return true;
	}
}