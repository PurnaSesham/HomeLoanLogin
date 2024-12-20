package com.home.loan.api.login.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.home.loan.api.login.jwt.JwtUtil;
import com.home.loan.api.login.model.LoginRequest;
import com.home.loan.api.login.model.LoginResponse;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class HomeLoanLoginController {

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	JwtUtil jwtUtil;

	@PostMapping("/loginHomeLoan")
	public ResponseEntity<?> userEndPoint(@RequestBody LoginRequest LoginRequest) {
		Authentication authentication;
		try {
			authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(LoginRequest.getUsername(), LoginRequest.getPassword()));
		} catch (Exception e) {
			Map<String, Object> errorMap = new HashMap<>();
			errorMap.put("message", "Bad credentials");
			errorMap.put("status", false);
			return new ResponseEntity<Object>(errorMap, HttpStatus.NOT_FOUND);
		}
		SecurityContextHolder.getContext().setAuthentication(authentication);
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		String jwtToken = jwtUtil.generateTokenFromUsername(userDetails);
		List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
		LoginResponse loginResponse = new LoginResponse();
		loginResponse.setJwt(jwtToken);
		loginResponse.setUsername(userDetails.getUsername());
		loginResponse.setRoles(roles);
		return new ResponseEntity<>(loginResponse, HttpStatus.OK);

	}

}
