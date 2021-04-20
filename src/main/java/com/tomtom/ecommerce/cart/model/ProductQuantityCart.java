package com.tomtom.ecommerce.cart.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;
@Entity
public class ProductQuantityCart {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@ApiModelProperty(hidden=true)
	private Integer productQuantityId;
	private Integer productId;
	private Integer productQuantity;

	@JsonIgnore
	public Integer getProductQuantityId() {
		return productQuantityId;
	}

	public void setProductQuantityId(Integer productQuantityId) {
		this.productQuantityId = productQuantityId;
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public Integer getProductQuantity() {
		return productQuantity;
	}

	public void setProductQuantity(Integer productQuantity) {
		this.productQuantity = productQuantity;
	}
	
	
}
