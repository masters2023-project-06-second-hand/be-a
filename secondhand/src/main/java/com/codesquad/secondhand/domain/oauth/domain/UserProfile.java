package com.codesquad.secondhand.domain.oauth.domain;

public class UserProfile {
	private final String email;

	public UserProfile(String email) {
		this.email = email;
	}

	public String getEmail() {
		return email;
	}
}
