package com.home.essentials.razorpay.service;

import java.security.SignatureException;

import com.home.essentials.model.HomeNeedsOrder;
import com.home.essentials.payload.VerifyRazorPayRequest;

public interface VerifyRazorPayService {

	HomeNeedsOrder verifyRazorPay(VerifyRazorPayRequest verifyRequest) throws SignatureException, Exception; 
}
