package com.home.essentials.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.home.essentials.model.Coupon;

public interface CouponRepository extends MongoRepository<Coupon, String> {

}
