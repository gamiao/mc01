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
	
	@Column(name = "create_time")
	private Date createTime;

	@Column(name = "description")
	private String description;

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

	@Override
	public boolean isNew() {
		return null == id;
	}

}
