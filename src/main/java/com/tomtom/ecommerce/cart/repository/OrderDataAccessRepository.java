package com.tomtom.ecommerce.cart.repository;

import org.springframework.data.repository.CrudRepository;

import com.tomtom.ecommerce.cart.model.OrderDetails;

public interface OrderDataAccessRepository  extends CrudRepository<OrderDetails, String>{ 

}