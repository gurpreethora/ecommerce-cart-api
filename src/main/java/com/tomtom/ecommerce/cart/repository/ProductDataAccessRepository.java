package com.tomtom.ecommerce.cart.repository;

import org.springframework.data.repository.CrudRepository;

import com.tomtom.ecommerce.cart.model.Product;

public interface ProductDataAccessRepository  extends CrudRepository<Product, Integer>{ 

}