package com.home.essentials.payload;

import lombok.Data;

@Data
public class VerifyRazorPayRequest {

	private String rPayorderId;

	private String razorpayPaymentId;

	private String razorpaySignature;
}
