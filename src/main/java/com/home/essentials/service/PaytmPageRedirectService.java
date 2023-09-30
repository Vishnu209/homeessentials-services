package com.home.essentials.service;

import com.home.essentials.dto.PayTMPaymentUrlDTO;
import com.home.essentials.model.PayTMRequest;

public interface PaytmPageRedirectService {

	//ModelAndView pageRedirect(PayTMRequest payTMRequest) throws Exception;
	
	PayTMPaymentUrlDTO pageRedirectUrl(PayTMRequest payTMRequest) throws Exception;
}
