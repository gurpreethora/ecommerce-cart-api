package com.tomtom.ecommerce.cart.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlElementWrapper;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;
@Entity
public class CartDetails {
	@Id
	@ApiModelProperty(hidden=true)
	private String userId;
	
	
	@OneToMany(cascade = {CascadeType.MERGE}, fetch = FetchType.LAZY)
	@XmlElementWrapper(name="products")
	private List<ProductQuantityCart> lstProductQuantityCart;
	
	@Transient
	private String address;
	@Transient
	private PaymentMode paymentMode;
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	@JsonIgnore
	public List<ProductQuantityCart> getLstProductQuantityCart() {
		return lstProductQuantityCart;
	}
	public void setLstProductQuantityCart(List<ProductQuantityCart> lstProductQuantityCart) {
		this.lstProductQuantityCart = lstProductQuantityCart;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public PaymentMode getPaymentMode() {
		return paymentMode;
	}
	public void setPaymentMode(PaymentMode paymentMode) {
		this.paymentMode = paymentMode;
	}
	
	
}
