package com.home.essentials.payload;

import lombok.Data;

@Data
public class RazorPayCreateOrderRequest {

	private int amount;

	private String currency;

	private String customerId;
	
	private String homeOrderId;

}
