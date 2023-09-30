package com.home.essentials.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.home.essentials.model.Product;
import com.home.essentials.model.ProductsForWishList;
import com.home.essentials.model.User;
import com.home.essentials.model.Wishlist;
import com.home.essentials.model.WishlistRequest;
import com.home.essentials.repository.ProductRepository;
import com.home.essentials.repository.UserRepository;
import com.home.essentials.service.WishlistService;

@Service
public class WishlistServiceimpl implements WishlistService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private ProductServiceImpl productServiceimpl;

	@Override
	public List<Wishlist> addToWishList(WishlistRequest wishlistRequest) {
		List<Wishlist> newWishlist = new ArrayList<Wishlist>();
		String userId = wishlistRequest.getUserId();
		Optional<User> userExisting = userRepository.findById(userId);
		User user = null;
		for (ProductsForWishList wish : wishlistRequest.getProductsForWishList()) {
			Optional<Product> productInDb = productServiceimpl.getProduct(wish.getProductId());
			if (productInDb.isPresent()) {
				Product productItem = productInDb.get();
				Wishlist userWishlist = new Wishlist();
				userWishlist.setImageId(productItem.getImageId());
				userWishlist.setProductImageUrl(productItem.getProductImageUrl());
				userWishlist.setProductCost(productItem.getProductCost());
				userWishlist.setProductId(productItem.getId());
				userWishlist.setProductName(productItem.getProductName());
				newWishlist.add(userWishlist);
			}
		}
		if (userExisting.isPresent()) {
			user = userExisting.get();
			user.setWishlist(newWishlist);
			user = userRepository.save(user);
		}
		return newWishlist;
	}

	@Override
	public User removeTheWishList(String userId) {
		Optional<User> userInDb = userRepository.findById(userId);
		if (userInDb.isPresent()) {
			User existingUserInDb = userInDb.get();
			List<Wishlist> wishlist = existingUserInDb.getWishlist();
			wishlist.removeAll(wishlist);
			existingUserInDb.setWishlist(wishlist);
			existingUserInDb = userRepository.save(existingUserInDb);
			return existingUserInDb;
		} else
			return null;
	}

}
