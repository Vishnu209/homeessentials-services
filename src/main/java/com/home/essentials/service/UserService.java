package com.home.essentials.service;

import java.util.List;
import java.util.Optional;

import com.home.essentials.model.User;
import com.home.essentials.payload.JwtAuthenticationResponse;

public interface UserService {

	User saveUser(User user) throws Exception;

	User getUserById(String userId) throws Exception;

	User getUserByEmail(String email);
	
	User getUserByUsername(String username);

	List<User> getAllUsers();

	User updateUser(User user);

	User deleteUser(String userId);

	User saveUserAsAdmin(User user) throws Exception;
	
	JwtAuthenticationResponse saveUserSocailLogin(User user) throws Exception;

	Optional<User> completePasswordReset(String newPassword, String key);

	Optional<User> requestPasswordReset(String mail);
}