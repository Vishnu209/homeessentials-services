package com.home.essentials.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.home.essentials.dto.ProductCategory;
import com.home.essentials.model.HomeNeedsOrder;
import com.home.essentials.model.ShoppingCartEntry;
import com.home.essentials.repository.OrderRepository;
import com.home.essentials.service.MostSellingProductCategoryService;

@Service
public class MostSellingProductCategoryServiceImpl implements MostSellingProductCategoryService {

	@Autowired
	private OrderRepository orderRepository;

	@Override
	public List<ProductCategory> getProductCategory() {

		List<ProductCategory> productCategories = new ArrayList<ProductCategory>();
		List<HomeNeedsOrder> ordersInDb = orderRepository.findAll();

		//int ordersWithGrocery = 0;
		//int ordersWithFruits = 0;
		//int ordersWithVeg = 0;
		//int ordersWithDried = 0;
		ProductCategory countForGrocery = new ProductCategory();
		ProductCategory countForFruits = new ProductCategory();
		ProductCategory countForVeg = new ProductCategory();
		ProductCategory countForDried = new ProductCategory();

		List<HomeNeedsOrder> ordersWithGrocery = new ArrayList<HomeNeedsOrder>();
		List<HomeNeedsOrder> ordersWithFruits = new ArrayList<HomeNeedsOrder>();
		List<HomeNeedsOrder> ordersWithVeg = new ArrayList<HomeNeedsOrder>();
		List<HomeNeedsOrder> ordersWithDried = new ArrayList<HomeNeedsOrder>();
		for (HomeNeedsOrder order : ordersInDb) {

			List<ShoppingCartEntry> cartEntry = order.getShoppingCartEntry();

			for (ShoppingCartEntry entry : cartEntry) {
				if (entry.getProductCategory() != null) {
					if (entry.getProductCategory().equalsIgnoreCase("GROCERY")) {
						//ordersWithGrocery = ordersWithGrocery + entry.getQuantity();
						ordersWithGrocery.add(order);
					}
					if (entry.getProductCategory().equalsIgnoreCase("FRUITS")) {
						ordersWithFruits.add(order);// = ordersWithFruits + entry.getQuantity();
					}
					if (entry.getProductCategory().equalsIgnoreCase("VEG")) {
						ordersWithVeg.add(order); //= ordersWithVeg + entry.getQuantity();
					}
					if (entry.getProductCategory().equalsIgnoreCase("DRIED")) {
						ordersWithDried.add(order);// = ordersWithDried + entry.getQuantity();
					}

				}
			}
		}
		/*ProductCategory type = new ProductCategory();
		type.setProductCategory("GROCERY");
		type.setCount(ordersWithGrocery);
		productCategories.add(type);
		ProductCategory type1 = new ProductCategory();
		type1.setProductCategory("FRUITS");
		type1.setCount(ordersWithFruits);
		productCategories.add(type1);
		ProductCategory type2 = new ProductCategory();
		type2.setProductCategory("DRIED");
		type2.setCount(ordersWithDried);
		productCategories.add(type2);
		ProductCategory type3 = new ProductCategory();
		type3.setProductCategory("VEG");
		type3.setCount(ordersWithVeg);
		productCategories.add(type3);*/
		
		countForGrocery.setCount(ordersWithGrocery.size());
		countForGrocery.setProductCategory("GROCERY");
		productCategories.add(countForGrocery);
		
		countForDried.setCount(ordersWithDried.size());
		countForDried.setProductCategory("DRIED");
		productCategories.add(countForDried);
		
		countForFruits.setCount(ordersWithFruits.size());
		countForFruits.setProductCategory("FRUITS");
		productCategories.add(countForFruits);
		
		countForVeg.setCount(ordersWithVeg.size());
		countForVeg.setProductCategory("VEG");
		productCategories.add(countForVeg);
		return productCategories;
	}

}
