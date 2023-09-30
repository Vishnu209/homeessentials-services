package com.home.essentials.model;

import lombok.Data;

@Data
public class CardTokenRequest {

	
	private String number;
	
	private String exp_month;
	
	private String exp_year;
	
	private String cvc;
}
