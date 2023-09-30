package com.home.essentials.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.home.essentials.dto.PayTMPaymentUrlDTO;
import com.home.essentials.model.PayTMPayments;
import com.home.essentials.model.PayTMRequest;
import com.home.essentials.service.PaytmPageRedirectService;
import com.home.essentials.service.PaytmResponseService;
import com.home.essentials.service.TransactionStatusService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@Api(value = "PayTM Payment Transaction Controller", description = "REST APIs for paytm redirect url, get transaction status from DB ", tags = {
		"PayTM Payment Transaction Controller" })
public class PayTMTransactionController {

	@Autowired
	private TransactionStatusService transactionStatusService;

	@Autowired
	private PaytmPageRedirectService pageRedirectService;

	@Autowired
	private PaytmResponseService paytmResponseService;

	@PostMapping(value = "/payment/redirectToPayTM", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "After passing customer id ,order id and txn amount, this api redirects to the paytm payment page https://securegw-stage.paytm.in/order/process")
	public ResponseEntity<PayTMPaymentUrlDTO> pageRedirect(@RequestBody PayTMRequest payTMRequest) throws Exception {
		return new ResponseEntity<>(pageRedirectService.pageRedirectUrl(payTMRequest), HttpStatus.OK);
	}

	@GetMapping(value = "/transactionStatus", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "This API gets the transaction status corresponding to requested OrderId for specific merchant.")
	public ResponseEntity<?> transactionStatus(@RequestParam("orderId") String orderId) throws Exception {
		return ResponseEntity.ok(transactionStatusService.transactionStatus(orderId));
	}

	@GetMapping(value = "/getPayTMPayment/{paymentId}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Retrive payment transaction details by id for paytm ")
	public ResponseEntity<PayTMPayments> getTransactionDetails(@PathVariable String paymentId) throws Exception {
		return ResponseEntity.ok(transactionStatusService.getPayments(paymentId));
	}

	@GetMapping(value = "/getAllPayTMPayments", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Retrive all payment transaction details for paytm ")
	public ResponseEntity<List<PayTMPayments>> getAllTxnDetails() throws Exception {
		return ResponseEntity.ok(transactionStatusService.getAllPayTMPayments());
	}

}
