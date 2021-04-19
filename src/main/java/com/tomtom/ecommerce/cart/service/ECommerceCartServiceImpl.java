package com.tomtom.ecommerce.cart.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.tomtom.ecommerce.cart.exception.EmptyCartECommerceException;
import com.tomtom.ecommerce.cart.exception.InvalidQuantityECommerceException;
import com.tomtom.ecommerce.cart.exception.ProductNotFoundECommerceException;
import com.tomtom.ecommerce.cart.exception.ProductNotInStockECommerceException;
import com.tomtom.ecommerce.cart.model.CartDetails;
import com.tomtom.ecommerce.cart.model.OrderDetails;
import com.tomtom.ecommerce.cart.model.Product;
import com.tomtom.ecommerce.cart.model.ProductOrder;
import com.tomtom.ecommerce.cart.model.ProductQuantityCart;
import com.tomtom.ecommerce.cart.model.ResponseStatus;
import com.tomtom.ecommerce.cart.repository.CartDataAccessRepository;

/**
 * @author Gurpreet Hora
 *
 */
@Service
@Transactional
public class ECommerceCartServiceImpl implements ECommerceCartService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ECommerceCartServiceImpl.class);
	
	@Autowired
    private RestTemplate restTemplate;
	
	@Value("${ecommerce.product.api.name}")
    private String ecommerceProductApiName;

	private final CartDataAccessRepository cartDataAccessRepository;
	
	@Autowired
	public ECommerceCartServiceImpl(CartDataAccessRepository cartDataAccessRepository) {
		super();
		this.cartDataAccessRepository = cartDataAccessRepository;
	}
	
	private String getProductApiURL(){
		return "http://" +ecommerceProductApiName +"/product/";
	}
	//Gets product details from ecommerce-product-api
	public Product getProduct(Integer productId) throws ProductNotFoundECommerceException  {
		LOGGER.debug("Trying to get product if{}" ,productId);
		Optional<ResponseStatus> responseStatus = Optional.ofNullable(restTemplate.getForObject(this.getProductApiURL()+productId, ResponseStatus.class));
		if(responseStatus.isPresent() && !responseStatus.get().getProducts().isEmpty() &&
				responseStatus.get().getProducts()!=null && responseStatus.get().getProducts().stream().findFirst().isPresent()){
			LOGGER.debug("Product found for product id {}" ,productId);
			return responseStatus.get().getProducts().stream().findFirst().get();
		} else {
			throw new ProductNotFoundECommerceException("Product not found for supplied productId : "+productId);
		}
	}
	
	/**
	 * This method adds/removes product cart. It also checks if the product is already in cart
	 * For adding productQuantity is expected positive, for removing it is expected negative.
	 * If the product is found in cart then the quantity of existing product will be updated 
	 */
	@Override
	public OrderDetails addProductsToCart(String userId, ProductQuantityCart productQuantityCart) throws InvalidQuantityECommerceException, ProductNotFoundECommerceException, ProductNotInStockECommerceException, EmptyCartECommerceException {
		LOGGER.debug("Trying to add products to cart for user {}" , userId);
		Optional<CartDetails> existingCart = this.cartDataAccessRepository.findById(userId);
		if (existingCart.isPresent()) {
			updateExistingCart(productQuantityCart, existingCart);
			cartDataAccessRepository.save(existingCart.get());
			LOGGER.debug("Transaction successful to add/update product to existing cart for user {}" , userId);
			return calculateCartValue(existingCart.get());
		} else {
			checkIfProductandQuantityExists(productQuantityCart.getProductId(), productQuantityCart.getProductQuantity());
			CartDetails cartDetails = new CartDetails();
			cartDetails.setUserId(userId);
			List<ProductQuantityCart> lstProductQuantityCart = new ArrayList<>();
			lstProductQuantityCart.add(productQuantityCart);
			cartDetails.setLstProductQuantityCart(lstProductQuantityCart);
			cartDataAccessRepository.save(cartDetails);
			LOGGER.debug("Transaction successful to add product to new cart for user {}" , userId);
			return calculateCartValue(cartDetails);
		}
	}

	/**
	 * @param product
	 * @param existingCart
	 * @throws InvalidQuantityECommerceException
	 * @throws ProductNotFoundECommerceException 
	 * @throws ProductNotInStockECommerceException 
	 */
	public void updateExistingCart(ProductQuantityCart productQuantityCart, Optional<CartDetails> existingCart)
			throws InvalidQuantityECommerceException, ProductNotFoundECommerceException, ProductNotInStockECommerceException {
		if(existingCart.isPresent() && !existingCart.get().getLstProductQuantityCart().isEmpty()){
			List<ProductQuantityCart> lstProductQuantityCart = new ArrayList<>();
			boolean productFoundInCart = false;
			for(ProductQuantityCart existingProduct : existingCart.get().getLstProductQuantityCart()) {
				if(existingProduct.getProductId().equals(productQuantityCart.getProductId())) {
					productFoundInCart=true;
					if(existingProduct.getProductQuantity() + productQuantityCart.getProductQuantity()<0) {
						throw new InvalidQuantityECommerceException("Existing quantity for same product is "+existingProduct.getProductQuantity()+" "
								+ "and new quantity supplied is "+productQuantityCart.getProductQuantity()+", final quantity cannot be less than zero !");
					} else {
						checkIfProductandQuantityExists(productQuantityCart.getProductId(), existingProduct.getProductQuantity() + productQuantityCart.getProductQuantity());
						existingProduct.setProductQuantity(existingProduct.getProductQuantity() + productQuantityCart.getProductQuantity());
					}
				}
			} if(!productFoundInCart) {
				lstProductQuantityCart.add(productQuantityCart);
			}
			existingCart.get().getLstProductQuantityCart().addAll(lstProductQuantityCart);
		}
	}

	/**
	 * @param productId
	 * @param productQuantityNeeded
	 * @throws ProductNotFoundECommerceException
	 * @throws ProductNotInStockECommerceException
	 * This method is responsible for checking if the product exists in database (Stock)
	 * It also checks if the required quantity is available in database (Stock)
	 */
	private void checkIfProductandQuantityExists(Integer productId, Integer productQuantityNeeded)
			throws ProductNotFoundECommerceException, ProductNotInStockECommerceException {
		int availableQuantityInStock = this.getProduct(productId).getProductQuantity();
		if(availableQuantityInStock < productQuantityNeeded) {
			throw new ProductNotInStockECommerceException("Available quantity for product is only : " +availableQuantityInStock
					+ " user tried adding " +productQuantityNeeded);
		}
	}

	@Override
	public OrderDetails getUserCart(String userId) throws EmptyCartECommerceException, ProductNotFoundECommerceException {
		Optional<CartDetails> existingCart = this.cartDataAccessRepository.findById(userId);
		if (existingCart.isPresent()) {
			return calculateCartValue(existingCart.get());
		} else {
			throw new EmptyCartECommerceException("No Products in cart for user : "+userId);
		}
	}

	/**
	 * @param cartDetails
	 * @return
	 * @throws ProductNotFoundECommerceException
	 * @throws EmptyCartECommerceException
	 * This method is responsible for calculating overall amounts from cart.
	 * It calculates amount at product level as well at order level. 
	 */
	public OrderDetails calculateCartValue(CartDetails cartDetails) throws ProductNotFoundECommerceException, EmptyCartECommerceException {
		OrderDetails orderDetails =new OrderDetails();
		List <ProductOrder> lstProductOrder = new ArrayList<>();
		Product product;
		ProductOrder productOrder;
		LOGGER.debug("Calculating cart value for User {}" , cartDetails.getUserId());
		if(cartDetails.getLstProductQuantityCart()== null || cartDetails.getLstProductQuantityCart().isEmpty()) {
			return this.getUserCart(cartDetails.getUserId());
		}
		for(ProductQuantityCart productQuantityCart : cartDetails.getLstProductQuantityCart()) {
			productOrder = new ProductOrder();
			product = this.getProduct(productQuantityCart.getProductId());
			productOrder.setProductName(product.getProductName());
			productOrder.setProductPrice(product.getProductPrice());
			productOrder.setProductId(productQuantityCart.getProductId());
			productOrder.setProductQuantity(productQuantityCart.getProductQuantity());
			productOrder.setTotalPrice(new BigDecimal(productQuantityCart.getProductQuantity()).multiply(product.getProductPrice()));
			lstProductOrder.add(productOrder);
		}
		orderDetails.setUserId(cartDetails.getUserId());
		orderDetails.setLstProducts(lstProductOrder);
		return orderDetails;
	}

	@Override
	public void deleteUserCart(String userId) {
		this.cartDataAccessRepository.deleteById(userId);
	}
}
