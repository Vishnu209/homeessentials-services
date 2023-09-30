package com.home.essentials.model;

import java.time.Instant;

import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Data;

@Data
public class RazorPayAcquirerData {

		
	@Field("auth_code")
	private String authCode;
	
	@Field("rrn")
	private String rrn;
	
	@Field("upi_transaction_id")
	private String upiTransactionId;
	
	@Field("bank_transaction_id")
	private String bankTransactionId;
	
	@Field("wallet_transaction_id")
	private String walletTransactionId; 
	
	@Field("created_date")
	private Instant createdDate;
}
