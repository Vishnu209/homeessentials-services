package com.home.essentials.model;

import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Data;

@Data
@Document(collection = "PayTMPayments")
public class PayTMPayments {
	
	@Id
	private String id;
	
	@Field("bank_name")
	private String bankName;
	
	@Field("bank_txn_id")
	private String bankTxnId;
	
	@Field("currency")
	private String currency;
	
	@Field("gateway")
	private String gatewayName;
	
	@Field("mid")
	private String mid;
	
	@Field("order_id")
	private String orderId;
	
	@Field("payment_mode")
	private String paymentMode;
	
	@Field("response_code")
	private String responseCode;
	
	@Field("response_message")
	private String responseMessage;
	
	@Field("status")
	private String status;
	
	@Field("txn_amount")
	private String txnAmount;
	
	@Field("txn_date")
	private String txnDate;
	
	@Field("txn_id")
	private String txnId;
	
	//@Field("created_by")
	//private String createdBy;
	
	@Field("created_date")
	private Instant created_date;
	
		
}
