package com.home.essentials.dto;

import com.home.essentials.model.RazorPayPaymentDetails;

import lombok.Data;

@Data
public class RazorPaySuccessDTO {

	private RazorPayPaymentDetails razorPayPaymentDetails;
	
	private String paymentStatus;
}
