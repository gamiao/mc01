package com.ehealth.mc.bo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.domain.Persistable;

@Entity
@Table(name = "order_detail")
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class OrderDetail implements Serializable, Persistable<Long> {

	private static final long serialVersionUID = 7047335901689944237L;

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "order_detail_gen")
	@SequenceGenerator(name = "order_detail_gen", sequenceName = "order_detail_gen", allocationSize = 1)
	@Column(name = "id")
	private Long id;

	@Column(name = "type")
	private String type;

	@Column(name = "title")
	private String title;

	@Column(name = "issue_age")
	private int issueAge;

	@Column(name = "create_time")
	private Date createTime;

	@Column(name = "description")
	private String description;

	@Column(name = "description2")
	private String description2;

	@Column(name = "description3")
	private String description3;

	@Column(name = "description4")
	private String description4;

	@Column(name = "description5")
	private String description5;

	@Column(name = "pictures")
	private String pictures;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getDescription2() {
		return description2;
	}

	public void setDescription2(String description2) {
		this.description2 = description2;
	}

	public String getDescription3() {
		return description3;
	}

	public void setDescription3(String description3) {
		this.description3 = description3;
	}

	public String getDescription4() {
		return description4;
	}

	public void setDescription4(String description4) {
		this.description4 = description4;
	}

	public String getDescription5() {
		return description5;
	}

	public void setDescription5(String description5) {
		this.description5 = description5;
	}

	@Override
	public boolean isNew() {
		return null == id;
	}

	public int getIssueAge() {
		return issueAge;
	}

	public void setIssueAge(int issueAge) {
		this.issueAge = issueAge;
	}

}
