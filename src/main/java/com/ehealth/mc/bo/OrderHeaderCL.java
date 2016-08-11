package com.ehealth.mc.bo;

import java.io.Serializable;

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
@Table(name = "order_header_cl")
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class OrderHeaderCL implements Serializable, Persistable<Long> {

	private static final long serialVersionUID = 4468871930096946529L;

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "order_header_cl_gen")
	@SequenceGenerator(name = "order_header_cl_gen", sequenceName = "order_header_cl_gen", allocationSize = 1)
	@Column(name = "id")
	private Long id;

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
