package com.home.essentials.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.home.essentials.model.HomeNeedsOrder;
import com.home.essentials.model.OrderStatus;


public interface OrderRepository extends MongoRepository<HomeNeedsOrder, String> {

	@Query("{'user_id':?0,'deleted_flag':false}")
	List<HomeNeedsOrder> getOrdersByUserId(String userId,Sort sort); 
	
	List<HomeNeedsOrder> getOrdersByStatus(OrderStatus status,Sort sort);

}
