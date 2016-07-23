package com.ehealth.mc.bo;

import java.io.Serializable;
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
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "order_header")
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class OrderHeader implements Serializable {

	private static final long serialVersionUID = 8176352130924072536L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Integer id;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "d_id")
	private Doctor doctor;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "p_id")
	private Patient patient;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "od_id")
	private OrderDetail orderDetail;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "orderHeader", fetch = FetchType.LAZY)
	private List<OrderConversation> orderConversations;

	@Column(name = "status")
	private String status;

	@Column(name = "is_archived")
	private int isArchived;

	@Column(name = "is_enabled")
	private int isEnabled;

	@Column(name = "is_deleted")
	private int isDeleted;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
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

	public int getIsArchived() {
		return isArchived;
	}

	public void setIsArchived(int isArchived) {
		this.isArchived = isArchived;
	}

	public int getIsEnabled() {
		return isEnabled;
	}

	public void setIsEnabled(int isEnabled) {
		this.isEnabled = isEnabled;
	}

	public int getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(int isDeleted) {
		this.isDeleted = isDeleted;
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

}
