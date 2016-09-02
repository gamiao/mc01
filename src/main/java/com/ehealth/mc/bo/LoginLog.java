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
@Table(name = "login_log")
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class LoginLog implements Serializable, Persistable<Long> {

	private static final long serialVersionUID = -4302858314532457229L;

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "login_log_gen")
	@SequenceGenerator(name = "login_log_gen", sequenceName = "login_log_gen", allocationSize = 1)
	@Column(name = "id")
	private Long id;

	@Column(name = "type")
	private String type;

	@Column(name = "login")
	private String login;

	@Column(name = "password")
	private String password;

	@Column(name = "ip")
	private String ip;

	@Column(name = "ua")
	private String ua;

	@Column(name = "result")
	private String result;

	@Column(name = "result_content", length = 1000)
	private String resultContent;

	@Column(name = "create_time")
	private Date createTime;

	@Override
	public boolean isNew() {
		return id == null;
	}

	@Override
	public Long getId() {
		return this.id;
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

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getUa() {
		return ua;
	}

	public void setUa(String ua) {
		this.ua = ua;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getResultContent() {
		return resultContent;
	}

	public void setResultContent(String resultContent) {
		this.resultContent = resultContent;
	}

}
