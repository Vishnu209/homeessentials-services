package com.home.essentials.controller;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.home.essentials.exception.EmailNotFoundException;
import com.home.essentials.exception.InternalServerErrorException;
import com.home.essentials.exception.InvalidPasswordException;
import com.home.essentials.model.KeyAndPasswordVM;
import com.home.essentials.model.User;
import com.home.essentials.payload.FailureResponse;
import com.home.essentials.payload.LoginRequest;
import com.home.essentials.repository.UserRepository;
import com.home.essentials.security.JwtTokenProvider;
import com.home.essentials.service.EmailService;
import com.home.essentials.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/api/auth")
@Api(value = "User  Controller", description = "  REST API for User Registeration, Update, Delete, Get, Password Forgot & Reset", tags = {
		"User  Controller" })
public class UserController {

	public static final int PASSWORD_MIN_LENGTH = 4;

	public static final int PASSWORD_MAX_LENGTH = 100;

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserRepository userRepository;

	@Autowired
	UserService userService;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	JwtTokenProvider tokenProvider;

	@Autowired
	private EmailService emailService;


	@GetMapping("/me")
	public ResponseEntity<User> retrieveCurrentUser(Principal principal) {
		String userNameOrEmail = SecurityContextHolder.getContext().getAuthentication().getName();
		log.info("userId:::" + userNameOrEmail);
		User user = userRepository.findByUsernameOrEmail(userNameOrEmail, userNameOrEmail).get();
		return ResponseEntity.status(HttpStatus.CREATED).body(user);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PostMapping("/signup")
	@ApiOperation(value = "User Registeration")
	public ResponseEntity<User> registerUser(@Valid @RequestBody User user) throws Exception {

		if (userRepository.existsByUsername(user.getUsername())) {
			log.info("Username  " + user.getUsername() + " already exists in the DB");
			return new ResponseEntity(new FailureResponse("Username is already taken!"), HttpStatus.BAD_REQUEST);
		}

		if (userRepository.existsByEmail(user.getEmail())) {
			log.info("Email Address  " + user.getEmail() + " already exists in the DB");
			return new ResponseEntity(new FailureResponse("Email Address already in use!"), HttpStatus.BAD_REQUEST);
		}
		user = userService.saveUser(user);
		return ResponseEntity.ok(user);

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PostMapping("/admin/signup")
	@ApiOperation(value = "Admin Registeration - sends a random string as password to the email.")
	public ResponseEntity<User> registerUserAsAdmin(@Valid @RequestBody User user) throws Exception {

		if (userRepository.existsByUsername(user.getUsername())) {
			log.info("Username  " + user.getUsername() + " already exists in the DB");
			return new ResponseEntity(new FailureResponse("Username is already taken!"), HttpStatus.BAD_REQUEST);
		}
		if (userRepository.existsByEmail(user.getEmail())) {
			log.info("Email Address  " + user.getEmail() + " already exists in the DB");
			return new ResponseEntity(new FailureResponse("Email Address already in use!"), HttpStatus.BAD_REQUEST);
		}
		
		user = userService.saveUserAsAdmin(user);

		return ResponseEntity.ok(user);

	}

	@GetMapping("/userId")
	@ApiOperation(value = "Get User By Id ")
	public ResponseEntity<User> getUser(@RequestParam("userId") String userId) throws Exception {

		return ResponseEntity.ok(userService.getUserById(userId));
	}

	@GetMapping("/user/userName")
	@ApiOperation(value = "Get User By UserName")
	public ResponseEntity<User> getUserByUserName(@RequestParam("username") String userName) throws Exception {
		return ResponseEntity.ok(userService.getUserByUsername(userName));
	}

	@GetMapping("/user/email")
	@ApiOperation(value = "Get User By Email")
	public ResponseEntity<User> getUserByEmail(@RequestParam("email") String email) throws Exception {
		return ResponseEntity.ok(userService.getUserByEmail(email));
	}

	@GetMapping("/users/findAll")
	@ApiOperation(value = "Get All Users")
	public ResponseEntity<List<User>> getAllUser() throws Exception {
		return ResponseEntity.ok(userService.getAllUsers());
	}

	@DeleteMapping("/delete/userId")
	@ApiOperation(value = "Delete User By Id")
	public ResponseEntity<User> deleteUser(@RequestParam("userId") String userId) throws Exception {
		return ResponseEntity.ok(userService.deleteUser(userId));
	}

	@PutMapping("/user/update")
	@ApiOperation(value = "Update User by id")
	public ResponseEntity<User> updateUser(@Valid @RequestBody User user, @RequestParam("userId") String id)
			throws GeneralSecurityException, IOException {
		return ResponseEntity.ok(userService.updateUser(user));

	}

	@PostMapping(path = "/account/forgot-password/init")
	public void requestPasswordReset(@RequestBody String mail) {
		emailService
				.sendPasswordResetMail(userService.requestPasswordReset(mail).orElseThrow(EmailNotFoundException::new));
	}

	@PostMapping(path = "/account/forgot-password/finish")
	public ResponseEntity<?> finishPasswordReset(@RequestBody KeyAndPasswordVM keyAndPassword) {
		if (!checkPasswordLength(keyAndPassword.getNewPassword())) {
			throw new InvalidPasswordException();
		}
		Optional<User> user = userService.completePasswordReset(keyAndPassword.getNewPassword(),
				keyAndPassword.getKey());

		if (!user.isPresent()) {
			throw new InternalServerErrorException("No user was found for this reset key");
		}
		return new ResponseEntity<>(user, HttpStatus.OK);
	}

	private boolean checkPasswordLength(String password) {
		return !StringUtils.isEmpty(password) && password.length() >= PASSWORD_MIN_LENGTH
				&& password.length() <= PASSWORD_MAX_LENGTH;
	}

	@PostMapping(path = "/account/reset-password/finish")
	public ResponseEntity<?> PasswordReset(@Valid @RequestBody LoginRequest loginRequest) {

		try {
			Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
					loginRequest.getUsernameOrEmail(), loginRequest.getPassword()));

			SecurityContextHolder.getContext().setAuthentication(authentication);

			if (authentication.isAuthenticated()) {
				User user = userRepository
						.findByUsernameOrEmail(loginRequest.getUsernameOrEmail(), loginRequest.getUsernameOrEmail())
						.get();
				user.setPassword(passwordEncoder.encode(loginRequest.getNewPassword()));
				userRepository.save(user);
				return new ResponseEntity<>(user, HttpStatus.OK);
			} else {
				return new ResponseEntity<>(new FailureResponse("you have provided wrong password!!"),
						HttpStatus.BAD_REQUEST);
			}
		} catch (AuthenticationException e) {
			return new ResponseEntity<>(new FailureResponse("you have provided wrong password!!"),
					HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<>(new FailureResponse("Internal server error!!"), HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/socialsignup/authenticate")
	@ApiOperation(value = "User Registeration- Sends a token if already present else signup and authenticate")
	public ResponseEntity<?> registerSocialLoginUser(@Valid @RequestBody User user)
			throws Exception {
		userService.saveUserSocailLogin(user);
		return ResponseEntity.ok(user);
	}

}
