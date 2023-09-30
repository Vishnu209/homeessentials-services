package com.home.essentials.model;

import java.time.Instant;

import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Data;

@Data
public class RazorPayCardInfo {

		
	@Field("card_id")
	private String cardId;
	
	@Field("card_entity")
	private String cardEntity;
	
	@Field("card_holder_name")
	private String cardHolderName;
	
	@Field("card_last_4_digits")
	private String cardLast4;
	
	@Field("card_network")
	private String cardNetwork;
	
	@Field("card_Type")
	private String cardType;
	
	@Field("card_issuer")
	private String cardIssuer;
	
	@Field("card_international")
	private boolean cardInternational;
	
	@Field("card_emi")
	private boolean cardEmi;
		
	@Field("created_date")
	private Instant createdDate;
}
