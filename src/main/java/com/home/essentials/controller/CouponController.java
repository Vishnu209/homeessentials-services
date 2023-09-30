package com.home.essentials.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.home.essentials.model.Coupon;
import com.home.essentials.payload.FailureResponse;
import com.home.essentials.payload.SuccessResponse;
import com.home.essentials.repository.CouponRepository;
import com.home.essentials.service.impl.CouponServiceImpl;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value = "Coupon Controller", description = "REST API for Coupons", tags = { "Coupon Controller" })
public class CouponController {

	@Autowired
	private CouponServiceImpl couponServiceImpl;

	@Autowired
	private CouponRepository couponRepository;

	@PostMapping("/api/createCoupon")
	@ApiOperation(value = "Create Coupon", response = Coupon.class)
	public ResponseEntity createCoupon(@RequestBody Coupon coupon) throws Exception {
		HashMap<String, Object> resp = new HashMap<>();
		couponServiceImpl.registerCoupon(coupon);
		resp.put("coupon", coupon);
		return new ResponseEntity<>(resp, HttpStatus.OK);
	}

	@PutMapping(path = "/api/updateCoupon")
	@ApiOperation(value = "Update Coupon By Id", response = Coupon.class)
	public ResponseEntity updateCoupon(@RequestParam("id") String id, @RequestBody Coupon coupon) throws Exception {
		HashMap<String, Object> resp = new HashMap<>();
		if (couponRepository.existsById(id)) {
			couponServiceImpl.updateCoupon(coupon);
			resp.put("updated_Coupon", coupon);
			return new ResponseEntity<>(resp, HttpStatus.OK);
		} else
			return new ResponseEntity<>(new FailureResponse("Coupon  not found in the Database"), HttpStatus.NOT_FOUND);
	}

	@DeleteMapping("/api/deleteCoupon")
	@ApiOperation(value = "Delete Coupon By Id")
	public ResponseEntity deleteCoupon(@RequestParam("id") String id) throws Exception {
		if (couponRepository.existsById(id)) {
			couponServiceImpl.deleteCoupon(id);
			return new ResponseEntity<>(new SuccessResponse("Coupon deleted successfully"), HttpStatus.OK);
		} else
			return new ResponseEntity<>(new FailureResponse("Coupon not found in the Database"), HttpStatus.NOT_FOUND);
	}

	@GetMapping("/api/getCoupon")
	@ApiOperation(value = "Get Coupon By Id", response = Coupon.class)
	public ResponseEntity getCoupon(@RequestParam("id") String id) throws Exception {
		Optional<Coupon> coupon = couponServiceImpl.getCoupon(id);
		if (couponRepository.existsById(id))
			return new ResponseEntity<>(coupon, HttpStatus.OK);
		else
			return new ResponseEntity<>(new FailureResponse("Coupon  doesnot exists in the database"),
					HttpStatus.NOT_FOUND);
	}

	@GetMapping("/api/getAllCoupons")
	@ApiOperation(value = "Get All Coupons")
	public ResponseEntity getAllCoupons() throws Exception {
		List<Coupon> coupons = couponServiceImpl.getAllCoupons();
		return new ResponseEntity<>(coupons, HttpStatus.OK);
	}

}
