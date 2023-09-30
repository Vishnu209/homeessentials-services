package com.home.essentials.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.home.essentials.model.RazorPayOrder;
import com.home.essentials.payload.RazorPayCreateOrderRequest;
import com.home.essentials.razorpay.service.RazorPayOrderService;
import com.home.essentials.razorpay.service.impl.VerifyRazorPayServiceImpl;
import com.razorpay.RazorpayException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value = "Razor Pay Payment Controller", description = "REST API for payments using razor pay", tags = { "Razor Pay Payment Controller"})
public class RazorPayPaymentController {
	
	@Autowired
	private RazorPayOrderService createOrderService;
	
	@Autowired
	private VerifyRazorPayServiceImpl verifyRazorPayServiceImpl;
	
	@PostMapping(value = "/razorpay/createOrder",produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Create Order with razor pay")
	public ResponseEntity<RazorPayOrder> createRPayOrder(@RequestBody RazorPayCreateOrderRequest request ) throws RazorpayException{

		RazorPayOrder response = createOrderService.createRazorPayOrder(request);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@GetMapping(value = "/razorpay/getOrder/{rpayId}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Get razor pay Order by id from DB")
	public ResponseEntity<RazorPayOrder> getRpayOrder(@PathVariable String rpayId) throws Exception{
		return new  ResponseEntity<>(createOrderService.getRpayOrder(rpayId), HttpStatus.OK);
	}

	@GetMapping(value = "/razorpay/getAllOrders", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Get all razor pay Orders  from DB")
	public ResponseEntity<List<RazorPayOrder>> getAllRpayOrders(){
		return new  ResponseEntity<>(createOrderService.getAllRpayOrders(), HttpStatus.OK);
	}
	
	@GetMapping(value = "/razorpay/fetchAllPaymentsForOrder/{rpazyOrderId}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Get all the payments made for a razor pay Order. Payment failed details can be found here")
	public ResponseEntity<?> fetchAllPaymentsForOrder(@PathVariable String rpazyOrderId) throws RazorpayException{
		return new  ResponseEntity<>(verifyRazorPayServiceImpl.getPaymentsForRpayOrder(rpazyOrderId), HttpStatus.OK);
	}
	
	
}
