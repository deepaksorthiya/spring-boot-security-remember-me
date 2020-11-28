package com.example.usermgmt;

import java.io.Serializable;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;

/**
 * @author Deepak Katariya
 * @date Nov 28, 2020
 */
public class UserProfile implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String username;
	private String firstName;
	private String lastName;
	private boolean enabled;
	private Set<GrantedAuthority> authorities;

	public UserProfile() {
		// JPA
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Set<GrantedAuthority> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(Set<GrantedAuthority> authorities) {
		this.authorities = authorities;
	}

	@Override
	public String toString() {
		return "UserProfile [username=" + username + ", firstName=" + firstName + ", lastName=" + lastName
				+ ", enabled=" + enabled + ", authorities=" + authorities + "]";
	}

}
