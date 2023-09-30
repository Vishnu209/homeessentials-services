package com.home.essentials.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.home.essentials.model.RazorPayPaymentDetails;

public interface RazorPayPaymentDetailsRepository extends MongoRepository<RazorPayPaymentDetails, String>{

}
