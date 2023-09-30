package com.home.essentials.service.impl;

import static com.home.essentials.error.ErrorUtils.UserNotFoundException;

import java.io.IOException;
import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.home.essentials.error.EntityNotFoundException;
import com.home.essentials.exception.BadRequestException;
import com.home.essentials.exception.ResourceNotFoundException;
import com.home.essentials.model.User;
import com.home.essentials.payload.JwtAuthenticationResponse;
import com.home.essentials.repository.UserRepository;
import com.home.essentials.security.JwtTokenProvider;
import com.home.essentials.security.MyUserDetails;
import com.home.essentials.service.EmailService;
import com.home.essentials.service.UserService;
import com.home.essentials.utils.RandomUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserServiceImpl implements UserService, UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	private final PasswordEncoder passwordEncoder;

	public UserServiceImpl(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	JwtTokenProvider tokenProvider;
	
	@Autowired
	AuthenticationManager authenticationManager;


	@Override
	public User saveUser(User user) throws Exception {
		if (user.getEmail() == null || user.getEmail().isEmpty()) {
			throw new Exception("email cannot be null or empty");
		}
		ObjectMapper mapper = new ObjectMapper();
		mapper.findAndRegisterModules().disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		user.setCreatedBy(user.getFirstName() + " " + user.getLastName());
		user.setCreatedDate(Instant.now());
		user.setAdmin(false);
		user.setDeletedFlag(false);
		user.setEnabled(true);
		user.setPassword(passwordEncoder.encode(user.getPassword()));

		EmailValidator validator = EmailValidator.getInstance();
		if (!validator.isValid(user.getEmail())) {
			log.info("Invalid email address");
			throw new Exception("Entered Email address is invalid!");
		}

		if (user.isSocialLogin()) {
			if (user.isSocialFbLogin()) {
				getFacebookProfileInfo(user.getAccessToken());
			} else {
				getGoogleTokenInfo(user.getAccessToken());
			}
		}
		try {
			emailService.sendUserCreationEmail(user, "mail/UserCreationEmail", "", null);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Email was not sent to the user!");
		}
		
		userRepository.save(user);
		return user;
	}
	

	@Override
	public User saveUserAsAdmin(User user) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		mapper.findAndRegisterModules().disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		if (user.getEmail() == null || user.getEmail().isEmpty()) {
			throw new Exception("email cannot be null or empty");
		}
		EmailValidator validator = EmailValidator.getInstance();
		if (!validator.isValid(user.getEmail())) {
			log.info("Invalid email address");
			throw new Exception("email cannot be null or empty");
		}
		user.setCreatedDate(Instant.now());
		user.setEnabled(true);
		user.setAdmin(true);
		user.setDeletedFlag(false);

		// setting a random password
		String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
		StringBuilder salt = new StringBuilder();
		Random rnd = new Random();
		while (salt.length() < 18) { // length of the random string.
			int index = (int) (rnd.nextFloat() * SALTCHARS.length());
			salt.append(SALTCHARS.charAt(index));
		}
		String saltStr = salt.toString();
		if (user.getPassword() == null) {
			user.setPassword(passwordEncoder.encode(saltStr));

			user.setAdminPassword(saltStr);
		} else {
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			user.setAdminPassword(user.getPassword());
		}
		try {
			emailService.sendUserCreationEmailToAdmin(user, "mail/AdminUserCreationEmail", "", null);
			user = userRepository.save(user);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Email was not sent to the user!");
		}
		return user;
	}


	@Override
	public JwtAuthenticationResponse saveUserSocailLogin(User user) throws Exception {
		String password = user.getPassword();

		if (user.isSocialLogin()) {
			if (!userRepository.existsByEmail(user.getEmail())) {

				if (user.isSocialFbLogin()) {

					getFacebookProfileInfo(user.getAccessToken());

				} else {
					getGoogleTokenInfo(user.getAccessToken());
				}

				EmailValidator validator = EmailValidator.getInstance();
				if (!validator.isValid(user.getEmail())) {
					log.info("Invalid email address");
					throw new Exception("email cannot be null or empty");
				}

				user.setPassword(passwordEncoder.encode(user.getPassword()));
				user.setEnabled(false);
				user.setDeletedFlag(false);
				ObjectMapper mapper = new ObjectMapper();
				mapper.findAndRegisterModules().disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
				user.setCreatedBy(user.getFirstName() + " " + user.getLastName());

				userRepository.save(user);


				// Sending email to the registered user
				try {
					emailService.sendUserCreationEmail(user, "mail/UserCreationEmail", "email.activation.title", null);

				} catch (Exception e) {
					e.printStackTrace();
					log.error("Email was not sent to the user!");
				}
			}

			log.info("Email Address  " + user.getEmail() + "  exists in the DB");
			Authentication authentication = authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), password));

			SecurityContextHolder.getContext().setAuthentication(authentication);
			log.info("Generating Token......");
			String jwt = tokenProvider.generateToken(authentication);
			log.info("****token generated :  " + jwt + "  !!!!!!!!");
			return new JwtAuthenticationResponse(jwt);
		}
		else{
			throw new Exception("Not a valid socail signup");
		}
	}

	@Override
	public User getUserById(String userId) throws Exception {
		return userRepository.findById(userId).orElseThrow(UserNotFoundException(userId));
	}

	@Override
	public User getUserByEmail(String email) {
		return userRepository.findByEmail(email).orElseThrow(() ->new EntityNotFoundException(User.class, "email", email));
	}

	@Override
	public User getUserByUsername(String username) {
		return userRepository.findByUsername(username).orElseThrow(() ->new EntityNotFoundException(User.class, "username", username));
	}

	@Override
	public List<User> getAllUsers() {
		Sort sort = Sort.by(Sort.Direction.DESC, "createdDate", "lastupdatedDate");
		return userRepository.getAllUsers(sort);
	}

	@Override
	public User updateUser(User user) {
		user.setLastupdatedDate(Instant.now());
		user.setUpdatedBy(user.getFirstName() + " " + user.getLastName());
		ObjectMapper mapper = new ObjectMapper();
		mapper.findAndRegisterModules().disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		return userRepository.save(user);
	}

	@Override
	public User deleteUser(String userId) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new EntityNotFoundException(User.class, "userId", userId));
		user.setDeletedFlag(true);
		userRepository.save(user);
		return user;
	}

	@Override
	public Optional<User> completePasswordReset(String newPassword, String key) {
		log.debug("Reset user password for reset key {}", key);
		return userRepository.findOneByResetKey(key)
				.filter(user -> user.getResetDate().isAfter(Instant.now().minusSeconds(86400))).map(user -> {
					user.setPassword(passwordEncoder.encode(newPassword));
					user.setResetKey(null);
					user.setResetDate(null);
					userRepository.save(user);
					return user;
				});
	}

	@Override
	public Optional<User> requestPasswordReset(String mail) {
		return userRepository.findByEmail(mail).map(user -> {
			user.setResetKey(RandomUtil.generateResetKey());
			user.setResetDate(Instant.now());
			userRepository.save(user);
			return user;
		});
	}

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
		// Let people login with either username or email
		User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail).orElseThrow(
				() -> new UsernameNotFoundException("User not found with username or email : " + usernameOrEmail));

		return MyUserDetails.create(user);
	}

	@Transactional
	public UserDetails loadUserById(String id) {
		User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User", "id", id));

		return MyUserDetails.create(user);
	}

	public String getGoogleTokenInfo(String accessToken) throws BadRequestException {
		log.debug("Calling Google API to get token info");
		RestTemplate restTemplate = new RestTemplate();
		String googleResponse = null;
		String idToken = null;
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

			UriComponentsBuilder uriBuilder = UriComponentsBuilder
					.fromUriString("https://www.googleapis.com/oauth2/v3/tokeninfo")
					.queryParam("id_token", accessToken);
			log.debug("google login uri {}", uriBuilder.toUriString());
			googleResponse = restTemplate.getForObject(uriBuilder.toUriString(), String.class);
			log.info("Gmail user authenticated successfully, details [{}]", googleResponse);

			idToken = getIdToken(googleResponse);

		} catch (HttpClientErrorException e) {
			log.error("Not able to authenticate from Google");
			try {
				JsonNode error = new ObjectMapper().readValue(e.getResponseBodyAsString(), JsonNode.class);
				log.error(error.toString());
				throw new BadRequestException("Invalid access token");
			} catch (IOException mappingExp) {
				throw new BadRequestException("Invalid user");
			}
		} catch (Exception exp) {
			log.error("User is not authorized to login into system", exp);
			throw new BadRequestException("Invalid user");
		}
		return idToken;
	}

	private String getIdToken(String googleResponse) {
		String idToken;
		Map<String, String> myMap = new HashMap<String, String>();
		googleResponse = googleResponse.replaceAll("\"", "");
		String[] pairs = googleResponse.split(",");
		for (int i = 0; i < pairs.length; i++) {
			String pair = pairs[i];
			String[] keyValue = pair.split(":");
			myMap.put(keyValue[0].trim(), keyValue[1].trim());
		}

		idToken = myMap.get("sub");
		log.info(myMap.get("sub") + "-->myMap");
		return idToken;
	}

	public String getFacebookProfileInfo(@PathVariable String accessToken) throws BadRequestException {
		log.debug("Calling Facebook API to validate and get profile info");
		RestTemplate restTemplate = new RestTemplate();
		String facebook = null;
		String idToken = null;
		// field names which will be retrieved from facebook
		final String fields = "id,email,first_name,last_name";
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

			UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString("https://graph.facebook.com/me")
					.queryParam("access_token", accessToken).queryParam("fields", fields);

			log.debug("Facebook profile uri {}", uriBuilder.toUriString());
			facebook = restTemplate.getForObject(uriBuilder.toUriString(), String.class);

			log.info("Facebook user authenticated and profile fetched successfully, details [{}]", facebook);

			idToken = getIdToken(facebook);
		} catch (HttpClientErrorException e) {
			log.error("Not able to authenticate from Facebook");
			throw new BadRequestException("Invalid access token");
		} catch (Exception exp) {
			log.error("User is not authorized to login into system", exp);
			throw new BadRequestException("Invalid user");
		}
		return facebook;
	}

}
