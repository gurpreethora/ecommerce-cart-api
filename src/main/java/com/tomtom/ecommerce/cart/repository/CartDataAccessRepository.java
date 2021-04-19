package com.tomtom.ecommerce.cart.repository;

import org.springframework.data.repository.CrudRepository;

import com.tomtom.ecommerce.cart.model.CartDetails;

public interface CartDataAccessRepository  extends CrudRepository<CartDetails, String>{ 

}