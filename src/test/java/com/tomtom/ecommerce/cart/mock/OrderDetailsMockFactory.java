package com.tomtom.ecommerce.cart.mock;

import java.math.BigDecimal;

import com.tomtom.ecommerce.cart.model.OrderDetails;
import com.tomtom.ecommerce.cart.model.PaymentMode;

public class OrderDetailsMockFactory {

	
	public static OrderDetails getOrderDetails() {
		return  new OrderDetails();
	}
	
	public static OrderDetails getDummyValuedOrderDetails() {
		return getOrderDetails("addressOne", PaymentMode.CASH, "user1", BigDecimal.TEN);
	}
	
	
	public static OrderDetails getOrderDetails(String address, PaymentMode paymentMode,
					String userId, BigDecimal productPrice) {
		OrderDetails orderDetails = new OrderDetails();
		orderDetails.setUserId(userId);
		orderDetails.setLstProducts(ProductOrderMockFactory.getProductOrder());
		return orderDetails;
	}
}
