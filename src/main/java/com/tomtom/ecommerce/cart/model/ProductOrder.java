package com.tomtom.ecommerce.cart.model;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
@Entity
public class ProductOrder {
	
	@Id
	@GeneratedValue
	private Integer productOrderId;
	private Integer productId;
	private String productName;
	private BigDecimal productPrice;
	private Integer productQuantity;
	private BigDecimal totalPrice;
	
	public Integer getProductOrderId() {
		return productOrderId;
	}
	public void setProductOrderId(Integer productOrderId) {
		this.productOrderId = productOrderId;
	}
	public Integer getProductId() {
		return productId;
	}
	public void setProductId(Integer productId) {
		this.productId = productId;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public BigDecimal getProductPrice() {
		return productPrice;
	}
	public void setProductPrice(BigDecimal productPrice) {
		this.productPrice = productPrice;
	}
	public Integer getProductQuantity() {
		return productQuantity;
	}
	public void setProductQuantity(Integer productQuantity) {
		this.productQuantity = productQuantity;
	}
	public BigDecimal getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}
	
	
}
