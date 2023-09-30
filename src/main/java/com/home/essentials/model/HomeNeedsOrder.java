package com.home.essentials.model;

import java.time.Instant;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Data;

@Data
@Document(collection = "Order")
public class HomeNeedsOrder {

	@Transient
    public static final String SEQUENCE_NAME = "orders_sequence";
	
	@Id
	private String id;
	
	@Field("sequence_name")
	private String sequenceName;

	@Field("payment_method")
	private String payment_method;

	@Field("user_id")
	private String userId;

	@Field("payment_status")
	private String paymentStatus;
	
	@Field("payment_failue_reason")
	private String paymentFailureReason;
	
	@Field("payment_method_title")
	private String payment_method_title;
	
	@Field("payment_details")
	private PaymentDetails paymentDetails;

	@Field("billing_details")
	private BillingDetails billingDetails;

	@Field("shipping_details")
	private ShippingDetails shippingDetails;
	
	@Field("delivery_charge")
	private double deliveryCharge;

	@Field("shopping_cart_entry")
	private List<ShoppingCartEntry> shoppingCartEntry ;
	
	@Field("cart_amount")
	private double cartAmount;

	@Field("total_amount_to_pay")
	private double Total_Amount_to_Pay;
	
	@Field("order_number")
	private String orderNumber;
	
	@Field("order_date_for_email")
	private String orderDateForEmail;
	
	@Field("order_status")
	private OrderStatus status;
	
	@Field("reason_for_cancellation")
	private String reasonForCancellation;
	
	@Field("mode_of_order")
	private String modeOfOrder;

	@Field("deleted_flag")
	private boolean deletedFlag =false;

	@Field("created_date")
	private Instant createdDate;

	@Field("last_updated_date")
	private Instant lastupdatedDate ;
	
	@Field("created_by")
	private String createdby;
	
	@Field("updated_by")
	private String updatedby;
}
