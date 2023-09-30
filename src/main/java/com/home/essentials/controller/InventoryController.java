package com.home.essentials.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.home.essentials.model.Inventory;
import com.home.essentials.payload.FailureResponse;
import com.home.essentials.repository.InventoryRepository;
import com.home.essentials.service.impl.InventoryServiceImpl;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value = "Inventory Controller", description = "REST API for Inventory", tags = { "Inventory Controller" })
public class InventoryController {

	@Autowired
	private InventoryServiceImpl inventoryServiceImpl;

	@Autowired
	private InventoryRepository inventoryRepository;

	@GetMapping("/api/getInventory")
	@ApiOperation(value = "Get Inventory By Id")
	public ResponseEntity getInventory(@RequestParam("id") String id) throws Exception {
		Optional<Inventory> inventory = inventoryServiceImpl.getInventory(id);
		if (inventoryRepository.existsById(id)) {
			return new ResponseEntity<>(inventory, HttpStatus.OK);
		} else
			return new ResponseEntity<>(new FailureResponse("Inventory  not found in the Database"),
					HttpStatus.NOT_FOUND);
	}

	@GetMapping("/api/inventory/getProduct")
	@ApiOperation(value = "Get Product in Inventory")
	public ResponseEntity getProductInInventory(@RequestParam("productId") String id) throws Exception {
		Optional<Inventory> productInInventory = inventoryServiceImpl.getInventoryByProductId(id);
		if (inventoryRepository.existsById(productInInventory.get().getId())) {
			return new ResponseEntity<>(productInInventory, HttpStatus.OK);
		} else
			return new ResponseEntity<>(new FailureResponse("Product  not found in the Inventory"),
					HttpStatus.NOT_FOUND);
	}

	@GetMapping("/api/getAllInventory")
	@ApiOperation(value = "Get All Inventory")
	public ResponseEntity getAllInventory() throws Exception {
		List<Inventory> inventories = inventoryServiceImpl.getAllInventory();
		return new ResponseEntity<>(inventories, HttpStatus.OK);
	}

	@PutMapping("/api/updateInventory")
	@ApiOperation(value = "Update Inventory By Id")
	public ResponseEntity updateInventory(@RequestParam("id") String id, @RequestBody Inventory inventory)
			throws Exception {
		HashMap<String, Object> resp = new HashMap<>();
		if (inventoryRepository.existsById(id)) {
			inventoryServiceImpl.updateInventory(inventory);
			resp.put("edited_inventory", inventory);
			return new ResponseEntity<>(resp, HttpStatus.OK);
		} else
			return new ResponseEntity<>(new FailureResponse("Inventory not found in the Database"),
					HttpStatus.NOT_FOUND);
	}

	@DeleteMapping("/api/deleteInventory")
	@ApiOperation(value = "Delete Inventory By Id")
	public ResponseEntity deleteInventory(@RequestParam("id") String id) throws Exception {
		if (inventoryRepository.existsById(id)) {
			inventoryRepository.deleteById(id);
			return new ResponseEntity<>(new FailureResponse("Inventory deleted"), HttpStatus.BAD_REQUEST);
		} else
			return new ResponseEntity<>(new FailureResponse("Inventory not found in the Database"),
					HttpStatus.NOT_FOUND);
	}

}