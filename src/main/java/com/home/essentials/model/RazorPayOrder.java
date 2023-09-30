package com.home.essentials.model;

import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Data;

@Data
@Document(collection = "RazorPayOrder")
public class RazorPayOrder {

	@Id
	private String id;

	@Field("razor_pay_order_id")
	private String rPayOrderId;

	@Field("entity")
	private String entity;

	@Field("amount")
	private int amount;

	@Field("amount_paid")
	private int amountPaid;

	@Field("currency")
	private String currency;

	@Field("receipt")
	private String receipt;

	@Field("created")
	private String status;

	@Field("attempts")
	private int attempts;
	
	@Field("notes")
	private RazorPayNotes notes;
	
	@Field("key_id")
	private String keyId;

	@Field("created_Date")
	private Instant createdDate;
	
	@Field("last_updated_date")
	private Instant lastUpdatedDate;

}
