package com.home.essentials.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.home.essentials.model.User;
import com.home.essentials.model.WishlistRequest;
import com.home.essentials.payload.FailureResponse;
import com.home.essentials.payload.SuccessResponse;
import com.home.essentials.repository.UserRepository;
import com.home.essentials.service.impl.WishlistServiceimpl;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@RestController
@Api(value = "Wishlist Controller", description = "  REST API for products added to wishlist, deleting and Viewing", tags = {
		"Wishlist Controller" })
@Slf4j
public class WishlistController {

	@Autowired
	private WishlistServiceimpl wishlistServiceimpl;

	@Autowired
	private UserRepository userRepository;

	@PostMapping("/api/wishlist/addToWishlist")
	@ApiOperation(value = "Add Products To Wishlist", response = User.class)
	public ResponseEntity<?> addToWishlist(@RequestBody WishlistRequest wishlist) throws Exception {
		wishlistServiceimpl.addToWishList(wishlist);
		Optional<User> existingUser = userRepository.findById(wishlist.getUserId());
		if (existingUser.isPresent()) {
			User userForWishlist = existingUser.get();

			return new ResponseEntity<>(new SuccessResponse("Product Added to wishlist"), HttpStatus.OK);
		} else
			return new ResponseEntity<>(new FailureResponse("User doesnot exist in database"), HttpStatus.NOT_FOUND);
	}

	@GetMapping("/api/wishlist/viewWishlist")
	@ApiOperation(value = "View The Wishlist For User")
	public ResponseEntity<?> getWishlist(Model model, @RequestParam("userId") String userId) throws Exception {

		Optional<User> userInDb = userRepository.findById(userId);
		if (userInDb.isPresent()) {
			User user = userInDb.get();
			model.addAttribute("myWishlist", user.getWishlist());

			return new ResponseEntity<>(model, HttpStatus.OK);
		} else
			return new ResponseEntity<>(new FailureResponse("User doesnot exist in database"), HttpStatus.NOT_FOUND);
	}

	@DeleteMapping("/api/wishlist/removeWishlist")
	@ApiOperation(value = "Remove the Entire WishList For User ", response = User.class)
	public ResponseEntity<?> removeWishList(@RequestParam("userId") String userId) throws Exception {

		wishlistServiceimpl.removeTheWishList(userId);
		Optional<User> userInDb = userRepository.findById(userId);
		if (userInDb.isPresent()) {
			User existingUserInDb = userInDb.get();

			return new ResponseEntity<>(existingUserInDb, HttpStatus.OK);
		} else
			return new ResponseEntity<>(new FailureResponse("User doesnot exist in database"), HttpStatus.NOT_FOUND);
	}
}
