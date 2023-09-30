package com.home.essentials.razorpay.service;

import java.util.List;

import com.home.essentials.model.RazorPayOrder;
import com.home.essentials.payload.RazorPayCreateOrderRequest;
import com.razorpay.RazorpayException;

public interface RazorPayOrderService {
	
	RazorPayOrder createRazorPayOrder(RazorPayCreateOrderRequest req) throws RazorpayException;
	
	RazorPayOrder getRpayOrder(String id) throws Exception;
	
	List<RazorPayOrder> getAllRpayOrders();

}
