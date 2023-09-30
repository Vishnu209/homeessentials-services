package com.home.essentials.service.impl;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.home.essentials.exception.ItemNotFoundException;
import com.home.essentials.model.HomeNeedsOrder;
import com.home.essentials.model.OrderStatus;
import com.home.essentials.model.User;
import com.home.essentials.repository.OrderRepository;
import com.home.essentials.repository.UserRepository;
import com.home.essentials.service.EmailService;
import com.home.essentials.service.OrderService;
import com.home.essentials.service.SequenceGeneratorService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private EmailService emailService;

	@Autowired
	private CartServiceImpl cartServiceImpl;

	@Autowired
	private SequenceGeneratorService sequenceService;

	@Override
	public HomeNeedsOrder registerOrder(HomeNeedsOrder order) throws Exception {
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.findAndRegisterModules().disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

		Optional<User> userInDb = userRepository.findById(order.getUserId());
		User user = null;
		if (userInDb.isPresent()) {
			user = userInDb.get();
		}
		List<User> adminUsers = userRepository.findTheUserByAdmin();
		List<String> adminEmails = new ArrayList<String>();
		for (User adminUser : adminUsers) {
			adminEmails.add(adminUser.getEmail());
		}

		try {
			order.setDeletedFlag(false);
			order.setCreatedDate(Instant.now());
			// auto-incrementor for the orderNumber field in order domain.
			long incrementer = sequenceService.generateSequence(String.valueOf(HomeNeedsOrder.SEQUENCE_NAME));

			String currentDate = DateTimeFormatter.ofPattern("yyyyMMdd").withZone(ZoneId.systemDefault())
					.format(Instant.now());
			log.info("Current Date = " + currentDate);
			order.setSequenceName(HomeNeedsOrder.SEQUENCE_NAME);
			String incrementerWithDate = currentDate + "0" + incrementer;
			order.setOrderNumber(incrementerWithDate);
			log.info("order number with Date = " + incrementerWithDate);
			//order.setShoppingCartEntry(user.getShoppingCartEntries());
			//log.info(order.getShoppingCartEntry().toString());
			String orderDateFormatted = DateTimeFormatter.ofPattern("dd/MM/yyyy").withZone(ZoneId.systemDefault())
					.format(Instant.now());
			log.info("order date formatted for attaching in email = " + orderDateFormatted);
			order.setOrderDateForEmail(orderDateFormatted);
			String name = SecurityContextHolder.getContext().getAuthentication().getName();
			Optional<User> loggedUser = userRepository.findByUsername(name);
			order.setCreatedby(loggedUser.get().getFirstName() + " " + loggedUser.get().getLastName());
			order = orderRepository.save(order);
			// clearing the cart after sending Email
			cartServiceImpl.removeTheCart(order.getUserId());

			// Sending email to the user after creating order
			// Email to the User
			emailService.sendOrderCreationEmail(user, order, "mail/OrderCreationEmail", "",
					adminEmails, false);
			// Email to the admin users
			emailService.sendOrderCreationEmailToAdmin(user, order, "mail/OrderCreationAdminEmail", "", adminEmails,
					true);

		} catch (Exception e) {

			order.setStatus(OrderStatus.FAILED);
			// Email to the admin users for failure
			emailService.sendOrderCreationEmailToAdmin(user, order, "mail/OrderCreationAdminEmail", "", adminEmails,
					true);
			e.printStackTrace();
			log.error("Email was not sent to the user!");

		}

		return order;
	}

	@Override
	public Optional<HomeNeedsOrder> getOrder(String orderId) throws Exception {
		Optional<HomeNeedsOrder> order = orderRepository.findById(orderId);
		if (order.isPresent()) {
			HomeNeedsOrder orderInDb = order.get();
			if (!orderInDb.isDeletedFlag())
				return order;
			else
				throw new ItemNotFoundException(orderId);
		}
		return null;
	}

	public List<HomeNeedsOrder> getAllOrder() throws Exception {
		Sort sort = Sort.by(Sort.Direction.DESC, "createdDate", "lastupdatedDate");
		List<HomeNeedsOrder> orders = orderRepository.findAll(sort);
		return orders;
	}

	@Override
	public HomeNeedsOrder updateOrder(HomeNeedsOrder order) throws Exception {
		Optional<HomeNeedsOrder> orderInDB = orderRepository.findById(order.getId());
		if (orderInDB.isPresent()) {
			HomeNeedsOrder editOrder = orderInDB.get();
			ObjectMapper mapper = new ObjectMapper();
			mapper.findAndRegisterModules().disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
			order.setDeletedFlag(false);
			order.setCreatedDate(editOrder.getCreatedDate());
			order.setLastupdatedDate(Instant.now());
			String name = SecurityContextHolder.getContext().getAuthentication().getName();
			Optional<User> loggedUser = userRepository.findByUsername(name);
			order.setUpdatedby(loggedUser.get().getFirstName() + " " + loggedUser.get().getLastName());
			if(order.getStatus().equals(OrderStatus.CANCELLED)) {
				if(order.getReasonForCancellation() == null || order.getReasonForCancellation().isEmpty()) {
						throw new Exception("Reason for order cancellation is mandatory");
				}
			}
			order = orderRepository.save(order);
			Optional<User> userInDb = userRepository.findById(editOrder.getUserId());
			User user = null;
			if (userInDb.isPresent()) {
				user = userInDb.get();
			}
			List<User> adminUsers = userRepository.findTheUserByAdmin();
			List<String> adminEmails = new ArrayList<String>();
			for (User adminUser : adminUsers) {
				adminEmails.add(adminUser.getEmail());
			}
			// Sending email to the user after creating order

			try {
				if (order.getStatus().equals(OrderStatus.OUT_FOR_DELIVERY)
						|| order.getStatus().equals(OrderStatus.SUBMITTED)) {
					emailService.sendOrderUpdationEmail(user, order, "mail/OrderUpdationEmail",
							"", null);
				}
				if (order.getStatus().equals(OrderStatus.DISPATCHED)) {
					emailService.sendOrderUpdationEmail(user, order, "mail/OrderDispatchedEmail", "", null);
				}
				// send email to user if order is delivered
				if (order.getStatus().equals(OrderStatus.DELIVERED)) {
					emailService.sendOrderUpdationEmail(user, order, "mail/OrderDeliveredEmail", "", null);
				}
				//
				if (order.getStatus().equals(OrderStatus.RETURNED)) {
					emailService.sendOrderUpdationEmail(user, order, "mail/OrderReturnedEmail", "", null);
				}

				// send email to admin users & user if the order is cancelled
				if (order.getStatus().equals(OrderStatus.CANCELLED)) {
					
					if(order.getReasonForCancellation() != null && !order.getReasonForCancellation().isEmpty()) {

					emailService.sendOrderUpdationEmail(user, order, "mail/OrderCancelledEmail", "", null);

					emailService.sendOrderUpdationEmailToAdmin(user, order, "mail/OrderCancelledAdminEmail", "",
							adminEmails, true);
				}
					if(order.getReasonForCancellation() == null || order.getReasonForCancellation().isEmpty()) {
						throw new Exception("Reason for order cancellation is mandatory");
					}
				}
				
			} catch (Exception e) {

				e.printStackTrace();
				log.error("Email was not sent to the user!");
			}

			return order;
		} else
			throw new ItemNotFoundException(order.getId());
	}

	@Override
	public String deleteOrder(String orderId) throws Exception {
		Optional<HomeNeedsOrder> orderInDb = orderRepository.findById(orderId);
		if (orderInDb.isPresent()) {
			HomeNeedsOrder orderToDelete = orderInDb.get();
			orderToDelete.setDeletedFlag(true);
			orderToDelete = orderRepository.save(orderToDelete);
			return "Order deleted";
		} else
			return "Order Doesnot exist";

	}

	public List<HomeNeedsOrder> getOrdersForToday() throws Exception {
		List<HomeNeedsOrder> orders = orderRepository.findAll();
		List<HomeNeedsOrder> ordersForToday = new ArrayList<>();

		orders.stream().forEach(x -> {
			String formattedOrderDate = DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(ZoneId.systemDefault())
					.format(x.getCreatedDate());
			String todaysDate = DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(ZoneId.systemDefault())
					.format(Instant.now());
			if (formattedOrderDate.equals(todaysDate)) {
				ordersForToday.add(x);
				ordersForToday.sort(Comparator.comparing(HomeNeedsOrder::getCreatedDate).reversed());
			} else
				orders.retainAll(orders);
		});
		return ordersForToday;
	}

	public List<HomeNeedsOrder> getTheOrdersById(String userId) {
		Sort sort = Sort.by(Sort.Direction.DESC, "createdDate", "lastupdatedDate");
		List<HomeNeedsOrder> orders = orderRepository.getOrdersByUserId(userId, sort);
		return orders;
	}

	@Override
	public List<HomeNeedsOrder> getOrderByStatus(OrderStatus orderStatus) throws Exception {
		Sort sort = Sort.by(Sort.Direction.DESC, "createdDate").descending();
		List<HomeNeedsOrder> orders = orderRepository.getOrdersByStatus(orderStatus, sort);
		return orders;
	}
}
