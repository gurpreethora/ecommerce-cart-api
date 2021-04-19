package com.tomtom.ecommerce.cart.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.tomtom.ecommerce.cart.exception.EmptyCartECommerceException;
import com.tomtom.ecommerce.cart.exception.InvalidQuantityECommerceException;
import com.tomtom.ecommerce.cart.exception.ProductNotFoundECommerceException;
import com.tomtom.ecommerce.cart.exception.ProductNotInStockECommerceException;
import com.tomtom.ecommerce.cart.mock.CartDetailsMockFactory;
import com.tomtom.ecommerce.cart.mock.ProductMockFactory;
import com.tomtom.ecommerce.cart.mock.ProductQuantityMockFactory;
import com.tomtom.ecommerce.cart.model.CartDetails;
import com.tomtom.ecommerce.cart.model.OrderDetails;
import com.tomtom.ecommerce.cart.model.Product;
import com.tomtom.ecommerce.cart.model.ProductQuantityCart;
import com.tomtom.ecommerce.cart.repository.CartDataAccessRepository;
import com.tomtom.ecommerce.cart.repository.OrderDataAccessRepository;
import com.tomtom.ecommerce.cart.repository.ProductDataAccessRepository;
@ExtendWith(MockitoExtension.class)
public class ECommerceServiceImplTest {

	@InjectMocks
	private ECommerceCartServiceImpl eCommerceServiceImpl;

	@Mock
	ProductDataAccessRepository productDataAccessRepository;
	
	@Mock
	CartDataAccessRepository cartDataAccessRepository;
	
	@Mock
	OrderDataAccessRepository orderDataAccessRepository;

	@Before
	public void initMocks() {
		MockitoAnnotations.initMocks(this);
	}

	private final String USER_ID = "userId";
	
	
	@Test
	public void getproductTest() throws ProductNotFoundECommerceException {
		
		Optional<Product> inputproduct = Optional.ofNullable(ProductMockFactory.getDummyValuedProduct());
		when(productDataAccessRepository.findById(inputproduct.get().getProductId())).thenReturn(inputproduct);
		Product productOp = eCommerceServiceImpl.getProduct(inputproduct.get().getProductId());
		assertNotNull(productOp);
		assertTrue(productOp.equals(productOp));
		assertEquals(inputproduct.get().getProductId(), productOp.getProductId());
	}
	
	@Test(expected = ProductNotFoundECommerceException.class)
	public void getproduct_ProductNotFoundECommerceExceptionTest() throws ProductNotFoundECommerceException {
		Optional<Product> inputproduct = Optional.ofNullable(null);
		when(productDataAccessRepository.findById(11)).thenReturn(inputproduct);
		eCommerceServiceImpl.getProduct(11);
	}
	
	@Test
	public void addProductsToCart_new_Test() throws InvalidQuantityECommerceException, ProductNotFoundECommerceException, ProductNotInStockECommerceException, EmptyCartECommerceException{
		ProductQuantityCart productQuantityCart = ProductQuantityMockFactory.getDummyValuedProduct();
		Product productWith5Quantity = ProductMockFactory.getDummyValuedProduct();
		
		when(cartDataAccessRepository.findById(USER_ID)).thenReturn(Optional.ofNullable(null));
		when(productDataAccessRepository.findById(productWith5Quantity.getProductId())).thenReturn(Optional.ofNullable(productWith5Quantity));
		when(cartDataAccessRepository.save(Mockito.any(CartDetails.class))).thenReturn(new CartDetails());
		
		OrderDetails orderDetails = eCommerceServiceImpl.addProductsToCart(USER_ID,productQuantityCart);
		assertNotNull(orderDetails);
		assertEquals(1, orderDetails.getLstProducts().size());
		assertEquals(USER_ID, orderDetails.getUserId());
		assertEquals(productWith5Quantity.getProductPrice().
				multiply(new BigDecimal(productWith5Quantity.getProductQuantity())),
				orderDetails.getOrderPrice());
	}
	
	@Test (expected = ProductNotInStockECommerceException.class)
	public void addProductsToCart_exisiting_Test() throws InvalidQuantityECommerceException, ProductNotFoundECommerceException, ProductNotInStockECommerceException, EmptyCartECommerceException{
		ProductQuantityCart productQuantityCart_10_products = ProductQuantityMockFactory.getDummyValuedProduct();
		Product productWith5Quantity = ProductMockFactory.getDummyValuedProduct();
		CartDetails cartDetails = CartDetailsMockFactory.getDummyValuedCartDetails();
		
		when(cartDataAccessRepository.findById(USER_ID)).thenReturn(Optional.ofNullable(cartDetails));
		when(productDataAccessRepository.findById(productWith5Quantity.getProductId())).thenReturn(Optional.ofNullable(productWith5Quantity));
		when(cartDataAccessRepository.save(Mockito.any(CartDetails.class))).thenReturn(new CartDetails());
		
		eCommerceServiceImpl.addProductsToCart(USER_ID,productQuantityCart_10_products);
	}
	
	@Test
	public void addProductsToCart_existing_Test() throws InvalidQuantityECommerceException, ProductNotFoundECommerceException, ProductNotInStockECommerceException, EmptyCartECommerceException{
		ProductQuantityCart productQuantityCart = ProductQuantityMockFactory.getDummyValuedProduct();
		productQuantityCart.setProductQuantity(4);									//trying to add 4 more to cart
		Product productWith5Quantity = ProductMockFactory.getDummyValuedProduct();	//total available products 5
		CartDetails cartDetails = CartDetailsMockFactory.getDummyValuedCartDetails();
		cartDetails.getLstProductQuantityCart().get(0).setProductQuantity(1); 		//setting existing quantity as 1
		cartDetails.getLstProductQuantityCart().remove(1);							// removing not present data in DB for product
		
		when(cartDataAccessRepository.findById(USER_ID)).thenReturn((Optional.ofNullable(cartDetails)));
		when(productDataAccessRepository.findById(productWith5Quantity.getProductId())).thenReturn(Optional.ofNullable(productWith5Quantity));
		when(cartDataAccessRepository.save(Mockito.any(CartDetails.class))).thenReturn(new CartDetails());
		
		OrderDetails orderDetails = eCommerceServiceImpl.addProductsToCart(USER_ID,productQuantityCart);
		assertNotNull(orderDetails);
		assertEquals(1, orderDetails.getLstProducts().size());
		assertEquals(cartDetails.getUserId(), orderDetails.getUserId());
		assertEquals(productWith5Quantity.getProductPrice().
				multiply(new BigDecimal(productWith5Quantity.getProductQuantity())),
				orderDetails.getOrderPrice());
	}
	
	@Test (expected = InvalidQuantityECommerceException.class)
	public void addProductsToCart_existing_InvalidQuantityECommerceException_Test() throws InvalidQuantityECommerceException, ProductNotFoundECommerceException, ProductNotInStockECommerceException, EmptyCartECommerceException{
		ProductQuantityCart productQuantityCart = ProductQuantityMockFactory.getDummyValuedProduct();
		productQuantityCart.setProductQuantity(-44);									//trying to remove 44 from cart
		Product productWith5Quantity = ProductMockFactory.getDummyValuedProduct();	//total available products 5
		CartDetails cartDetails = CartDetailsMockFactory.getDummyValuedCartDetails();
		cartDetails.getLstProductQuantityCart().get(0).setProductQuantity(1); 		//setting existing quantity as 1
		cartDetails.getLstProductQuantityCart().remove(1);							// removing not present data in DB for product
		
		when(cartDataAccessRepository.findById(USER_ID)).thenReturn((Optional.ofNullable(cartDetails)));
		when(productDataAccessRepository.findById(productWith5Quantity.getProductId())).thenReturn(Optional.ofNullable(productWith5Quantity));
		
		eCommerceServiceImpl.addProductsToCart(USER_ID,productQuantityCart);
	}
	

	@Test
	public void getUserCartTest() throws EmptyCartECommerceException, ProductNotFoundECommerceException{
		CartDetails cartDetails = CartDetailsMockFactory.getDummyValuedCartDetails();
		Product product1 = ProductMockFactory.getProduct("product1", 1, 5, BigDecimal.TEN);
		Product product2 = ProductMockFactory.getProduct("product2", 2, 10, BigDecimal.ONE);
		when(cartDataAccessRepository.findById(USER_ID)).thenReturn(Optional.ofNullable(cartDetails));
		when(productDataAccessRepository.findById(product1.getProductId())).thenReturn(Optional.ofNullable(product1));
		when(productDataAccessRepository.findById(product2.getProductId())).thenReturn(Optional.ofNullable(product2));
		OrderDetails orderDetails = eCommerceServiceImpl.getUserCart(USER_ID);
		assertNotNull(orderDetails);
		assertEquals(cartDetails.getUserId(), orderDetails.getUserId());
		assertEquals(product1.getProductId(),orderDetails.getLstProducts().get(0).getProductId());
		assertEquals(product2.getProductId(),orderDetails.getLstProducts().get(1).getProductId());
		assertEquals(product1.getProductPrice().
				multiply(new BigDecimal(product1.getProductQuantity())),
				orderDetails.getLstProducts().get(0).getTotalPrice());
		assertEquals(product2.getProductPrice().
				multiply(new BigDecimal(product2.getProductQuantity())),
				orderDetails.getLstProducts().get(1).getTotalPrice());
		
		assertEquals(new BigDecimal(60), orderDetails.getLstProducts().stream().map(x->x.getTotalPrice()).reduce(BigDecimal.ZERO, BigDecimal::add)); 
	}
	
	@Test (expected = EmptyCartECommerceException.class)
	public void getUserCart_EmptyCartECommerceExceptionTest() throws EmptyCartECommerceException, ProductNotFoundECommerceException{
		when(cartDataAccessRepository.findById(USER_ID)).thenReturn(Optional.ofNullable(null));
		eCommerceServiceImpl.getUserCart(USER_ID);
	}
	
	@Test (expected = ProductNotFoundECommerceException.class)
	public void getUserCart_ProductNotFoundECommerceExceptionTest() throws EmptyCartECommerceException, ProductNotFoundECommerceException{
		CartDetails cartDetails = CartDetailsMockFactory.getDummyValuedCartDetails();
		when(cartDataAccessRepository.findById(USER_ID)).thenReturn(Optional.ofNullable(cartDetails));
		eCommerceServiceImpl.getUserCart(USER_ID);
	}
	
	
	@Test
	public void deleteUserCartTest() {
		eCommerceServiceImpl.deleteUserCart(USER_ID);
		verify(cartDataAccessRepository).deleteById(USER_ID);
	}
}
