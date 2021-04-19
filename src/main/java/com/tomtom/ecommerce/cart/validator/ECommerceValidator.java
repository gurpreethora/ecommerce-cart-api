package com.tomtom.ecommerce.cart.validator;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.tomtom.ecommerce.cart.model.ProductQuantityCart;

@Configurable
public class ECommerceValidator implements Validator {

	private static final String IS_MANDATORY = " is mandatory";
	private static final String EXPECTED_POSITIVE = " expected positive numeric value";
	private static final String PRODUCT_ID = "ProductId";
	private static final String PRODUCT_QUANTITY = "ProductQuantity";	

	@Override
	public boolean supports(Class<?> clazz) {

		return ProductQuantityCart.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		if(target.getClass().equals(ProductQuantityCart.class)) {
			validateProductQuantityCart(target, errors);
		}
	}

	private void validateProductQuantityCart(Object target, Errors errors) {
		ProductQuantityCart productQuantityCart = (ProductQuantityCart) target;
		if(productQuantityCart.getProductQuantity() == null) {
			errors.rejectValue(PRODUCT_QUANTITY, "" , IS_MANDATORY);
		}else if(productQuantityCart.getProductQuantity()!= null && productQuantityCart.getProductQuantity()==0) {
			errors.rejectValue(PRODUCT_QUANTITY,"", " can be positive or negative only");
		}
		if(productQuantityCart.getProductId() == null) {
			errors.rejectValue(PRODUCT_ID, "" , IS_MANDATORY);
		}else if(productQuantityCart.getProductId()!= null && productQuantityCart.getProductId()<1) {
			errors.rejectValue(PRODUCT_ID,"", EXPECTED_POSITIVE);
		}
	}
}
