package com.home.essentials.utils;

import java.util.List;

import com.home.essentials.model.ShoppingCartEntry;

public interface PriceStrategy {

	public double getTotal(List<ShoppingCartEntry> shoppingCartEntries);
}
