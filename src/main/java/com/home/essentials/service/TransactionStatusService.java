package com.home.essentials.service;

import java.util.List;

import com.home.essentials.model.PayTMPayments;

public interface TransactionStatusService {

	String transactionStatus(String orderId) throws Exception;
	
	PayTMPayments getPayments(String paymentId) throws Exception;
	
	List<PayTMPayments> getAllPayTMPayments();
}
