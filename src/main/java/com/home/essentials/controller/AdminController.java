package com.home.essentials.controller;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.home.essentials.model.HomeNeedsOrder;
import com.home.essentials.model.Category;
import com.home.essentials.model.Counts;
import com.home.essentials.model.Product;
import com.home.essentials.model.User;
import com.home.essentials.payload.FailureResponse;
import com.home.essentials.payload.SuccessResponse;
import com.home.essentials.repository.CategoryRepository;
import com.home.essentials.repository.OrderRepository;
import com.home.essentials.repository.ProductRepository;
import com.home.essentials.repository.UserRepository;
import com.home.essentials.service.impl.CategoryServiceImpl;
import com.home.essentials.service.impl.ProductServiceImpl;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value = "Admin Controller", description = "REST API for Products And Categories", tags = { "Admin Controller" })
public class AdminController {

	@Autowired
	private ProductServiceImpl productServiceImpl;

	@Autowired
	private CategoryServiceImpl categoryServiceImpl;

	@Autowired
	private ProductServiceImpl productService;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private CategoryRepository categoryRepository;


	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private UserRepository userRepository;


	// product Api's
	@PostMapping(path = "/api/admin/createProducts")
	@ApiOperation(value = "Create Products")
	public ResponseEntity<Product> add(
			@RequestParam(value = "productImage", required = false) List<MultipartFile> productImage,
			@RequestParam("productRequest") String productRequestString) throws GeneralSecurityException, IOException {
		Product product = null;
		try {
			product = productService.saveProduct(productRequestString, productImage);
		} catch (Exception e) {

			e.printStackTrace();
		}
		return new ResponseEntity<>(product, HttpStatus.OK);
	}

	@PutMapping("/api/admin/updateProduct")
	@ApiOperation(value = "Update  Product By Id")
	public ResponseEntity<Product> updateProduct(
			@RequestParam(value = "file", required = false) List<MultipartFile> productImage,
			@RequestParam("productRequest") String productRequestString) throws Exception {

		Product product = null;
		try {
			product = productService.updateProduct(productRequestString, productImage);
		} catch (Exception e) {

			e.printStackTrace();
		}
		return new ResponseEntity<>(product, HttpStatus.OK);
	}

	@DeleteMapping("/api/admin/deleteProduct")
	@ApiOperation(value = "Delete Product By Id")
	public ResponseEntity<?> deleteProduct(@RequestParam("id") String id) throws Exception {
		if (productRepository.existsById(id)) {
			productServiceImpl.deleteProduct(id);
			return new ResponseEntity<>(new SuccessResponse("Product deleted successfully"), HttpStatus.OK);
		} else
			return new ResponseEntity<>(new FailureResponse("Product doesnot exist in the database"),
					HttpStatus.NOT_FOUND);

	}

//----------------------------------------------------------------------------------------------------------------------------------------------------------//

	// Category Api's
	@PostMapping("/api/admin/createCategories")
	@ApiOperation(value = "Create Categories")
	public ResponseEntity<Category> registerCategory(@RequestBody Category category) throws Exception {
		categoryServiceImpl.registerCategory(category);
		return new ResponseEntity<>(category, HttpStatus.OK);
	}

	@PutMapping(path = "/api/admin/updateCategory")
	@ApiOperation(value = "Update Category By Id")
	public ResponseEntity<?> updateCategory(@RequestParam("id") String id, @RequestBody Category category)
			throws Exception {
		HashMap<String, Object> resp = new HashMap<>();
		if (categoryRepository.existsById(id)) {
			categoryServiceImpl.updateCategory(category);
			resp.put("updated_category", category);

			return new ResponseEntity<>(resp, HttpStatus.OK);
		} else
			return new ResponseEntity<>(new FailureResponse("ProductCategory  not found in the Database"),
					HttpStatus.NOT_FOUND);
	}

	@DeleteMapping("/api/admin/deleteCategory")
	@ApiOperation(value = "Delete Category By Id")
	public ResponseEntity<?> deleteCategory(@RequestParam("id") String id) throws Exception {
		if (categoryRepository.existsById(id)) {
			categoryServiceImpl.deleteCategory(id);

			return new ResponseEntity<>(new SuccessResponse("ProductCategory deleted successfully"), HttpStatus.OK);
		} else
			return new ResponseEntity<>(new FailureResponse("ProductCategory not found in the Database"),
					HttpStatus.NOT_FOUND);
	}

	@GetMapping("/api/admin/totalCounts")
	@ApiOperation(value = "get all counts")
	public ResponseEntity<Counts> getAllCounts() throws Exception {

		List<Product> products = productRepository.findAll();
		List<Category> categories = categoryRepository.findAll();
		List<User> users = userRepository.findAll();
		List<HomeNeedsOrder> orders = orderRepository.findAll();
		Counts counts = new Counts();
		counts.setTotalCategories(categories.size());
		counts.setTotalOrders(orders.size());
		counts.setTotalProducts(products.size());
		counts.setTotalUsers(users.size());
		double income = 0.0;
		for (HomeNeedsOrder totalIncomeInOrders : orders) {
			income = income + totalIncomeInOrders.getTotal_Amount_to_Pay();
		}
		counts.setIncome(income);
		return new ResponseEntity<>(counts, HttpStatus.OK);

	}
}