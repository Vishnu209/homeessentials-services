package com.home.essentials.service.impl;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.home.essentials.exception.BadRequestException;
import com.home.essentials.exception.ItemNotFoundException;
import com.home.essentials.exception.ResourceNotFoundException;
import com.home.essentials.model.Category;
import com.home.essentials.model.Inventory;
import com.home.essentials.model.Product;
import com.home.essentials.model.RatingAndReview;
import com.home.essentials.model.User;
import com.home.essentials.repository.CategoryRepository;
import com.home.essentials.repository.InventoryRepository;
import com.home.essentials.repository.ProductRepository;
import com.home.essentials.repository.UserRepository;
import com.home.essentials.service.ProductService;
import com.home.essentials.utils.AmazonClient;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	AmazonClient amazonClient;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private InventoryRepository inventoryRepository;

	@Override
	public Product saveProduct(String productRequest, List<MultipartFile> productImageLt) throws Exception {

		Instant date = Instant.now();
		String formattedDate = DateTimeFormatter.ofPattern("yyyyMMddHHmmss").withZone(ZoneId.systemDefault())
				.format(date);

		Product product = null;

		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.findAndRegisterModules().disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
			product = mapper.readValue(productRequest, Product.class);
		} catch (IOException e) {
			log.error("Error while converting input string to product request!!", e);
			throw new Exception("Error while converting input string to product request!!");
		}

		List<String> productImageUrlLt = new ArrayList<String>();
		if (productImageLt != null && !productImageLt.isEmpty()) {
			log.info("product image present!!!");
			for (MultipartFile productImage : productImageLt) {
				DBObject metadata = new BasicDBObject();
				metadata.put("materialType", "productImage");
				String contentS3Url;

				String filename = "Home Needs/Product Images/" + formattedDate + "-"
						+ productImage.getOriginalFilename();

				// UPLOADING TO S3

				contentS3Url = amazonClient.uploadContent(productImage, null, metadata, filename,
						productImage.getInputStream());
				productImageUrlLt.add(contentS3Url);
			}
		}
		product.setProductImageUrl(productImageUrlLt);
		product.setCreatedDate(Instant.now());
		product.setDeletedFlag(false);
		List<RatingAndReview> reviews = new ArrayList<RatingAndReview>();
		product.setReviews(reviews);
		String user = SecurityContextHolder.getContext().getAuthentication().getName();
		Optional<User> loggedUser = userRepository.findByUsername(user);
		product.setCreatedBy(loggedUser.get().getFirstName() + " " + loggedUser.get().getLastName());

		if (product.getOffer() != null && !product.getOffer().isEmpty() && Integer.parseInt(product.getOffer()) > 0) {

			int productCost = Integer.parseInt(product.getProductCost());
			int offer = Integer.parseInt(product.getOffer());
			int productDiscountedCost = productCost - (productCost * offer / 100);
			product.setProductDiscountedCost(productDiscountedCost);

		}

		if (product.getOffer() != null && !product.getOffer().isEmpty() && Integer.parseInt(product.getOffer()) > 100) {
			throw new Exception("offer should be less than 100");
		}

		if (product.getOffer() != null && !product.getOffer().isEmpty() && Integer.parseInt(product.getOffer()) < 0) {
			throw new Exception("offer cannot be less than 0");
		}

		Optional<Category> categoryForProduct = categoryRepository.findById(product.getCategoryId());
		if (categoryForProduct.isPresent()) {
			Category productCategory = categoryForProduct.get();
			int productCount = productCategory.getProductCount();
			productCount = productCount + 1;
			productCategory.setProductCount(productCount);
			productCategory = categoryRepository.save(productCategory);
		}
		product = productRepository.save(product);

		// Saving the inventory with the product details
		Inventory inventory = new Inventory();
		inventory.setProductId(product.getId());
		inventory.setAvailable_stock(product.getAvailable_stock());
		inventory.setSold(product.getSold());
		inventory.setDeletedFlag(false);
		inventory.setProductName(product.getProductName());
		inventory.setCreatedDate(Instant.now());
		inventoryRepository.save(inventory);
		return product;
	}

	@Override
	public Product updateProduct(String productRequest, List<MultipartFile> productImageLt) throws Exception {
		Instant date = Instant.now();
		String formattedDate = DateTimeFormatter.ofPattern("yyyyMMddHHmmss").withZone(ZoneId.systemDefault()).format(date);

		Product product = null;
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.findAndRegisterModules().disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
			product = mapper.readValue(productRequest, Product.class);
		} catch (IOException e) {
			log.error("Error while converting input string to product request!!", e);
			throw new Exception("Error while converting input string to product request!!");
		}

		Optional<Product> productInDb = productRepository.findById(product.getId());
		if (productInDb.isPresent()) {
			Product editProduct = productInDb.get();
			// if product image is not null, upload to s3 & set the url in productImageUrl,
			// else retain the previously saved image in productImageUrl

			List<String> productImageUrlLt = new ArrayList<String>();

			// ALWAYS DELETE EXISTING IMAGES FROM S3 AND SAVE THE NEW LIST
			List<String> existingProdImageUrl = editProduct.getProductImageUrl() != null
					&& !editProduct.getProductImageUrl().isEmpty() ? editProduct.getProductImageUrl() : null;
			if (existingProdImageUrl != null) {

				for (String imageUrl : existingProdImageUrl) {

					amazonClient.deleteFileFromS3(imageUrl);
				}
			}

			if (productImageLt != null && !productImageLt.isEmpty()) {
				for (MultipartFile productImage : productImageLt) {
					DBObject metadata = new BasicDBObject();
					metadata.put("materialType", "productImage");
					String contentS3Url;
					String filename = "Home Needs/Product Images/" + formattedDate + "-"
							+ productImage.getOriginalFilename();
					// UPLOADING TO S3
					contentS3Url = amazonClient.uploadContent(productImage, null, metadata, filename,
							productImage.getInputStream());
					productImageUrlLt.add(contentS3Url);

				}
				product.setProductImageUrl(productImageUrlLt);

			}
			product.setCreatedDate(editProduct.getCreatedDate());
			product.setDeletedFlag(false);
			product.setLastupdatedDate(Instant.now());
			String user = SecurityContextHolder.getContext().getAuthentication().getName();
			Optional<User> loggedUser = userRepository.findByUsername(user);
			product.setUpdatedBy(loggedUser.get().getFirstName() + " " + loggedUser.get().getLastName());
			if (product.getOffer() != null && !product.getOffer().isEmpty()
					&& Integer.parseInt(product.getOffer()) > 0) {
				int productCost = Integer.parseInt(product.getProductCost());
				int offer = Integer.parseInt(product.getOffer());
				int productDiscountedCost = productCost - (productCost * offer / 100);
				product.setProductDiscountedCost(productDiscountedCost);
			}
			if (product.getOffer() != null && !product.getOffer().isEmpty()
					&& Integer.parseInt(product.getOffer()) > 100) {
				throw new Exception("offer should be less than 100");
			}
			if (product.getOffer() != null && !product.getOffer().isEmpty()
					&& Integer.parseInt(product.getOffer()) < 0) {
				throw new Exception("offer cannot be less than 0");
			}
			product = productRepository.save(product);
			// Updating the inventory
			Optional<Inventory> inventoryForProduct = inventoryRepository.findByProductId(product.getId());
			inventoryForProduct.get().setProductId(product.getId());
			inventoryForProduct.get().setAvailable_stock(product.getAvailable_stock());
			inventoryForProduct.get().setSold(product.getSold());
			inventoryForProduct.get().setDeletedFlag(false);
			inventoryForProduct.get().setProductName(product.getProductName());
			inventoryForProduct.get().setCreatedDate(Instant.now());
			inventoryRepository.save(inventoryForProduct.get());
		} else {
			throw new Exception("product not found in database");
		}
		return product;
	}

	@Override
	public Optional<Product> getProduct(String productId) {
		Optional<Product> product = productRepository.findById(productId);
		if (product.isPresent()) {
			Product productInDb = product.get();
			if (!productInDb.isDeletedFlag()) // checking if the DeletedFlag is false
				return product;
			else
				throw new ItemNotFoundException(productId);
		}
		return null;
	}

	public List<Product> getAllProduct() throws BadRequestException {
		List<Product> allProducts = productRepository.findAll();
		List<Product> products = new ArrayList<>();
		allProducts.stream().forEach(x -> {
			if (!x.isDeletedFlag())
				products.add(x);
			else
				allProducts.retainAll(allProducts);
		});
		return products;
	}

	@Override
	public String deleteProduct(String productId) throws Exception {
		Optional<Product> productInDB = productRepository.findById(productId);
		if (productInDB.isPresent()) {
			Product productToDelete = productInDB.get();
			productToDelete.setDeletedFlag(true);
			Optional<Category> categoryForProduct = categoryRepository.findById(productToDelete.getCategoryId());
			if (categoryForProduct.isPresent()) {
				Category productCategory = categoryForProduct.get();
				int productCount = productCategory.getProductCount();
				productCount = productCount - 1;
				productCategory.setProductCount(productCount);
				productCategory = categoryRepository.save(productCategory);
			}
			productToDelete = productRepository.save(productToDelete);
			return "Product deleted Successfully";
		} else
			throw new ItemNotFoundException(productId);
	}

	public List<Product> getproductBycategoryId(String categoryId) {
		List<Product> products = productRepository.findAllByCategoryId(categoryId);
		return products;
	}

	public List<Product> getProductByProductName(String productName) {
		List<Product> products = productRepository.findByProductName(productName);
		List<Product> productsWithSameProductName = new ArrayList<>();
		for (Product product : products) {
			Optional<Product> prod = productRepository.findById(product.getId());
			if (prod.isPresent()) {
				Product productInDb = prod.get();
				productName = productName.trim();
				if (productInDb.getProductName().equalsIgnoreCase(productName))
					productsWithSameProductName.add(product);
				else
					throw new ItemNotFoundException(productInDb.getId());
			} else
				throw new ResourceNotFoundException("Product", "productName", product.getProductName());
		}
		return productsWithSameProductName;
	}

	// Uploading banner images to s3
	/*
	 * public List<String> saveBanners(List<MultipartFile> bannerImageLt) throws
	 * Exception { List<String> bannerImageUrlLt = new ArrayList<String>();
	 * if(bannerImageLt != null && !bannerImageLt.isEmpty()) {
	 * log.info("banners present!!!"); for (MultipartFile bannerImage :
	 * bannerImageLt) { DBObject metadata = new BasicDBObject();
	 * metadata.put("materialType", "banners"); String contentS3Url; String filename
	 * = "Banners/" + formattedDate + "-" + bannerImage.getOriginalFilename();
	 * 
	 * // UPLOADING TO S3
	 * 
	 * contentS3Url = amazonClient.uploadContent(bannerImage, null, metadata,
	 * filename, bannerImage.getInputStream()); bannerImageUrlLt.add(contentS3Url);
	 * } } return bannerImageUrlLt;
	 * 
	 * }
	 */
}
