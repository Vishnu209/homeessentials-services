package com.home.essentials.model;

import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Data;

@Data
@Document(collection = "RazorPaySuccessCallBack")
public class RazorPaySuccessCallBack {
	
	@Id
	private String id;

	@Field("razorpay_payment_id")
	private String razorpayPaymentId;
	
	@Field("razorpay_order_id")
	private String razorpayOrderId;
	
	@Field("razorpay_signature")
	private String razorpaySignature;
	
	@Field("created_date")
	private Instant createdDate;
}
