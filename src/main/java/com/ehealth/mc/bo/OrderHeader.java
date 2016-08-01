package com.ehealth.mc.bo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.data.domain.Persistable;

@Entity
@Table(name = "order_header")
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class OrderHeader implements Serializable, Persistable<Long> {

	private static final long serialVersionUID = 8176352130924072536L;

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "order_header_gen")
	@SequenceGenerator(name = "order_header_gen", sequenceName = "order_header_gen", allocationSize = 1)
	@Column(name = "id")
	private Long id;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "d_id")
	@Fetch(FetchMode.JOIN)
	private Doctor doctor;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "p_id")
	@Fetch(FetchMode.JOIN)
	private Patient patient;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "od_id")
	@Fetch(FetchMode.JOIN)
	private OrderDetail orderDetail;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "orderHeader", fetch = FetchType.LAZY)
	private List<OrderConversation> orderConversations;

	@Column(name = "status")
	private String status;
	
	@Column(name = "create_time")
	private Date createTime;

	@Column(name = "is_archived")
	private String isArchived;

	@Column(name = "is_enabled")
	private String isEnabled;

	@Column(name = "is_deleted")
	private String isDeleted;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Doctor getDoctor() {
		return doctor;
	}

	public void setDoctor(Doctor doctor) {
		this.doctor = doctor;
	}

	public Patient getPatient() {
		return patient;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
	}

	public OrderDetail getOrderDetail() {
		return orderDetail;
	}

	public void setOrderDetail(OrderDetail orderDetail) {
		this.orderDetail = orderDetail;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<OrderConversation> getOrderConversations() {
		return orderConversations;
	}

	public void setOrderConversations(List<OrderConversation> orderConversations) {
		this.orderConversations = orderConversations;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getIsArchived() {
		return isArchived;
	}

	public void setIsArchived(String isArchived) {
		this.isArchived = isArchived;
	}

	public String getIsEnabled() {
		return isEnabled;
	}

	public void setIsEnabled(String isEnabled) {
		this.isEnabled = isEnabled;
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

	@Override
	public boolean isNew() {
		return null == id;
	}

}
