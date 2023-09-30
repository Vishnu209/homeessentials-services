package com.home.essentials.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;

import com.home.essentials.model.PayTMPayments;

public interface PaytmResponseService {

	String getResponseFromPayTM(HttpServletRequest httpServletRequest, Model model) throws Exception;
}
