package com.home.essentials.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.home.essentials.model.HomeNeedsOrder;
import com.home.essentials.model.OrderStatus;
import com.home.essentials.repository.OrderRepository;
import com.home.essentials.service.impl.OrderServiceImpl;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value = "Order Controller", description = "REST API for Product Order", tags = { "Order Controller" })

public class OrderController {

	@Autowired
	private OrderServiceImpl orderServiceImpl;

	@Autowired
	private OrderRepository orderRepository;

	@PostMapping("/api/order/createOrder")
	@ApiOperation(value = "Create Order")
	public ResponseEntity<HomeNeedsOrder> createOrder(@RequestBody HomeNeedsOrder order) throws Exception {
		orderServiceImpl.registerOrder(order);
		return new ResponseEntity<>(order, HttpStatus.OK);
	}

	@GetMapping("/api/order/getOrder")
	@ApiOperation(value = "Get Order By Id")
	public ResponseEntity<?> getOrder(@RequestParam("id") String id) throws Exception {
		Optional<HomeNeedsOrder> order = orderServiceImpl.getOrder(id);

		return new ResponseEntity<>(order, HttpStatus.OK);
	}

	@GetMapping("/api/order/getAllOrders")
	@ApiOperation(value = "Get All Orders")
	public ResponseEntity<List<HomeNeedsOrder>> getAllOrders() throws Exception {
		List<HomeNeedsOrder> orders = orderServiceImpl.getAllOrder();

		return new ResponseEntity<>(orders, HttpStatus.OK);
	}

	@PutMapping("/api/order/updateOrder")
	@ApiOperation(value = "Update Order By Id")
	public ResponseEntity<?> updateOrder(@RequestParam("id") String id, @RequestBody HomeNeedsOrder order) throws Exception {
		HashMap<String, Object> resp = new HashMap<>();
		if (orderRepository.existsById(id)) {
			orderServiceImpl.updateOrder(order);
			resp.put("updated_order", order);
			return new ResponseEntity<>(resp, HttpStatus.OK);
		} else
			return new ResponseEntity<>("Order not found", HttpStatus.NOT_FOUND);
	}

	@DeleteMapping("/api/order/deleteOrder")
	@ApiOperation(value = "Delete Order By Id")
	public ResponseEntity<?> deleteOrder(@RequestParam("id") String id) throws Exception {
		orderServiceImpl.deleteOrder(id);
		HashMap<String, String> resp = new HashMap<>();
		resp.put("message", "Order is successfully deleted");
		return new ResponseEntity<>(resp, HttpStatus.OK);
	}

	@GetMapping("/api/getOrdersForToday")
	@ApiOperation(value = "Get Orders for today")
	public ResponseEntity<List<HomeNeedsOrder>> getTheOrdersForToday() throws Exception {
		List<HomeNeedsOrder> orders = orderServiceImpl.getOrdersForToday();
		return new ResponseEntity<>(orders, HttpStatus.OK);
	}

	@GetMapping("/api/getMyOrders")
	@ApiOperation(value = "Get Orders for User")
	public ResponseEntity<List<HomeNeedsOrder>> getOrdersForUser(@RequestParam("userId") String userId) {
		List<HomeNeedsOrder> ordersForUser = orderServiceImpl.getTheOrdersById(userId);
		return ResponseEntity.ok(ordersForUser);
	}

	@GetMapping("/api/ordersByStatus/{orderStatus}")
	@ApiOperation(value = "Get Orders by status")
	public ResponseEntity<List<HomeNeedsOrder>> getOrdersByStatus(@PathVariable String orderStatus) {
		List<HomeNeedsOrder> orders = null;
		try {
			orders = orderServiceImpl.getOrderByStatus(OrderStatus.valueOf(orderStatus));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.ok(orders);
	}
}
