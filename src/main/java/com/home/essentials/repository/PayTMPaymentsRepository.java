package com.home.essentials.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.home.essentials.model.PayTMPayments;

public interface PayTMPaymentsRepository extends MongoRepository<PayTMPayments, String>{

}
