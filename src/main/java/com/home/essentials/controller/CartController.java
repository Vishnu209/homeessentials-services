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

import com.home.essentials.model.ShoppingCartRequest;
import com.home.essentials.model.User;
import com.home.essentials.payload.FailureResponse;
import com.home.essentials.repository.ProductRepository;
import com.home.essentials.repository.UserRepository;
import com.home.essentials.service.CartService;
import com.home.essentials.service.impl.CartServiceImpl;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@RestController
@Api(value = "Cart Controller", description = "REST API for Cart", tags = { "Cart Controller" })
@Slf4j
public class CartController {

	@Autowired
	private CartService cartService;

	@Autowired
	private CartServiceImpl cartServiceImpl;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private UserRepository userRepository;

	

	@PostMapping("/api/cart/addToCart")
	@ApiOperation(value = "Add Products To Cart", response = User.class)
	public ResponseEntity<?> addToCart(@RequestBody ShoppingCartRequest shoppingCart) throws Exception {
		cartServiceImpl.addShoppingCartEntries(shoppingCart);
		Optional<User> existingUser = userRepository.findById(shoppingCart.getUserId());
		if (existingUser.isPresent()) {
			User userForCart = existingUser.get();

			return new ResponseEntity<>(userForCart, HttpStatus.OK);
		} else
			return new ResponseEntity<>(new FailureResponse("User doesnot exist in database"), HttpStatus.NOT_FOUND);
	}

	@DeleteMapping("/api/cart/removeCart")
	@ApiOperation(value = "Remove the Entire Cart For User ", response = User.class)
	public ResponseEntity<?> removeCart(@RequestParam("userId") String userId) throws Exception {

		cartServiceImpl.removeTheCart(userId);
		Optional<User> userInDb = userRepository.findById(userId);
		if (userInDb.isPresent()) {
			User existingUserInDb = userInDb.get();

			return new ResponseEntity<>(existingUserInDb, HttpStatus.OK);
		} else
			return new ResponseEntity<>(new FailureResponse("User doesnot exist in database"), HttpStatus.NOT_FOUND);
	}

	@GetMapping("/api/cart/viewCart")
	@ApiOperation(value = "View The Cart For User")
	public ResponseEntity<?> getShoppingCartEntries(Model model, @RequestParam("userId") String userId)
			throws Exception {

		Optional<User> userInDb = userRepository.findById(userId);
		if (userInDb.isPresent()) {
			User user = userInDb.get();
			model.addAttribute("shoppingCartEntries", user.getShoppingCartEntries());
			model.addAttribute("totalPrice", cartService.getTotalPrice(user.getShoppingCartEntries()));
			model.addAttribute("taxPrice", cartService.getTotalTax(user.getShoppingCartEntries()));
		}

		return new ResponseEntity<>(model, HttpStatus.OK);

	}
}
