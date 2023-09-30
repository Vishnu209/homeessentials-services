package com.home.essentials.model;

import java.util.List;

import lombok.Data;

@Data
public class WishlistRequest {

private List<ProductsForWishList> productsForWishList;
	
	private String userId;
}
