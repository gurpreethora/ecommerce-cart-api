package com.tomtom.ecommerce.cart.mock;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.tomtom.ecommerce.cart.model.ProductOrder;

public class ProductOrderMockFactory {

	
	public static List<ProductOrder> getProductOrder() {
		List<ProductOrder> lstProductOrder = new ArrayList<ProductOrder>();
		lstProductOrder.add(getProductOrder("product1", 1, 5, BigDecimal.TEN));
		lstProductOrder.add(getProductOrder("product2", 2, 10, BigDecimal.ONE));
		return lstProductOrder;
	}
	
	public static ProductOrder getBlankProductOrder() {
		return new ProductOrder();
	}
	
	public static ProductOrder getDummyValuedProduct() {
		return getProductOrder("product1", 1, 5, BigDecimal.TEN);
	}
	
	
	public static ProductOrder getProductOrder(String productName, Integer productId,
					Integer productQuantity, BigDecimal productPrice) {
		ProductOrder productOrder = new ProductOrder();
		productOrder.setProductId(productId);
		productOrder.setProductQuantity(productQuantity);
		return productOrder;
	}

}
