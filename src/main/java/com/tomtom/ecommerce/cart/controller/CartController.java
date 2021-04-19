package com.tomtom.ecommerce.cart.controller;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tomtom.ecommerce.cart.builder.ECommerceResponseBuilder;
import com.tomtom.ecommerce.cart.constants.ECommerceConstants;
import com.tomtom.ecommerce.cart.model.OrderDetails;
import com.tomtom.ecommerce.cart.model.ProductQuantityCart;
import com.tomtom.ecommerce.cart.model.ResponseStatus;
import com.tomtom.ecommerce.cart.service.ECommerceCartService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
@Api(value = "ECommerce Cart API")
@RestController
@RequestMapping(value = "ecommerce-cart-api/user")
public class CartController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CartController.class);

	@Autowired
	ECommerceCartService eCommerceService;
	
	@ApiOperation( value = "adds new/updates product to cart")
	@ApiResponses(value = {
			@ApiResponse(response = OrderDetails.class, message = "Success", code = 201)})
	@PostMapping("{userId}/cart/product")
	public ResponseEntity<ResponseStatus> addProductsToCart (@PathVariable @NotBlank @Size(min = 1, max = 50) String userId,
			@RequestBody @Valid ProductQuantityCart productQuantityCart){
		OrderDetails orderDetails;
		try {
			orderDetails = eCommerceService.addProductsToCart(userId, productQuantityCart);
			orderDetails.setUserId(userId);
		} catch (Exception e) {
			LOGGER.warn("Exception occured while adding product : ", e);
			return ECommerceResponseBuilder.buildResponse(ECommerceConstants.FAILURE,HttpStatus.OK, e.getMessage());
		}
		return ECommerceResponseBuilder.buildResponse(ECommerceConstants.SUCCESS, HttpStatus.CREATED, orderDetails);
	}
	
	@ApiOperation( value = "Gets products in users cart")
	@ApiResponses(value = {
			@ApiResponse(response = OrderDetails.class, message = ECommerceConstants.SUCCESS, code = 200)})
	@GetMapping (value = "{userId}/cart/")
	public ResponseEntity<ResponseStatus> getUserCart (@PathVariable @NotBlank @Size(min = 1, max = 50) String userId){
		OrderDetails orderDetails;
		try {
			orderDetails =  eCommerceService.getUserCart(userId);
			orderDetails.setStatus("In Cart");
		} catch (Exception e) {
			LOGGER.warn("Exception occured while getting user cart : ", e);
			return ECommerceResponseBuilder.buildResponse(ECommerceConstants.FAILURE, HttpStatus.OK,  e.getMessage());
		}
		return ECommerceResponseBuilder.buildResponse(ECommerceConstants.SUCCESS, HttpStatus.OK, orderDetails);
	}
	
	@ApiOperation( value = "Clear User's cart")
	@ApiResponses(value = {
			@ApiResponse(response = String.class, message = ECommerceConstants.SUCCESS, code = 200)})
	@DeleteMapping (value = "cart/{userId}")
	public ResponseEntity<ResponseStatus> deleteUserCart (@PathVariable @NotBlank @Size(min = 1, max = 50) String userId){
		try {
			eCommerceService.deleteUserCart(userId);
			LOGGER.debug("Cart cleared for user : {}", userId);
		} catch (Exception e) {
			LOGGER.warn("Exception occured while deleting cart : ", e);
			return ECommerceResponseBuilder.buildResponse(ECommerceConstants.FAILURE,HttpStatus.OK , e.getMessage());
		}
		return ECommerceResponseBuilder.buildResponse(ECommerceConstants.SUCCESS, HttpStatus.ACCEPTED);
	}

}
