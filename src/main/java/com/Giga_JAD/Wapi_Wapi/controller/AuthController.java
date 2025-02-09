package com.Giga_JAD.Wapi_Wapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.Giga_JAD.Wapi_Wapi.model.blueprint.User;
import com.Giga_JAD.Wapi_Wapi.model.dao.UserDAO;
import com.Giga_JAD.Wapi_Wapi.utils.passwordUtils;

@RestController
@RequestMapping("/auth")
public class AuthController {

	private final UserDAO userDAO;

	@Autowired
	public AuthController(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	@PostMapping("/login")
	public String login(@RequestParam String username, @RequestParam String password, HttpServletResponse response,
			HttpSession session) {

		// Hash the password before validating
		String hashedPassword = passwordUtils.hashPassword(password);

		// Validate user credentials
		User validatedUser = userDAO.validateUser(username, hashedPassword);

		if (validatedUser != null) {
			if (validatedUser.isBlocked()) { // Fix method name
				return "Your account is blocked. Please contact support.";
			} else {
				// Create a secure cookie for authentication
				Cookie isLoggedInCookie = new Cookie("isLoggedIn", "true");
				isLoggedInCookie.setPath("/");
				isLoggedInCookie.setHttpOnly(true);
				isLoggedInCookie.setSecure(true); // Set to true for HTTPS
				isLoggedInCookie.setMaxAge(60 * 60); // 1 hour

				response.addCookie(isLoggedInCookie);

				// Store user in session
				session.setAttribute("currentUser", validatedUser);

				return "Login successful! Redirecting...";
			}
		} else {
			return "Invalid username or password.";
		}
	}
}
