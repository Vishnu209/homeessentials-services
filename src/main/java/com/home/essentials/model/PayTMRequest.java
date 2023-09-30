package com.home.essentials.model;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties("paytm.payment.sandbox") // from the yml file
public class PayTMRequest {

private String merchantId;
	
	private String merchantKey;
	
	private String channelId;
	
	private String website;
	
	private String industryTypeId;
	
	private String paytmUrl;
	
	private String orderId;
	
	private String customerId;
	
	private String transactionAmount;
}
