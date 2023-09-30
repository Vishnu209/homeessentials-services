package com.home.essentials.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.home.essentials.model.PaymentRequest;
import com.home.essentials.model.StripeClient;
import com.home.essentials.payload.FailureResponse;
import com.home.essentials.payload.SuccessResponse;
import com.home.essentials.repository.UserRepository;
import com.home.essentials.service.CartService;
import com.stripe.model.Charge;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;

@RestController
@Api(value = "Payment Controller", description = "REST API for Payment", tags = { "Payment Controller" })
@Slf4j
public class PaymentController {

	@Autowired
	private CartService cartService;

	@Autowired
	private UserRepository userRepository;

	private StripeClient stripeClient;

	@Autowired
	PaymentController(StripeClient stripeClient) {
		this.stripeClient = stripeClient;
	}

	@PostMapping("/api/payment/charge_Stripe")
	public ResponseEntity<?> chargeCard(@RequestBody PaymentRequest paymentRequest) {

		log.info("Creating payment entity!!");
		try {

			String token = paymentRequest.getToken();
			Double amount = paymentRequest.getAmount();

			Charge charge = null;
			charge = this.stripeClient.chargeNewCard(token, amount);
			// charge.setCustomer(user.getStripeCustId());

		} catch (

		Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ResponseEntity<>(new FailureResponse("Payment Failed"), HttpStatus.BAD_REQUEST);

		}
		return new ResponseEntity<>(new SuccessResponse("Payment is successfull"), HttpStatus.OK);

	}
}