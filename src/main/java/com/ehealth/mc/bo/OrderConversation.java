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
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.domain.Persistable;

@Entity
@Table(name = "order_conv")
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class OrderConversation implements Serializable, Persistable<Long> {

	private static final long serialVersionUID = -5671018062113749028L;

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "order_conv_gen")
	@SequenceGenerator(name = "order_conv_gen", sequenceName = "order_conv_gen", allocationSize = 1)
	@Column(name = "id")
	private Long id;

	@Column(name = "type")
	private String type;

	@Column(name = "title")
	private String title;

	@Column(name = "description")
	private String description;

	@Column(name = "pictures")
	private String pictures;
	
	@Column(name = "create_time")
	private Date createTime;

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "oh_id", referencedColumnName = "id")
	private OrderHeader orderHeader;

	public OrderHeader getOrderHeader() {
		return orderHeader;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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

	public String getPictures() {
		return pictures;
	}

	public void setPictures(String pictures) {
		this.pictures = pictures;
	}

	public void setOrderHeader(OrderHeader orderHeader) {
		this.orderHeader = orderHeader;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Override
	public boolean isNew() {
		return null == id;
	}

}
