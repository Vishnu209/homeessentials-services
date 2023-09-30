package com.home.essentials.controller;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.home.essentials.model.Inventory;
import com.home.essentials.model.Product;
import com.home.essentials.payload.FailureResponse;
import com.home.essentials.repository.ProductRepository;
import com.home.essentials.service.impl.InventoryServiceImpl;
import com.home.essentials.service.impl.ProductServiceImpl;
import com.home.essentials.utils.AmazonClient;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value = "Product Controller", description = "REST API for Products ", tags = { "Product Controller" })
public class ProductController {

	@Autowired
	private ProductServiceImpl productServiceImpl;

	@Autowired
	private InventoryServiceImpl inventoryServiceImpl;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	AmazonClient amazonClient;

	@GetMapping("/api/getProductImage")
	@ApiOperation(value = "Get Product Image By image name")
	public ResponseEntity<?> getProductsImageByName(@RequestParam("productImage") String productImage)
			throws Exception {

		InputStream is = amazonClient.getFile(productImage);
		byte[] prodImage = IOUtils.toByteArray(is);

		return new ResponseEntity<>(prodImage, HttpStatus.OK);
	}

	@GetMapping("/api/getProduct")
	@ApiOperation(value = "Get Product By Id", response = Product.class)
	public ResponseEntity<?> getProduct(@RequestParam("id") String id) throws Exception {
		Optional<Product> product = productServiceImpl.getProduct(id);
		Optional<Inventory> productInventory = inventoryServiceImpl.getInventoryByProductId(id);
		if (productRepository.existsById(id)) {
			product.get().setAvailable_stock(productInventory.get().getAvailable_stock());
			product.get().setSold(productInventory.get().getSold());

			return new ResponseEntity<>(product, HttpStatus.OK);

		} else
			return new ResponseEntity<>(new FailureResponse("Product  not found in the Database"),
					HttpStatus.NOT_FOUND);
	}

	@GetMapping("/api/getAllProducts")
	@ApiOperation(value = "Get All Products")
	public ResponseEntity<?> getAllProduct() throws Exception {
		List<Product> products = productServiceImpl.getAllProduct();
		List<Product> productResp = new ArrayList<Product>();
		for (Product prod : products) {
			Optional<Inventory> productInventory = inventoryServiceImpl.getInventoryByProductId(prod.getId());
			prod.setAvailable_stock(productInventory.get().getAvailable_stock());
			prod.setSold(productInventory.get().getSold());
			productResp.add(prod);
		}
		return new ResponseEntity<>(productResp, HttpStatus.OK);
	}
	
	@GetMapping("/api/home/getAllProducts")
	@ApiOperation(value = "Get All Products for Home page ")
	public ResponseEntity<?> getAllProductForHome() throws Exception {
		List<Product> products = productServiceImpl.getAllProduct();
		List<Product> productResp = new ArrayList<Product>();
		for (Product prod : products) {
			Optional<Inventory> productInventory = inventoryServiceImpl.getInventoryByProductId(prod.getId());
			prod.setAvailable_stock(productInventory.get().getAvailable_stock());
			prod.setSold(productInventory.get().getSold());
			productResp.add(prod);
		}
		return new ResponseEntity<>(productResp, HttpStatus.OK);
	}

	@GetMapping("/api/getProducts/categoryId")
	@ApiOperation(value = "Get Products By CategoryId")
	public ResponseEntity<?> getProductsByCategoryId(@RequestParam("categoryId") String categoryId) throws Exception {
		List<Product> product = productServiceImpl.getproductBycategoryId(categoryId);

		return new ResponseEntity<>(product, HttpStatus.OK);
	}

	@GetMapping("/api/getProducts/productName")
	@ApiOperation(value = "Get Product By ProductName")
	public ResponseEntity<?> getProductsByProductName(@RequestParam("productName") String productName)
			throws Exception {

		List<Product> products = productServiceImpl.getProductByProductName(productName);

		return new ResponseEntity<>(products, HttpStatus.OK);
	}
}
