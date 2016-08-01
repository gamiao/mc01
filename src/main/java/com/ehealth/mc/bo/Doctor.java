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
@Table(name = "doctor")
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class Doctor implements Serializable, Persistable<Long> {

	private static final long serialVersionUID = -7997871599991354097L;

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "doctor_gen")
	@SequenceGenerator(name = "doctor_gen", sequenceName = "doctor_gen", allocationSize = 1)
	@Column(name = "id")
	private Long id;

	@Column(name = "login")
	private String login;

	@Column(name = "password")
	private String password;

	@Column(name = "chinese_name")
	private String chineseName;

	@Column(name = "gender")
	private String gender;

	@Column(name = "avatar")
	private String avatar;

	@Column(name = "address")
	private String address;

	@Column(name = "mobile")
	private String mobile;

	@Column(name = "birthday")
	private String birthday;

	@Column(name = "medical_level")
	private String medicalLevel;
	
	@Column(name = "create_time")
	private Date createTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getChineseName() {
		return chineseName;
	}

	public void setChineseName(String chineseName) {
		this.chineseName = chineseName;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getMedicalLevel() {
		return medicalLevel;
	}

	public void setMedicalLevel(String medicalLevel) {
		this.medicalLevel = medicalLevel;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
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
