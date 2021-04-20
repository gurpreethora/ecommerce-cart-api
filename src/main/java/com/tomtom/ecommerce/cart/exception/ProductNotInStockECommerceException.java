package com.tomtom.ecommerce.cart.exception;

public class ProductNotInStockECommerceException extends Exception {

	private static final long serialVersionUID = -3741760666064849357L;
	public ProductNotInStockECommerceException(String message) 
	{ 
		super(message); 
	}
}