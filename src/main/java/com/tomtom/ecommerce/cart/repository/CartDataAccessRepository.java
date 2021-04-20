package com.tomtom.ecommerce.cart.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.tomtom.ecommerce.cart.model.CartDetails;
@Repository
public interface CartDataAccessRepository  extends CrudRepository<CartDetails, String>{ 

}