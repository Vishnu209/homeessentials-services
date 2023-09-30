package com.home.essentials.service.impl;

import java.net.URI;
import java.net.URLEncoder;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.home.essentials.dto.PayTMPaymentUrlDTO;
import com.home.essentials.model.PayTMRequest;
import com.home.essentials.model.PaytmDetails;
import com.home.essentials.service.PaytmPageRedirectService;
import com.paytm.pg.merchant.PaytmChecksum;

@Service
public class PaytmPageRedirectServiceImpl implements PaytmPageRedirectService {

	@Autowired
	private PaytmDetails paytmDetails;

	@Value("${paytm.mobile}")
	private String mobileNo;

	@Value("${paytm.email}")
	private String email;

	@Value("${paytm.payment.sandbox.merchantId}")
	private String mid;

	@Value("${paytm.payment.sandbox.callbackUrl}")
	private String callbackUrl;

	@Value("${paytm.payment.sandbox.channelId}")
	private String channelId;

	@Value("${paytm.payment.sandbox.paytmUrl}")
	private String paytmUrl;

	/*@Override
	public ModelAndView pageRedirect(PayTMRequest payTMRequest) throws Exception {
		ModelAndView modelAndView = new ModelAndView("redirect:" + paytmDetails.getPaytmUrl());
		TreeMap<String, String> parameters = new TreeMap<>();
		paytmDetails.getDetails().forEach((k, v) -> parameters.put(k, v)); // extracting all the values from
																			// PaytmDetails class and keeping in the
																			// parameter map
		parameters.put("MOBILE_NO", mobileNo);
		parameters.put("EMAIL", email);
		parameters.put("ORDER_ID", payTMRequest.getOrderId());
		parameters.put("TXN_AMOUNT", payTMRequest.getTransactionAmount());
		parameters.put("CUST_ID", payTMRequest.getCustomerId());
		String checkSum = getCheckSum(parameters);
		parameters.put("CHECKSUMHASH", checkSum);
		modelAndView.addAllObjects(parameters);
		return modelAndView;
	}*/

	// calculating checksum
	private String getCheckSum(TreeMap<String, String> parameters) throws Exception {
		return PaytmChecksum.generateSignature(parameters, paytmDetails.getMerchantKey());
	}

	@Override
	public PayTMPaymentUrlDTO pageRedirectUrl(PayTMRequest payTMRequest) throws Exception {
		TreeMap<String, String> parameters = new TreeMap<>();
		paytmDetails.getDetails().forEach((k, v) -> parameters.put(k, v)); // extracting all the values from
																			// PaytmDetails class and keeping in the
																			// parameter map
		parameters.put("MOBILE_NO", mobileNo);
		parameters.put("EMAIL", email);
		parameters.put("ORDER_ID", payTMRequest.getOrderId());
		parameters.put("TXN_AMOUNT", payTMRequest.getTransactionAmount());
		parameters.put("CUST_ID", payTMRequest.getCustomerId());
		String checkSum = getCheckSum(parameters);
		parameters.put("CHECKSUMHASH", checkSum);

		String charset = "UTF-8";
		String redirectUrl = String.format(
				"CALLBACK_URL=%s&CHANNEL_ID=%s&CHECKSUMHASH=%s&CUST_ID=%s&EMAIL=%s&INDUSTRY_TYPE_ID=%s&MID=%s&MOBILE_NO=%s&ORDER_ID=%s&TXN_AMOUNT=%s&WEBSITE=%s",
				URLEncoder.encode(callbackUrl, charset), URLEncoder.encode(channelId, charset),
				URLEncoder.encode(checkSum, charset), URLEncoder.encode(payTMRequest.getCustomerId(), charset),
				URLEncoder.encode(email, charset), URLEncoder.encode(paytmDetails.getIndustryTypeId(), charset),
				URLEncoder.encode(mid, charset), URLEncoder.encode(mobileNo, charset),
				URLEncoder.encode(payTMRequest.getOrderId(), charset),
				URLEncoder.encode(payTMRequest.getTransactionAmount(), charset),
				URLEncoder.encode(paytmDetails.getWebsite(), charset));

		URI url = new URI("" + paytmUrl + "?" + redirectUrl);
		PayTMPaymentUrlDTO urlDTO = new PayTMPaymentUrlDTO();
		urlDTO.setPaytmPaymentUrl(url.toString());
		return urlDTO;
	}

}
