package com.ehealth.mc.bo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name = "doctor_notification")
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class DoctorNotification implements Serializable, Persistable<Long> {

	private static final long serialVersionUID = -2986816346532356488L;

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "doctor_notification_gen")
	@SequenceGenerator(name = "doctor_notification_gen", sequenceName = "doctor_notification_gen", allocationSize = 1)
	@Column(name = "id")
	private Long id;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "d_id")
	@Fetch(FetchMode.JOIN)
	private Doctor doctor;

	@Column(name = "doctor_detail", length = 1000)
	private String doctorDetail;

	@Column(name = "type")
	private String type;

	@Column(name = "create_time")
	private Date createTime;

	@Column(name = "is_deleted")
	private String isDeleted;

	@Column(name = "mail_result")
	private String mailResult;

	@Column(name = "title")
	private String title;

	@Column(name = "description", length = 1000)
	private String description;

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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
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

	public Doctor getDoctor() {
		return doctor;
	}

	public void setDoctor(Doctor doctor) {
		this.doctor = doctor;
	}

	public String getDoctorDetail() {
		return doctorDetail;
	}

	public void setDoctorDetail(String doctorDetail) {
		this.doctorDetail = doctorDetail;
	}

	public String getMailResult() {
		return mailResult;
	}

	public void setMailResult(String mailResult) {
		this.mailResult = mailResult;
	}

}
