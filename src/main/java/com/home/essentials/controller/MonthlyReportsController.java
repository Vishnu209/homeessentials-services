package com.home.essentials.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.home.essentials.dto.MonthlyReports;
import com.home.essentials.dto.ProductCategory;
import com.home.essentials.service.MonthlyOrderService;
import com.home.essentials.service.MonthlyUsersRegisteredService;
import com.home.essentials.service.MostSellingProductCategoryService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value = "Monthly Reports Controller", description = "REST API for Monthly Reports ", tags = {
		"Monthly Reports Controller" })
public class MonthlyReportsController {

	@Autowired
	private MonthlyOrderService monthlyOrderService;

	@Autowired
	private MonthlyUsersRegisteredService monthlyUsersRegisteredService;

	@Autowired
	private MostSellingProductCategoryService mostSellingProductCategoryService;

	@GetMapping("/api/requestOrderCountMonthly")
	@ApiOperation(value = " Get the number of orders placed monthly  ")
	public ResponseEntity<List<MonthlyReports>> getOrdersMonthly() {
		List<MonthlyReports> orderReport = monthlyOrderService.getMonthlyOrders();
		return new ResponseEntity<>(orderReport, HttpStatus.OK);
	}

	@GetMapping(value = "/api/requestUserCountMonthly")
	@ApiOperation(value = " Get the number of users registered monthly  ")

	public ResponseEntity<List<MonthlyReports>> getUsersRegisteredMonthly() {
		List<MonthlyReports> userReport = monthlyUsersRegisteredService.getMonthlyUsers();
		return new ResponseEntity<>(userReport, HttpStatus.OK);
	}

	@GetMapping("/api/getMostSellingProductCategory")
	@ApiOperation(value = " Get the most selling product category")
	public ResponseEntity<List<ProductCategory>> getMostSellingProductCategory() {
		List<ProductCategory> result = mostSellingProductCategoryService.getProductCategory();
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

}
