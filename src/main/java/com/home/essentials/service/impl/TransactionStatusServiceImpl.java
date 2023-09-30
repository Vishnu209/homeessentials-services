package com.home.essentials.service.impl;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;

import com.home.essentials.model.PayTMPayments;
import com.home.essentials.repository.PayTMPaymentsRepository;
import com.home.essentials.service.TransactionStatusService;
import com.paytm.pg.merchant.PaytmChecksum;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TransactionStatusServiceImpl implements TransactionStatusService {
	
	@Autowired
	private PayTMPaymentsRepository payTMPaymentsRepository;
	
	@Value("${paytm.payment.sandbox.merchantId}")
	private String mid;
	
	@Value("${paytm.payment.sandbox.merchantKey}")
	private String merchantKey;

	
	@Override
	public String transactionStatus(String orderId) throws Exception {
		/* initialize an object */
		JSONObject paytmParams = new JSONObject();

		/* body parameters */
		JSONObject body = new JSONObject();

		/* Find your MID in your Paytm Dashboard at https://dashboard.paytm.com/next/apikeys */
		body.put("mid", mid);

		/* Enter your order id which needs to be check status for */
		body.put("orderId", orderId);

		/**
		* Generate checksum by parameters we have in body
		* You can get Checksum JAR from https://developer.paytm.com/docs/checksum/
		* Find your Merchant Key in your Paytm Dashboard at https://dashboard.paytm.com/next/apikeys 
		*/
		String checksum = PaytmChecksum.generateSignature(body.toString(), merchantKey);
		/* head parameters */
		JSONObject head = new JSONObject();

		/* put generated checksum value here */
		head.put("signature", checksum);

		/* prepare JSON string for request */
		paytmParams.put("body", body);
		paytmParams.put("head", head);
		String post_data = paytmParams.toString();

		/* for Staging */
		URL url = new URL("https://securegw-stage.paytm.in/v3/order/status");

		/* for Production */
		// URL url = new URL("https://securegw.paytm.in/v3/order/status");
		 String responseData = "";
		try {
		    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		    connection.setRequestMethod("POST");
		    connection.setRequestProperty("Content-Type", "application/json");
		    connection.setDoOutput(true);

		    DataOutputStream requestWriter = new DataOutputStream(connection.getOutputStream());
		    requestWriter.writeBytes(post_data);
		    requestWriter.close();
		   
		    InputStream is = connection.getInputStream();
		    BufferedReader responseReader = new BufferedReader(new InputStreamReader(is));
		    if ((responseData = responseReader.readLine()) != null) {
		        System.out.append("Response: " + responseData);
		    }
		    // System.out.append("Request: " + post_data);
		    responseReader.close();
		} catch (Exception exception) {
		    exception.printStackTrace();
		}

		return responseData;
	}


	@Override
	public PayTMPayments getPayments(String paymentId) throws Exception {
		PayTMPayments payResponse = null;
		Optional<PayTMPayments> payment = payTMPaymentsRepository.findById(paymentId);
		if(payment.isPresent()) {
			 payResponse = payment.get();
			 return payResponse;
		}else {
			throw new Exception("payment id not present");
		}
	}


	@Override
	public List<PayTMPayments> getAllPayTMPayments() {
		
		return payTMPaymentsRepository.findAll();
	}

	
}
