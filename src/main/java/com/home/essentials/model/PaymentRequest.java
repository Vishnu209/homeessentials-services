package com.home.essentials.model;

import lombok.Data;

@Data
public class PaymentRequest {

	private String token;
	
	private Double amount;
}
