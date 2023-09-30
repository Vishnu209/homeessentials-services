package com.home.essentials.utils;

import java.util.List;

import com.home.essentials.model.ShoppingCartEntry;

public class PriceStrategyTax implements PriceStrategy {

	@Override
	public double getTotal(List<ShoppingCartEntry> shoppingCartEntries) {
		double total = 0.0;
		if (shoppingCartEntries != null && !shoppingCartEntries.isEmpty()) {
			for (ShoppingCartEntry e : shoppingCartEntries) {
				total += e.getProductTotalPrice() * 0.18;
			}
		}
		return total;
	}

}