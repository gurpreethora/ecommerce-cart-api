package com.tomtom.ecommerce.cart.mock;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.tomtom.ecommerce.cart.model.Product;
import com.tomtom.ecommerce.cart.model.ResponseStatus;

public class ProductMockFactory {

	
	public static List<Product> getProducts() {
		List<Product> lstProducts = new ArrayList<Product>();
		lstProducts.add(getProduct("product1", 1, 5, BigDecimal.TEN));
		lstProducts.add(getProduct("product2", 2, 10, BigDecimal.ONE));
		return lstProducts;
	}
	
	public static Product getBlankProduct() {
		return new Product();
	}
	
	public static Product getDummyValuedProduct() {
		return getProduct("product1", 1, 5, BigDecimal.TEN);
	}
	
	
	public static Product getProduct(String productName, Integer productId,
					Integer productQuantity, BigDecimal productPrice) {
		Product product = new Product();
		product.setProductId(productId);
		product.setProductName(productName);
		product.setProductPrice(productPrice);
		product.setProductQuantity(productQuantity);
		return product;
	}

	public static ResponseStatus getResponseStatusProduct(Product inputproduct) {
		ResponseStatus responseStatus = new ResponseStatus();
		List<Product> products = new ArrayList<>();
		products.add(inputproduct);
		responseStatus.setProducts(products);
		return responseStatus;
	}

}
