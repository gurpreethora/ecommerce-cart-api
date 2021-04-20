package com.tomtom.ecommerce.cart.mock;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.tomtom.ecommerce.cart.model.ProductQuantityCart;

public class ProductQuantityMockFactory {

	
	public static List<ProductQuantityCart> getProductQuantity() {
		List<ProductQuantityCart> lstProductQuantityCart = new ArrayList<ProductQuantityCart>();
		lstProductQuantityCart.add(getProductQuantityCart("product1", 1, 5, BigDecimal.TEN));
		lstProductQuantityCart.add(getProductQuantityCart("product2", 2, 10, BigDecimal.ONE));
		return lstProductQuantityCart;
	}
	
	public static ProductQuantityCart getBlankProductQuantityCart() {
		return new ProductQuantityCart();
	}
	
	public static ProductQuantityCart getDummyValuedProduct() {
		return getProductQuantityCart("product1", 1, 5, BigDecimal.TEN);
	}
	
	
	public static ProductQuantityCart getProductQuantityCart(String productName, Integer productId,
					Integer productQuantity, BigDecimal productPrice) {
		ProductQuantityCart productQuantityCart = new ProductQuantityCart();
		productQuantityCart.setProductId(productId);
		productQuantityCart.setProductQuantity(productQuantity);
		return productQuantityCart;
	}

}
