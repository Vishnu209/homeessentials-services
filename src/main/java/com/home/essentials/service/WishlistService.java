package com.home.essentials.service;

import java.util.List;

import com.home.essentials.model.User;
import com.home.essentials.model.Wishlist;
import com.home.essentials.model.WishlistRequest;

public interface WishlistService {
	
	List<Wishlist> addToWishList(WishlistRequest wishlistRequest);
	
	User removeTheWishList(String userId);

}
