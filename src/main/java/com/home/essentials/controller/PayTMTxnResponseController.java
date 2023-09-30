package com.home.essentials.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;

import com.home.essentials.service.PaytmResponseService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Controller
@Api(value = "PayTM Transaction Response Controller", description = "REST APIs for PayTM Transaction Response", tags = {
		"PayTM Transaction Response Controller" })
public class PayTMTxnResponseController {

	@Autowired
	private PaytmResponseService paytmResponseService;

	@PostMapping(value = "/paytm/paymentResponse", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "After SuccessFul payment, the response is obtained in the redirect url   paytm/paymentResponse")
	public String getResponseRedirect(HttpServletRequest httpServletRequest, Model model) throws Exception {
		return paytmResponseService.getResponseFromPayTM(httpServletRequest, model);
	}
}
