package com.home.essentials.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.home.essentials.model.HomeNeedsOrder;
import com.home.essentials.payload.VerifyRazorPayRequest;
import com.home.essentials.razorpay.service.VerifyRazorPayService;

import io.swagger.annotations.Api;

@RestController
@Api(value = "Razor Pay Success Call Back Controller", description = "REST API for razor pay success call back fields", tags = {
		"Razor Pay Success Call Back Controller" })
public class RazorPaySuccessCallBackController {

	@Autowired
	private VerifyRazorPayService verifyRazorPayService;

	@PostMapping("/razorpay/verifySignature")
	public ResponseEntity<HomeNeedsOrder> verifySignature(@RequestBody VerifyRazorPayRequest payRequest)
			throws Exception {
		return new ResponseEntity<>(verifyRazorPayService.verifyRazorPay(payRequest), HttpStatus.OK);
	}

}
