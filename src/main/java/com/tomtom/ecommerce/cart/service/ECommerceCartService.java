package com.tomtom.ecommerce.cart.service;

import com.tomtom.ecommerce.cart.exception.EmptyCartECommerceException;
import com.tomtom.ecommerce.cart.exception.InvalidQuantityECommerceException;
import com.tomtom.ecommerce.cart.exception.ProductNotFoundECommerceException;
import com.tomtom.ecommerce.cart.exception.ProductNotInStockECommerceException;
import com.tomtom.ecommerce.cart.model.OrderDetails;
import com.tomtom.ecommerce.cart.model.ProductQuantityCart;

public interface ECommerceCartService {

	OrderDetails addProductsToCart(String userId, ProductQuantityCart productQuantityCart) throws InvalidQuantityECommerceException, ProductNotFoundECommerceException, ProductNotInStockECommerceException, EmptyCartECommerceException;

	OrderDetails getUserCart(String userId) throws EmptyCartECommerceException, ProductNotFoundECommerceException;

	void deleteUserCart(String userId);

}
