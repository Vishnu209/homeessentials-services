package com.home.essentials.utils;

import java.util.List;

import org.springframework.stereotype.Service;

import com.home.essentials.model.ShoppingCartEntry;

@Service
public class PriceStrategyRaw implements PriceStrategy {

	@Override
	public double getTotal(List<ShoppingCartEntry> shoppingCartEntries) {
		double total = 0.0;
		if (shoppingCartEntries != null && !shoppingCartEntries.isEmpty()) {
			for (ShoppingCartEntry e : shoppingCartEntries) {
				total += e.getProductTotalPrice();
			}
		}	
		return total;
	}

}