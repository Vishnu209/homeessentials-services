package com.home.essentials.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.home.essentials.model.User;
import com.home.essentials.payload.FailureResponse;
import com.home.essentials.payload.JwtAuthenticationResponse;
import com.home.essentials.payload.LoginRequest;
import com.home.essentials.repository.UserRepository;
import com.home.essentials.security.JwtTokenProvider;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RestController
@CrossOrigin
@Api(value = "JWT AUTHENTICATION CONTROLLER", description = "REST API for Login")
public class JWTAuthenticationController {

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserRepository userRepository;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	JwtTokenProvider tokenProvider;

	@PostMapping("/api/auth/signin")
	@ApiOperation(value = "Login with the required Credentials")
	public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
		log.info("Rest request to authenticate the user!!");
		
		Optional<User> userExist = userRepository.findByUsernameOrEmail(loginRequest.getUsernameOrEmail(), loginRequest.getUsernameOrEmail());
		if (!userExist.isPresent() || (userExist.isPresent() && userExist.get().isDeletedFlag())) {
			return new ResponseEntity(new FailureResponse("User not in use or deleted!"), HttpStatus.BAD_REQUEST);

		}
		if(!passwordEncoder.matches(loginRequest.getPassword(), userExist.get().getPassword())) {
			return new ResponseEntity(new FailureResponse("Incorrect password provided!!"), HttpStatus.BAD_REQUEST);
		}
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsernameOrEmail(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);

		String jwt = tokenProvider.generateToken(authentication);
		log.info("Token generated : " + jwt);
		return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
	}
}
