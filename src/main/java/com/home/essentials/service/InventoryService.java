package com.home.essentials.service;

import java.util.Optional;

import com.home.essentials.model.Inventory;

public interface InventoryService  {
	
	//INVENTORY registerInventory(INVENTORY inventory) throws Exception;

    Optional<Inventory> getInventory(String InventoryId) throws Exception;

    Inventory updateInventory(Inventory inventory) throws Exception;

    String deleteInventory(String InventoryId) throws Exception;
}