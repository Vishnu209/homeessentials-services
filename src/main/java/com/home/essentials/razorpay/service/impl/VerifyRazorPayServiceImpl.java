package com.home.essentials.razorpay.service.impl;

import java.security.SignatureException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.home.essentials.dto.RazorPaySuccessDTO;
import com.home.essentials.model.HomeNeedsOrder;
import com.home.essentials.model.PaymentDetails;
import com.home.essentials.model.RazorPayAcquirerData;
import com.home.essentials.model.RazorPayCardInfo;
import com.home.essentials.model.RazorPayOrder;
import com.home.essentials.model.RazorPayPaymentDetails;
import com.home.essentials.payload.VerifyRazorPayRequest;
import com.home.essentials.razorpay.service.VerifyRazorPayService;
import com.home.essentials.repository.OrderRepository;
import com.home.essentials.repository.RazorPayOrderRepository;
import com.home.essentials.repository.RazorPayPaymentDetailsRepository;
import com.razorpay.Card;
import com.razorpay.Order;
import com.razorpay.Payment;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class VerifyRazorPayServiceImpl implements VerifyRazorPayService {

	@Autowired
	private RazorPayPaymentDetailsRepository razorPayPaymentDetailsRepo;
	
	@Autowired
	private RazorPayOrderRepository rpayOrderRepo;
	
	@Autowired
	private OrderRepository orderRepo;

	@Value("${razorpay.keyId}")
	private String keyId;

	@Value("${razorpay.keySecret}")
	private String keySecret;

	private static final String HMAC_SHA256_ALGORITHM = "HmacSHA256";


	@Override
	public HomeNeedsOrder verifyRazorPay(VerifyRazorPayRequest verifyRequest) throws Exception {

		 HomeNeedsOrder homeNeedsOrder = null;
		String generatedSignature = calculateRFC2104HMAC(
				verifyRequest.getRPayorderId() + "|" + verifyRequest.getRazorpayPaymentId(), keySecret);
		log.info("generated signature = " + generatedSignature);

		RazorPaySuccessDTO  razorPaySucceessDTO = new RazorPaySuccessDTO();
		
		if (generatedSignature.equals(verifyRequest.getRazorpaySignature())) {
			try {
			 RazorPayPaymentDetails paymentDetails =  saveRazorPaymentDetails(verifyRequest);
			 razorPaySucceessDTO.setRazorPayPaymentDetails(paymentDetails);
			 razorPaySucceessDTO.setPaymentStatus("SUCCESS");
			 
			 Optional<RazorPayOrder> rpayOrd = rpayOrderRepo.findByRpayOrderId(verifyRequest.getRPayorderId()); 
			 if(rpayOrd.isPresent()) {
				 RazorPayOrder rpayOrder = rpayOrd.get();
				 Optional<HomeNeedsOrder> order= orderRepo.findById(rpayOrder.getNotes().getHomeOrderId());
				 if(order.isPresent()) {
					 homeNeedsOrder  = order.get();
					 
				 }else {
					 throw new Exception("Order not found");
				 }
			 }else {
				 throw new Exception("Razor Pay Order not found");
			 }

			} catch (Exception e) {

				log.error(e.getMessage());
			}
			
		} else {
			throw new Exception("Signature verfication failed");
		}
		return homeNeedsOrder;
	}

	public String calculateRFC2104HMAC(String data, String secret) throws java.security.SignatureException {

		String result;

		try {

			// get an hmac_sha256 key from the raw secret bytes
			SecretKeySpec signingKey = new SecretKeySpec(secret.getBytes(), HMAC_SHA256_ALGORITHM);

			// get an hmac_sha256 Mac instance and initialize with the signing key
			Mac mac = Mac.getInstance(HMAC_SHA256_ALGORITHM);
			mac.init(signingKey);

			// compute the hmac on input data bytes
			byte[] rawHmac = mac.doFinal(data.getBytes());

			// base64-encode the hmac
			result = DatatypeConverter.printHexBinary(rawHmac).toLowerCase();

		} catch (Exception e) {
			throw new SignatureException("Failed to generate HMAC : " + e.getMessage());
		}
		return result;

	}

	public RazorPayPaymentDetails saveRazorPaymentDetails(VerifyRazorPayRequest verifyRequest) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		mapper.findAndRegisterModules().disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

		RazorPayPaymentDetails razorPaymentDetails = new RazorPayPaymentDetails();

		try {
			RazorpayClient razorpay = new RazorpayClient(keyId, keySecret);
			Payment payment = razorpay.Payments.fetch(verifyRequest.getRazorpayPaymentId());

			razorPaymentDetails.setEntity(payment.get("entity"));
			razorPaymentDetails.setAmount(payment.get("amount"));
			razorPaymentDetails.setCurrency(payment.get("currency"));
			razorPaymentDetails.setStatus(payment.get("status"));
			razorPaymentDetails.setMethod(payment.get("method"));

			RazorPayAcquirerData acquirerData = new RazorPayAcquirerData();

			if (razorPaymentDetails.getMethod().equalsIgnoreCase("card")) {

				RazorpayClient razorpayReq = new RazorpayClient(keyId, keySecret);
				Card card = razorpayReq.Cards.fetch(payment.get("card_id"));
				log.info(card.toString());

				RazorPayCardInfo cardInfo = new RazorPayCardInfo();
				cardInfo.setCardId(payment.get("card_id"));
				cardInfo.setCardEmi(card.get("emi"));
				cardInfo.setCardEntity(card.get("entity"));
				cardInfo.setCardHolderName(card.get("name"));
				cardInfo.setCardInternational(card.get("international"));
				if (card.get("issuer").equals(null)) {
					cardInfo.setCardIssuer("");

				} else {
					cardInfo.setCardIssuer(card.get("issuer"));
				}
				cardInfo.setCardLast4(card.get("last4"));
				cardInfo.setCardNetwork(card.get("network"));
				cardInfo.setCardType(card.get("type"));
				cardInfo.setCreatedDate(Instant.now());

				String acqdata = payment.get("acquirer_data").toString();
				JSONObject acquirerDataJsonObject = new JSONObject(acqdata);
				String data = acquirerDataJsonObject.get("auth_code").toString();

				acquirerData.setAuthCode(data);
				acquirerData.setCreatedDate(Instant.now());

				razorPaymentDetails.setRazorPayCardInfo(cardInfo);
				razorPaymentDetails.setRazorPayAcquirerData(acquirerData);
				
			
			}

			if (razorPaymentDetails.getMethod().equalsIgnoreCase("upi")) {
				razorPaymentDetails.setVpa(payment.get("vpa"));

				String acqdata = payment.get("acquirer_data").toString();

				JSONObject acquirerDataJsonObject = new JSONObject(acqdata);
				String rrn = acquirerDataJsonObject.get("rrn").toString();
				String upiTransactionId = acquirerDataJsonObject.get("upi_transaction_id").toString();
				acquirerData.setRrn(rrn);
				acquirerData.setUpiTransactionId(upiTransactionId);
				acquirerData.setCreatedDate(Instant.now());
				razorPaymentDetails.setRazorPayAcquirerData(acquirerData);
			}
			
			if (razorPaymentDetails.getMethod().equalsIgnoreCase("netbanking")) {
				razorPaymentDetails.setBank(payment.get("bank"));

				String acqdata = payment.get("acquirer_data").toString();

				JSONObject acquirerDataJsonObject = new JSONObject(acqdata);
				String bankTransactionId = acquirerDataJsonObject.get("bank_transaction_id").toString();
				acquirerData.setBankTransactionId(bankTransactionId);
				acquirerData.setCreatedDate(Instant.now());
				razorPaymentDetails.setRazorPayAcquirerData(acquirerData);
				
			}

			if (razorPaymentDetails.getMethod().equalsIgnoreCase("wallet")) {
				razorPaymentDetails.setWallet(payment.get("wallet"));

				String acqdata = payment.get("acquirer_data").toString();

				JSONObject acquirerDataJsonObject = new JSONObject(acqdata);
				String walletTransactionId = acquirerDataJsonObject.get("transaction_id").toString();
				acquirerData.setWalletTransactionId(walletTransactionId);
				acquirerData.setCreatedDate(Instant.now());
				razorPaymentDetails.setRazorPayAcquirerData(acquirerData);
			}

			razorPaymentDetails.setRpayPaymentId(verifyRequest.getRazorpayPaymentId());
			razorPaymentDetails.setRpaySignature(verifyRequest.getRazorpaySignature());
			razorPaymentDetails.setRpayOrderId(payment.get("order_id"));
			Order order = razorpay.Orders.fetch(payment.get("order_id"));
			String notes = order.get("notes").toString();
			log.info("notes  = " + notes);
			JSONObject notesJsonObject = new JSONObject(notes);
			String customerId = notesJsonObject.get("customerId").toString();
			log.info("customerId = " + customerId);
			razorPaymentDetails.setCustomerId(customerId);
			String homeOrderId = notesJsonObject.get("homeOrderId").toString();
			log.info("homeOrderId = " + homeOrderId);
			razorPaymentDetails.setHomeOrderId(homeOrderId);
			if (payment.get("invoice_id").equals(null))
				razorPaymentDetails.setInvoiceId("");
			else
				razorPaymentDetails.setInvoiceId(payment.get("invoice_id"));

			razorPaymentDetails.setDescription(payment.get("description"));
			razorPaymentDetails.setEmail(payment.get("email"));
			razorPaymentDetails.setContact(payment.get("contact"));
			razorPaymentDetails.setFee(payment.get("fee"));
			razorPaymentDetails.setTax(payment.get("tax"));
			razorPaymentDetails.setCreatedDate(Instant.now());

			razorPayPaymentDetailsRepo.save(razorPaymentDetails);
			
			if (razorPaymentDetails.getMethod().equalsIgnoreCase("card")) {
				Optional<HomeNeedsOrder> orderInDb = orderRepo.findById(razorPaymentDetails.getHomeOrderId());
				if(orderInDb.isPresent()) {
					HomeNeedsOrder orderByCard = orderInDb.get();
					orderByCard.setPayment_method("RAZORPAY");
					orderByCard.setPaymentStatus("TXN_SUCCESS");
					
					PaymentDetails paymentDetails = new PaymentDetails();
					paymentDetails.setBrand(razorPaymentDetails.getRazorPayCardInfo().getCardNetwork());
					paymentDetails.setLast4(razorPaymentDetails.getRazorPayCardInfo().getCardLast4());
					paymentDetails.setFunding(razorPaymentDetails.getRazorPayCardInfo().getCardType());
					paymentDetails.setId(razorPaymentDetails.getId());
					
					orderByCard.setPaymentDetails(paymentDetails);
					orderRepo.save(orderByCard);
				}
			}
			
			if (razorPaymentDetails.getMethod().equalsIgnoreCase("upi")) {
				Optional<HomeNeedsOrder> orderInDb = orderRepo.findById(razorPaymentDetails.getHomeOrderId());
				if(orderInDb.isPresent()) {
					HomeNeedsOrder orderByUpi = orderInDb.get();
					orderByUpi.setPayment_method("RAZORPAY");
					orderByUpi.setPaymentStatus("TXN_SUCCESS");
					
					PaymentDetails paymentDetails = new PaymentDetails();
					paymentDetails.setBrand(razorPaymentDetails.getVpa());
					paymentDetails.setFunding(razorPaymentDetails.getMethod());
					paymentDetails.setBankTxnId(razorPaymentDetails.getRazorPayAcquirerData().getUpiTransactionId());
					paymentDetails.setId(razorPaymentDetails.getId());
					
					orderByUpi.setPaymentDetails(paymentDetails);
					orderRepo.save(orderByUpi);
				}
			}
			
			if (razorPaymentDetails.getMethod().equalsIgnoreCase("netbanking")) {
				Optional<HomeNeedsOrder> orderInDb = orderRepo.findById(razorPaymentDetails.getHomeOrderId());
				if(orderInDb.isPresent()) {
					HomeNeedsOrder orderByNetBanking = orderInDb.get();
					orderByNetBanking.setPayment_method("RAZORPAY");
					orderByNetBanking.setPaymentStatus("TXN_SUCCESS");
					
					PaymentDetails paymentDetails = new PaymentDetails();
					paymentDetails.setBrand(razorPaymentDetails.getBank());
					paymentDetails.setFunding(razorPaymentDetails.getMethod());
					paymentDetails.setBankTxnId(razorPaymentDetails.getRazorPayAcquirerData().getBankTransactionId());
					paymentDetails.setId(razorPaymentDetails.getId());
					
					orderByNetBanking.setPaymentDetails(paymentDetails);
					orderRepo.save(orderByNetBanking);
				}
			}
			
			if (razorPaymentDetails.getMethod().equalsIgnoreCase("wallet")) {
				Optional<HomeNeedsOrder> orderInDb = orderRepo.findById(razorPaymentDetails.getHomeOrderId());
				if(orderInDb.isPresent()) {
					HomeNeedsOrder orderByWallet = orderInDb.get();
					orderByWallet.setPayment_method("RAZORPAY");
					orderByWallet.setPaymentStatus("TXN_SUCCESS");
					
					PaymentDetails paymentDetails = new PaymentDetails();
					paymentDetails.setBrand(razorPaymentDetails.getWallet());
					paymentDetails.setFunding(razorPaymentDetails.getMethod());
					paymentDetails.setId(razorPaymentDetails.getId());
					
					orderByWallet.setPaymentDetails(paymentDetails);
					orderRepo.save(orderByWallet);
				}
			}
			

		} catch (RazorpayException e) {
			log.error(e.getMessage());
		}

		return razorPaymentDetails;

	}
	
	
	public String getPaymentsForRpayOrder(String rpayOrderId) throws RazorpayException {
		
		RazorpayClient razorpay = new RazorpayClient(keyId, keySecret);
		
		List<Payment> payments = razorpay.Orders.fetchPayments(rpayOrderId);
		
		return payments.toString();
	}

}
