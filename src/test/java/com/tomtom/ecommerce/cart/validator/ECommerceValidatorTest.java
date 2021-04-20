package com.tomtom.ecommerce.cart.validator;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.springframework.validation.Errors;

import com.tomtom.ecommerce.cart.model.ProductQuantityCart;

public class ECommerceValidatorTest {

	Errors errors = mock(Errors.class);
	ECommerceValidator eCommerceValidator = new ECommerceValidator();
	
	private final String EXPECTED_POSITIVE_NUMERIC= " expected positive numeric value";
	private final String IS_MANDATORY = " is mandatory";
	
	@Test
	public void productQuantityCart_MandatoryValidatortest() {
		eCommerceValidator.validate(new ProductQuantityCart(), errors);
		verify(errors, times(1)).rejectValue("ProductQuantity", "" , IS_MANDATORY);
		verify(errors, times(1)).rejectValue("ProductId", "" , IS_MANDATORY);
		reset(errors);
	}
	
	@Test
	public void productQuantityCart_inValidInputs_Validatortest() {
		ProductQuantityCart productQuantityCart = new ProductQuantityCart();
		productQuantityCart.setProductId(-1);
		productQuantityCart.setProductQuantity(0);
		eCommerceValidator.validate(productQuantityCart, errors);
		verify(errors, times(1)).rejectValue("ProductQuantity", "" , " can be positive or negative only");
		verify(errors, times(1)).rejectValue("ProductId", "" ,EXPECTED_POSITIVE_NUMERIC);
		reset(errors);
	}

	@Test
	public void testSupports() {
		ECommerceValidator eCommerceValidator = new ECommerceValidator();
		assertTrue(eCommerceValidator.supports(ProductQuantityCart.class));
	}
}
