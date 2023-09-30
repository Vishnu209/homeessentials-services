package com.home.essentials.service;

import java.util.Optional;

import com.home.essentials.model.Coupon;

public interface CouponService {

	Coupon registerCoupon(Coupon coupon) throws Exception;

	Optional<Coupon> getCoupon(String couponId) throws Exception;

	Coupon updateCoupon(Coupon coupon) throws Exception;

	String deleteCoupon(String couponId) throws Exception;

}
