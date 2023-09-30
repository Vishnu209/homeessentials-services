package com.home.essentials.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.home.essentials.model.Product;
import com.home.essentials.model.ShoppingCartEntry;
import com.home.essentials.model.ShoppingCartRequest;
import com.home.essentials.model.User;
import com.home.essentials.repository.UserRepository;
import com.home.essentials.service.CartService;
import com.home.essentials.utils.PriceStrategy;
import com.home.essentials.utils.PriceStrategyRaw;
import com.home.essentials.utils.PriceStrategyTax;
import com.home.essentials.utils.PriceUtilities;

@Service
public class CartServiceImpl implements CartService {

	@Autowired
	private ProductServiceImpl productServiceImpl;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PriceStrategy priceStrategy;

	@Override
	public List<ShoppingCartEntry> addShoppingCartEntries(ShoppingCartRequest shoppingCart) {
		List<ShoppingCartEntry> cartEntries = shoppingCart.getShoppingCart();
		List<ShoppingCartEntry> shoppingCarts = new ArrayList<ShoppingCartEntry>();
		String userId = shoppingCart.getUserId();
		Optional<User> userExisting = userRepository.findById(userId);
		User user = null;

		cartEntries.stream().forEach(x -> {
			x.setImageId(x.getImageId());
			x.setProductImageUrl(x.getProductImageUrl());
			x.setProductCost(x.getProductCost());
			x.setProductId(x.getProductId());
			x.setProductName(x.getProductName());
			x.setQuantity(x.getQuantity());
			x.setColour(x.getColour());
			x.setPackagingQuantities(x.getPackagingQuantities());
			x.setSize(x.getSize());
			Optional<Product> productInDb = productServiceImpl.getProduct(x.getProductId());
			if (productInDb.isPresent()) {
				Product product = productInDb.get();
				x.setProductCategory(product.getCategoryName());
				//checked for offer is NotNull and is NotEmpty and greater than or equal to 0.
				if (product.getOffer() != null && !product.getOffer().isEmpty()
						&& Integer.parseInt(product.getOffer()) >= 0) {
					int productCost = Integer.parseInt(x.getProductCost());
					int offer = Integer.parseInt(product.getOffer());
					double productTotalPrice = x.getQuantity() * (productCost - (productCost * offer / 100));
					x.setProductTotalPrice(productTotalPrice);
				}
				//checked for offer is Null or is Empty 
				 if(product.getOffer() == null || product.getOffer().isEmpty()) {
					int productCost = Integer.parseInt(x.getProductCost());
					double productTotalPrice = x.getQuantity() * productCost;
					x.setProductTotalPrice(productTotalPrice);
				}
			}
			shoppingCarts.add(x);
		});
		if (userExisting.isPresent()) {
			user = userExisting.get();
			user.setShoppingCartEntries(shoppingCarts);
			user = userRepository.save(user);
		}
		return shoppingCarts;
	}

	@Override
	public User removeTheCart(String userId) {
		Optional<User> userInDb = userRepository.findById(userId);
		if (userInDb.isPresent()) {
			User existingUserInDb = userInDb.get();
			List<ShoppingCartEntry> shoppingCart = existingUserInDb.getShoppingCartEntries();
			shoppingCart.removeAll(shoppingCart);
			existingUserInDb.setShoppingCartEntries(shoppingCart);
			existingUserInDb = userRepository.save(existingUserInDb);
			return existingUserInDb;
		} else
			return null;
	}

	@Override
	public Double getTotalPrice(List<ShoppingCartEntry> shoppingCartEntries) {
		priceStrategy = new PriceStrategyRaw();
		return priceStrategy.getTotal(shoppingCartEntries);
	}

	@Override
	public String getTotalTax(List<ShoppingCartEntry> shoppingCartEntries) {
		priceStrategy = new PriceStrategyTax();
		return PriceUtilities.roundToTwoDecimalPlaces(priceStrategy.getTotal(shoppingCartEntries));
	}

}
