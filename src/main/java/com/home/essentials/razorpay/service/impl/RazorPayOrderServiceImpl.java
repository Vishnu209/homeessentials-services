package com.home.essentials.razorpay.service.impl;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.home.essentials.error.EntityNotFoundException;
import com.home.essentials.model.RazorPayNotes;
import com.home.essentials.model.RazorPayOrder;
import com.home.essentials.payload.RazorPayCreateOrderRequest;
import com.home.essentials.razorpay.service.RazorPayOrderService;
import com.home.essentials.repository.RazorPayOrderRepository;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

@Service
public class RazorPayOrderServiceImpl implements RazorPayOrderService{
	
	@Autowired
	private RazorPayOrderRepository razorPayOrderRepository;
	
	@Value("${razorpay.keyId}")
	private String keyId;
	
	@Value("${razorpay.keySecret}")
	private String keySecret;
	
	@Override
	public RazorPayOrder createRazorPayOrder(RazorPayCreateOrderRequest req) throws RazorpayException {
		Instant date = Instant.now();
		ObjectMapper mapper = new ObjectMapper();
		mapper.findAndRegisterModules().disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		String currentDate = DateTimeFormatter.
				ofPattern("yyyyMMddHHmmss").withZone(ZoneId.systemDefault()).format(date);
		
		RazorpayClient razorpay = new RazorpayClient(keyId, keySecret);

		JSONObject orderRequest = new JSONObject();
		orderRequest.put("amount", req.getAmount() * 100); // amount in the smallest currency unit
		orderRequest.put("currency", req.getCurrency());
		orderRequest.put("receipt", "HOME_ORDER_" + currentDate);
		
		JSONObject notes = new JSONObject();
		notes.put("customerId", req.getCustomerId());
		notes.put("homeOrderId", req.getHomeOrderId());

		orderRequest.put("notes", notes);
		Order razorPayOrder = razorpay.Orders.create(orderRequest);
		
		RazorPayOrder ordRes = new RazorPayOrder();
		ordRes.setAmount(razorPayOrder.get("amount"));
		ordRes.setAmountPaid(razorPayOrder.get("amount_paid"));
		ordRes.setAttempts(razorPayOrder.get("attempts"));
		ordRes.setStatus(razorPayOrder.get("status"));
		RazorPayNotes rpayNotes = new RazorPayNotes();
		rpayNotes.setCustomerId(req.getCustomerId());
		rpayNotes.setHomeOrderId(req.getHomeOrderId());
		ordRes.setNotes(rpayNotes);		 
		ordRes.setCreatedDate(Instant.now());
		ordRes.setCurrency(razorPayOrder.get("currency"));
		ordRes.setEntity(razorPayOrder.get("entity"));
		ordRes.setReceipt(razorPayOrder.get("receipt"));
		ordRes.setRPayOrderId(razorPayOrder.get("id"));	
		ordRes.setKeyId(keyId);
		razorPayOrderRepository.save(ordRes);
		return ordRes;
	}

	@Override
	public RazorPayOrder getRpayOrder(String id) throws Exception {
		return razorPayOrderRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException(RazorPayOrder.class, "id", id));
	}

	@Override
	public List<RazorPayOrder> getAllRpayOrders() {
		
		return razorPayOrderRepository.findAll();
	}

	
}
