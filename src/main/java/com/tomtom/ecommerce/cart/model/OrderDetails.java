package com.tomtom.ecommerce.cart.model;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.annotations.CreationTimestamp;

import io.swagger.annotations.ApiModelProperty;
@Entity
public class OrderDetails {

	private String status;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@ApiModelProperty(value = "The order id", position = 1)
	private String orderId;
	@ApiModelProperty(value = "The Ordered By ", position = 2)
	private String userId;
	
	@OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
	List<ProductOrder> lstProducts;
	
	@Column(nullable = false, updatable = false)
	@CreationTimestamp
	private Date orderedOn;
	
	private BigDecimal orderPrice;

	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public List<ProductOrder> getLstProducts() {
		return lstProducts;
	}
	public void setLstProducts(List<ProductOrder> lstProducts) {
		this.lstProducts = lstProducts;
	}
	public BigDecimal getOrderPrice() {
		BigDecimal totalorderPrice = BigDecimal.ZERO;
		for (ProductOrder productOrder : getLstProducts()) {
			totalorderPrice = totalorderPrice.add(productOrder.getTotalPrice());
		}
		return totalorderPrice;
	}
	public void setOrderPrice(BigDecimal orderPrice) {
		this.orderPrice = orderPrice;
	}
	public Date getOrderedOn() {
		return orderedOn;
	}
	public void setOrderedOn(Date orderedOn) {
		this.orderedOn = orderedOn;
	}


}
