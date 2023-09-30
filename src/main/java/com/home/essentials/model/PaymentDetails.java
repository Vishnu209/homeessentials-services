package com.home.essentials.model;

import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Data;

@Data
public class PaymentDetails {

	@Field("brand")
	private String brand;

	@Field("funding")
	private String funding;

	private String id;

	@Field("last_4")
	private String last4;

	@Field("exp_month")
	private String expMonth;

	@Field("exp_year")
	private String expYear;
	
	@Field("gateway_name")
	private String gatewayName;

	@Field("bank_txn_id")
	private String bankTxnId;
}
