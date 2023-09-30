package com.home.essentials.model;

import java.util.List;

import lombok.Data;

@Data
public class ShoppingCartRequest {

	private List<ShoppingCartEntry> shoppingCart;
	
	private String userId;
	
}
