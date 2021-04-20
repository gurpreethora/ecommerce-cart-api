package com.tomtom.ecommerce.cart.controller;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.tomtom.ecommerce.cart.constants.ECommerceCartConstants;
import com.tomtom.ecommerce.cart.exception.EmptyCartECommerceException;
import com.tomtom.ecommerce.cart.exception.InvalidQuantityECommerceException;
import com.tomtom.ecommerce.cart.exception.ProductNotFoundECommerceException;
import com.tomtom.ecommerce.cart.exception.ProductNotInStockECommerceException;
import com.tomtom.ecommerce.cart.mock.OrderDetailsMockFactory;
import com.tomtom.ecommerce.cart.mock.ProductQuantityMockFactory;
import com.tomtom.ecommerce.cart.model.OrderDetails;
import com.tomtom.ecommerce.cart.model.ProductQuantityCart;
import com.tomtom.ecommerce.cart.model.ResponseStatus;
import com.tomtom.ecommerce.cart.service.ECommerceCartService;
@ExtendWith(MockitoExtension.class)
public class CartControllerTest {
	
	@InjectMocks
	private CartController cartController;
	
	@Mock
	private ECommerceCartService commerceService ;
	
	@Before
	public void initMocks() {
		MockitoAnnotations.initMocks(this);
	}

	private final String USER_ID = "userId";
	
	@Test
	public void addCartTest() throws InvalidQuantityECommerceException, ProductNotFoundECommerceException, ProductNotInStockECommerceException, EmptyCartECommerceException{
		ProductQuantityCart productQuantityCart = ProductQuantityMockFactory.getDummyValuedProduct(); 
		when(commerceService.addProductsToCart(USER_ID, productQuantityCart)).thenReturn(new OrderDetails());
		ResponseEntity<ResponseStatus> respo = cartController.addProductsToCart(USER_ID,productQuantityCart);
		assertNotNull(respo);
		assertEquals(HttpStatus.CREATED, respo.getStatusCode());
	}
	
	@Test
	public void accountDetails_NotFoundMoneyManagerExceptionTest() throws InvalidQuantityECommerceException, ProductNotFoundECommerceException, ProductNotInStockECommerceException, EmptyCartECommerceException{
		ProductQuantityCart productQuantityCart = ProductQuantityMockFactory.getDummyValuedProduct(); 
		when(commerceService.addProductsToCart(USER_ID,productQuantityCart)).thenThrow(new InvalidQuantityECommerceException("Invalid Quantity"));
		ResponseEntity<ResponseStatus> respo = cartController.addProductsToCart(USER_ID,productQuantityCart);
		assertNotNull(respo);
		assertEquals(HttpStatus.OK, respo.getStatusCode());
		assertEquals(ECommerceCartConstants.FAILURE, respo.getBody().getStatus());
		assertEquals("Invalid Quantity", respo.getBody().getMessages().get(0));
	}
	
	@Test
	public void getCartTest() throws EmptyCartECommerceException, ProductNotFoundECommerceException{
		when(commerceService.getUserCart(USER_ID)).thenReturn(OrderDetailsMockFactory.getDummyValuedOrderDetails());
		ResponseEntity<ResponseStatus> respo = cartController.getUserCart(USER_ID);
		assertNotNull(respo);
		assertEquals(ECommerceCartConstants.SUCCESS,respo.getBody().getStatus());
		assertEquals("In Cart",respo.getBody().getOrderDetails().getStatus());
		assertEquals(HttpStatus.OK, respo.getStatusCode());
	}
	
	@Test
	public void getCartTest_ProductNotFoundECommerceException() throws EmptyCartECommerceException, ProductNotFoundECommerceException{
		when(commerceService.getUserCart(USER_ID)).thenThrow(new ProductNotFoundECommerceException("Invalid Product"));
		ResponseEntity<ResponseStatus> respo = cartController.getUserCart(USER_ID);
		assertNotNull(respo);
		assertEquals(HttpStatus.OK, respo.getStatusCode());
		assertEquals(ECommerceCartConstants.FAILURE, respo.getBody().getStatus());
		assertEquals("Invalid Product", respo.getBody().getMessages().get(0));
	}
	
	@Test
	public void getCartTest_InvalidQuantityECommerceException() throws EmptyCartECommerceException, ProductNotFoundECommerceException{
		when(commerceService.getUserCart(USER_ID)).thenThrow(new EmptyCartECommerceException("Cart Empty"));
		ResponseEntity<ResponseStatus> respo = cartController.getUserCart(USER_ID);
		assertNotNull(respo);
		assertEquals(HttpStatus.OK, respo.getStatusCode());
		assertEquals(ECommerceCartConstants.FAILURE, respo.getBody().getStatus());
		assertEquals("Cart Empty", respo.getBody().getMessages().get(0));
	}
	
	@Test
	public void deleteUserCartTest() {
		Mockito.doNothing().when(commerceService).deleteUserCart(USER_ID);
		ResponseEntity<ResponseStatus> respo = cartController.deleteUserCart(USER_ID);
		assertNotNull(respo);
		assertEquals(ECommerceCartConstants.SUCCESS,respo.getBody().getStatus());
		assertEquals(HttpStatus.ACCEPTED, respo.getStatusCode());
	}
}
