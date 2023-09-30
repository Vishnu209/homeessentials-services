package com.home.essentials.service;

import java.util.List;
import java.util.Optional;

import com.home.essentials.model.HomeNeedsOrder;
import com.home.essentials.model.OrderStatus;

public interface OrderService  {
	
	HomeNeedsOrder registerOrder(HomeNeedsOrder order) throws Exception;

    Optional<HomeNeedsOrder> getOrder(String orderId) throws Exception;

    HomeNeedsOrder updateOrder(HomeNeedsOrder order) throws Exception;

    String deleteOrder(String orderId) throws Exception;
    
    List<HomeNeedsOrder> getOrderByStatus(OrderStatus orderStatus) throws Exception;
}