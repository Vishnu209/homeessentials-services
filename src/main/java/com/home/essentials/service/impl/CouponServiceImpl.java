package com.home.essentials.service.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.home.essentials.exception.ItemNotFoundException;
import com.home.essentials.model.Coupon;
import com.home.essentials.model.User;
import com.home.essentials.repository.CouponRepository;
import com.home.essentials.repository.UserRepository;
import com.home.essentials.service.CouponService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CouponServiceImpl implements CouponService {

	@Autowired
	private CouponRepository couponRepository;

	@Autowired
	private UserRepository userRepository;

	@Override
	public Coupon registerCoupon(Coupon coupon) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		mapper.findAndRegisterModules().disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		coupon.setDeletedFlag(false);
		coupon.setCouponCode(coupon.getCouponCode().toUpperCase());
		coupon.setCouponDiscount(coupon.getCouponDiscount());
		coupon.setCouponValid(true);
		String user = SecurityContextHolder.getContext().getAuthentication().getName();
		Optional<User> loggedUser = userRepository.findByUsername(user);
		coupon.setCreatedBy(loggedUser.get().getFirstName() + " " + loggedUser.get().getLastName());
		coupon = couponRepository.save(coupon);
		return coupon;
	}

	@Override
	public Optional<Coupon> getCoupon(String couponId) throws Exception {
		Optional<Coupon> coupon = couponRepository.findById(couponId);
		if (coupon.isPresent()) {
			Coupon couponnDb = coupon.get();
			if (!couponnDb.isDeletedFlag()) // checking if deletedFlag is false
				return coupon;
			else
				throw new ItemNotFoundException(couponId);
		}
		return null;
	}

	public List<Coupon> getAllCoupons() throws Exception {
		List<Coupon> allCoupons = couponRepository.findAll();
		List<Coupon> coupons = new ArrayList<>();
		allCoupons.stream().forEach(x ->{
			if(!x.isDeletedFlag())
				coupons.add(x);
			else
				allCoupons.retainAll(allCoupons);
		});				
		return coupons;
	}
	
	
	@Override
	public Coupon updateCoupon(Coupon coupon) throws Exception {
		Optional<Coupon> couponInDB = couponRepository.findById(coupon.getId());
		if (couponInDB.isPresent()) {
			Coupon editCoupon = couponInDB.get();
			ObjectMapper mapper = new ObjectMapper();
			mapper.findAndRegisterModules().disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
			coupon.setDeletedFlag(false);
			coupon.setCreatedDate(editCoupon.getCreatedDate());
			coupon.setLastupdatedDate(Instant.now());
			String user = SecurityContextHolder.getContext().getAuthentication().getName();
			Optional<User> loggedUser =userRepository.findByUsername(user);
			coupon.setUpdatedBy(loggedUser.get().getFirstName() + " " + loggedUser.get().getLastName());
			coupon = couponRepository.save(coupon);
			return coupon;
		} else
			throw new ItemNotFoundException(coupon.getId());
	}

	@Override
	public String deleteCoupon(String couponId) throws Exception {
		Optional<Coupon> couponInDb = couponRepository.findById(couponId);
		if (couponInDb.isPresent()) {
			Coupon couponToDelete = couponInDb.get();
			couponToDelete.setDeletedFlag(true);
			couponToDelete = couponRepository.save(couponToDelete);
			return "coupon Deleted";
		} else
			return "coupon doesnot exist";
	}

}
