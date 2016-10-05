package com.ehealth.mc.bo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.data.domain.Persistable;

@Entity
@Table(name = "order_billing")
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class OrderBilling implements Serializable, Persistable<Long> {

	private static final long serialVersionUID = 7915454042241873261L;

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "order_billing_gen")
	@SequenceGenerator(name = "order_billing_gen", sequenceName = "order_billing_gen", allocationSize = 1)
	@Column(name = "id")
	private Long id;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "oh_id")
	@Fetch(FetchMode.JOIN)
	private OrderHeader orderHeader;

	@Column(name = "type")
	private String type;

	@Column(name = "channel")
	private String channel;

	@Column(name = "is_deleted")
	private String isDeleted;

	@Column(name = "create_time")
	private Date createTime;

	@Column(name = "billing_code")
	private String billingCode;

	@Column(name = "product_code")
	private String productCode;

	@Column(name = "title")
	private String title;

	@Column(name = "description")
	private String description;

	@Column(name = "price")
	private Double price;

	@Column(name = "result")
	private String result;

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public OrderHeader getOrderHeader() {
		return orderHeader;
	}

	public void setOrderHeader(OrderHeader orderHeader) {
		this.orderHeader = orderHeader;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getBillingCode() {
		return billingCode;
	}

	public void setBillingCode(String billingCode) {
		this.billingCode = billingCode;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public boolean isNew() {
		return id == null;
	}

}
