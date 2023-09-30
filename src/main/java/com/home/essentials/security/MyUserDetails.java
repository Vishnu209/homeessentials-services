package com.home.essentials.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.home.essentials.model.User;

import lombok.Data;

@Data
public class MyUserDetails implements UserDetails {
	/**
	* 
	*/
	private static final long serialVersionUID = -2058831996789056337L;

	private String id;

	private static Collection<? extends GrantedAuthority> authorities;

	private User user;

	public MyUserDetails(String id, Collection<? extends GrantedAuthority> authorities, User user) {
		super();
		this.id = id;
		MyUserDetails.authorities = authorities;
		this.user = user;
	}

	public static MyUserDetails create(User user) {

		return new MyUserDetails(user.getId(), authorities, user);
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
