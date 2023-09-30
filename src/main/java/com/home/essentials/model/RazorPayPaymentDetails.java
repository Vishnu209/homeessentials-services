package com.home.essentials.model;

import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Data;

@Data
@Document(collection = "RazorPayPaymentDetails")
public class RazorPayPaymentDetails {

	@Id
	private String id;
	
	@Field("entity")
	private String entity;
	
	@Field("amount")
	private int amount;
	
	@Field("currency")
	private String currency;
	
	@Field("status")
	private String status;
		
	@Field("method")
	private String method;
	
	@Field("home_order_id")
	private String homeOrderId;
	
	@Field("razor_pay_order_id")
	private String rpayOrderId;
	
	@Field("razor_pay_payment_id")
	private String rpayPaymentId;
	
	@Field("razor_pay_signature")
	private String rpaySignature;
	
	@Field("invoice_id")
	private String invoiceId;
	
	@Field("description")
	private String description;

	@Field("razor_pay_card_info")
	private RazorPayCardInfo razorPayCardInfo;
	
	@Field("bank")
	private String bank;
	
	@Field("wallet")
	private String wallet;
	
	@Field("vpa")
	private String vpa;
	
	@Field("international")
	private boolean international;
	
	@Field("refund_status")
	private String refundStatus;
	
	@Field("amount_refunded")
	private int amountRefunded; 
	
	@Field("captured")
	private boolean captured;
	
	@Field("email")
	private String email;
	
	@Field("contact")
	private String contact;
	
	@Field("fee")
	private int fee;
	
	@Field("tax")
	private int tax;
	
	@Field("razor_pay_acquirer_data")
	private RazorPayAcquirerData razorPayAcquirerData;
	
	@Field("error_code")
	private String errorCode;
	
	@Field("error_description")
	private String errorDescription;
	
	@Field("customer_id")
	private String customerId;
	
	@Field("created_date")
	private Instant createdDate;
}
