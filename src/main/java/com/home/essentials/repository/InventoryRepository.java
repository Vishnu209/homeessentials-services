package com.home.essentials.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.home.essentials.model.Inventory;

public interface InventoryRepository extends MongoRepository<Inventory, String>{

	Optional<Inventory> findByProductId(String productId);
	
}
