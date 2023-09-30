package com.home.essentials.service.impl;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.home.essentials.exception.ItemNotFoundException;
import com.home.essentials.model.Inventory;
import com.home.essentials.repository.InventoryRepository;
import com.home.essentials.repository.ProductRepository;
import com.home.essentials.service.InventoryService;

@Service
public class InventoryServiceImpl implements InventoryService {

	@Autowired
	private InventoryRepository inventoryRepository;

	@Autowired
	private ProductRepository productRepository;

	/*@Override
	public Inventory registerInventory(Inventory inventory) throws Exception {
		String productId = inventory.getProductId();
		Optional<Product> productInDb = productRepository.findById(productId);
		if (productInDb.isPresent()) {
			if (!inventoryRepository.existsById(inventory.getId())) {
				inventory.setDeletedFlag(false);
				inventory.setProductName(productInDb.get().getProductName());
				inventory = inventoryRepository.save(inventory);
				return inventory;
			} else
				throw new DuplicateItemException(inventory.getProductId());
		} else
			throw new ItemNotFoundException(productId);
	}*/

	@Override
	public Optional<Inventory> getInventory(String id) throws Exception {
		Optional<Inventory> inventory = inventoryRepository.findById(id);
		if (inventory.isPresent()) {
			Inventory inventoryInDb = inventory.get();
			if (!inventoryInDb.isDeletedFlag())
				return inventory;
			else
				throw new ItemNotFoundException(id);
		} else
			throw new ItemNotFoundException(id);

	}

	public Optional<Inventory> getInventoryByProductId(String productId) throws Exception {
		Optional<Inventory> productInInventory = inventoryRepository.findByProductId(productId);
		if (productInInventory.isPresent()) {
			Inventory inventoryProduct = productInInventory.get();
			if (!inventoryProduct.isDeletedFlag())
				return productInInventory;
			else
				throw new ItemNotFoundException(productId);
		} else
			throw new ItemNotFoundException(productId);
	}

	public List<Inventory> getAllInventory() throws Exception {
		List<Inventory> inventories = inventoryRepository.findAll();
		return inventories;
	}

	@Override
	public Inventory updateInventory(Inventory inventory) throws Exception {
		Optional<Inventory> inventoryInDB = inventoryRepository.findById(inventory.getId());
		if (inventoryInDB.isPresent()) {
			Inventory editInventory = inventoryInDB.get();
			ObjectMapper mapper = new ObjectMapper();
			mapper.findAndRegisterModules().disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
			inventory.setDeletedFlag(false);
			inventory.setCreatedDate(editInventory.getCreatedDate());
			inventory.setLastupdatedDate(Instant.now());
			inventory = inventoryRepository.save(inventory);
			return inventory;
		} else
			throw new ItemNotFoundException(inventory.getId());
	}

	@Override
	public String deleteInventory(String id) throws Exception {
		Optional<Inventory> inventoryInDb = inventoryRepository.findById(id);
		if (inventoryInDb.isPresent()) {
			Inventory inventoryToDelete = inventoryInDb.get();
			inventoryToDelete.setDeletedFlag(true);
			inventoryToDelete = inventoryRepository.save(inventoryToDelete);
			return "Inventory deleted";
		} else
			return "Inventory Doesnot exist";
	}
}
