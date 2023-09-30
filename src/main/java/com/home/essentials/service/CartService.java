package com.home.essentials.service;

import java.util.List;

import com.home.essentials.model.ShoppingCartEntry;
import com.home.essentials.model.ShoppingCartRequest;
import com.home.essentials.model.User;

public interface CartService {

	List<ShoppingCartEntry> addShoppingCartEntries(ShoppingCartRequest shoppingCart);
		
	Double getTotalPrice(List<ShoppingCartEntry> shoppingCartEntries);

	String getTotalTax(List<ShoppingCartEntry> shoppingCartEntries);

	User removeTheCart(String userId);
	
	}