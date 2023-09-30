package com.home.essentials.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.home.essentials.model.RazorPayOrder;

public interface RazorPayOrderRepository extends MongoRepository<RazorPayOrder, String>{

	@Query(value = "{'rPayOrderId':?0}")
	Optional<RazorPayOrder> findByRpayOrderId(String rpayOrderId);
}
