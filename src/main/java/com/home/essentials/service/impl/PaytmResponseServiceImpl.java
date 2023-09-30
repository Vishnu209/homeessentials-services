package com.home.essentials.service.impl;

import java.time.Instant;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.thymeleaf.context.Context;

import com.home.essentials.model.HomeNeedsOrder;
import com.home.essentials.model.PayTMPayments;
import com.home.essentials.model.PaymentDetails;
import com.home.essentials.repository.OrderRepository;
import com.home.essentials.repository.PayTMPaymentsRepository;
import com.home.essentials.repository.UserRepository;
import com.home.essentials.service.PaytmResponseService;
import com.paytm.pg.merchant.PaytmChecksum;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PaytmResponseServiceImpl implements PaytmResponseService {

	@Autowired
	private PayTMPaymentsRepository payTMPaymentsRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private OrderRepository orderRepo;

	@Value("${paytm.mobile}")
	private String mobileNo;

	@Value("${paytm.email}")
	private String email;

	@Value("${paytm.payment.sandbox.merchantKey}")
	private String merchantKey;

	
	private static final String PayTMPayments = "paymentReceipt";
	
	
	@Override
	public String getResponseFromPayTM(HttpServletRequest httpServletRequest, Model model) throws Exception {
		Map<String, String[]> mapData = httpServletRequest.getParameterMap();
		TreeMap<String, String> parameters = new TreeMap<String, String>();
		mapData.forEach((key, val) -> parameters.put(key, val[0]));
		String paytmCheckSum = "";
		if (mapData.containsKey("CHECKSUMHASH")) {
			paytmCheckSum = mapData.get("CHECKSUMHASH")[0];
		}
		String result;

		boolean isValidCheckSum = false;
		log.info("RESULT : " + parameters.toString());

		try {
			isValidCheckSum = validateCheckSum(parameters, paytmCheckSum);
			if (isValidCheckSum && parameters.containsKey("RESPCODE")) {
				if (parameters.get("RESPCODE").equals("01"))
					result = "Payment Successful";
				else
					result = "Payment Failed";

			} else
				result = "Checksum mismatched";
		} catch (Exception e) {
			result = e.toString();
		}
		PayTMPayments paymentReceipt = new PayTMPayments();
		paymentReceipt.setBankName(parameters.get("BANKNAME"));
		paymentReceipt.setBankTxnId(parameters.get("BANKTXNID"));
		paymentReceipt.setCreated_date(Instant.now());
		// paymentReceipt.setCreatedBy(loggedUser.get().getFirstName() + " " +
		// loggedUser.get().getLastName());
		paymentReceipt.setCurrency(parameters.get("CURRENCY"));
		paymentReceipt.setGatewayName(parameters.get("GATEWAYNAME"));
		paymentReceipt.setMid(parameters.get("MID"));
		paymentReceipt.setOrderId(parameters.get("ORDERID"));
		paymentReceipt.setPaymentMode(parameters.get("PAYMENTMODE"));
		paymentReceipt.setResponseCode(parameters.get("RESPCODE"));
		paymentReceipt.setResponseMessage(parameters.get("RESPMSG"));
		paymentReceipt.setStatus(parameters.get("STATUS"));
		paymentReceipt.setTxnAmount(parameters.get("TXNAMOUNT"));
		paymentReceipt.setTxnDate(parameters.get("TXNDATE"));
		paymentReceipt.setTxnId(parameters.get("TXNID"));
		payTMPaymentsRepository.save(paymentReceipt);
		// after the payment, the response is captured in result variable and map to
		// report.html

		Optional<HomeNeedsOrder> orderInDb = orderRepo.findById(paymentReceipt.getOrderId());
		if (orderInDb.isPresent()) {
			HomeNeedsOrder order = orderInDb.get();

			order.setPayment_method("PAYTM");
			order.setPaymentStatus(paymentReceipt.getStatus());
			if (paymentReceipt.getStatus().equalsIgnoreCase("TXN_FAILURE")) {
				order.setPaymentFailureReason(paymentReceipt.getResponseMessage());
			}
			PaymentDetails paymentDetails = new PaymentDetails();
			paymentDetails.setBrand(paymentReceipt.getBankName());
			paymentDetails.setFunding(paymentReceipt.getPaymentMode());
			paymentDetails.setBankTxnId(paymentReceipt.getBankTxnId());
			paymentDetails.setGatewayName(paymentReceipt.getGatewayName());
			paymentDetails.setId(paymentReceipt.getId());
			
			order.setPaymentDetails(paymentDetails);
			orderRepo.save(order);

		}
		model.addAttribute("result", result);
		//model.addAttribute("redirectURL", "http:localhost:4200/#/order-success?key="+parameters.get("ORDERID"));
		parameters.remove("CHECKSUMHASH");
		model.addAttribute("parameters", parameters);
		
		Locale locale = Locale.forLanguageTag("en");
		Context context = new Context(locale);
		context.setVariable(PayTMPayments, paymentReceipt);
		return "report";
	}

	private boolean validateCheckSum(TreeMap<String, String> parameters, String paytmChecksum) throws Exception {
		return PaytmChecksum.verifySignature(parameters, merchantKey, paytmChecksum);
	}

}
