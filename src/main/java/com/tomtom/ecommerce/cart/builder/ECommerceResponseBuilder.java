package com.tomtom.ecommerce.cart.builder;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.tomtom.ecommerce.cart.model.OrderDetails;
import com.tomtom.ecommerce.cart.model.ResponseStatus;

public final class ECommerceResponseBuilder {
	private static final Logger LOGGER = LoggerFactory.getLogger(ECommerceResponseBuilder.class);

	private ECommerceResponseBuilder() {

	}

	public static ResponseEntity<ResponseStatus> buildResponse(String status, HttpStatus httpStatus, Object... objects) {
		com.tomtom.ecommerce.cart.model.ResponseStatus responseStatus = createResponse(status);
		if(objects != null) {
			for(Object object : objects) {

				if(object instanceof OrderDetails) {
						OrderDetails orderDetails = (OrderDetails) object;
						responseStatus.setOrderDetails(orderDetails);
				}  else if(object instanceof String) {
					LOGGER.debug("Possible exception {}", object);
					responseStatus.setMessages(Arrays.asList(((String) object))); 
				}
			}
		}
		return ResponseEntity.status(httpStatus).body(responseStatus);
	}
	public static ResponseStatus createResponse(String status) {
		ResponseStatus responseStatus = new ResponseStatus();
		responseStatus.setStatus(status);
		return responseStatus;
	}

}
