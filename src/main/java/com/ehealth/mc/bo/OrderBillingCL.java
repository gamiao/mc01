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
@Table(name = "order_billing_cl")
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class OrderBillingCL implements Serializable, Persistable<Long> {

	private static final long serialVersionUID = 86129381992412246L;

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "order_billing_cl_gen")
	@SequenceGenerator(name = "order_billing_cl_gen", sequenceName = "order_billing_cl_gen", allocationSize = 1)
	@Column(name = "id")
	private Long id;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "ob_id")
	@Fetch(FetchMode.JOIN)
	private OrderBilling orderBilling;

	@Column(name = "type")
	private String type;

	@Column(name = "owner")
	private String owner;

	@Column(name = "is_deleted")
	private String isDeleted;

	@Column(name = "title")
	private String title;

	@Column(name = "description", length = 1000)
	private String description;

	@Column(name = "before_change", length = 1000)
	private String beforeChange;

	@Column(name = "after_change", length = 1000)
	private String afterChange;

	@Column(name = "create_time")
	private Date createTime;

	public OrderBilling getOrderBilling() {
		return orderBilling;
	}

	public void setOrderBilling(OrderBilling orderBilling) {
		this.orderBilling = orderBilling;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
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

	public String getBeforeChange() {
		return beforeChange;
	}

	public void setBeforeChange(String beforeChange) {
		this.beforeChange = beforeChange;
	}

	public String getAfterChange() {
		return afterChange;
	}

	public void setAfterChange(String afterChange) {
		this.afterChange = afterChange;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
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
