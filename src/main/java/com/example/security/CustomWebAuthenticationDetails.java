package com.example.security;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.web.authentication.WebAuthenticationDetails;

/**
 * @author Deepak Katariya
 * @date Nov 28, 2020
 */
public class CustomWebAuthenticationDetails extends WebAuthenticationDetails {

	private static final long serialVersionUID = 1L;

	private final String verificationCode;

	public CustomWebAuthenticationDetails(HttpServletRequest request) {
		super(request);
		verificationCode = request.getParameter("code");
	}

	public String getVerificationCode() {
		return verificationCode;
	}
}